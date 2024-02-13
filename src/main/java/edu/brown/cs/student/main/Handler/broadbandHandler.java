package edu.brown.cs.student.main.Handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CensusAPIUtilities;
import edu.brown.cs.student.main.County;
import edu.brown.cs.student.main.State;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class broadbandHandler implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    Map<String,Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    responseMap.put("state", state);
    responseMap.put("county", county);

    if (state == null || county == null) {
      responseMap.put("result", "error");
      responseMap.put("error_type", "error_bad_request");
      responseMap.put("details", state == null ? "error_arg_state" : "error_arg_county");
      return adapter.toJson(responseMap);
    }
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      String dateTime = LocalDateTime.now().format(formatter);
      responseMap.put("date and time", dateTime);
      String stateCodesJson = this.sendStateCodeRequest();
      List<State> stateList = CensusAPIUtilities.deserializeStateCodes(stateCodesJson);
      String stateCode = "-1"; //need to do exception catching
      String countyCode = "-1";
      for (State state1 : stateList) {
        if (state1.getName().equals(state)) {
          stateCode = state1.getCode();
        }
      }
      if (stateCode.equals("-1")) {
        responseMap.put("result", "error");
        responseMap.put("error_type", "error_bad_request");
        responseMap.put("details", "State does not exist");
        return adapter.toJson(responseMap);
      }

      String countyCodesJson = this.sendCountyCodeRequest(stateCode);
      List<County> countyList = CensusAPIUtilities.deserializeCountyCodes(countyCodesJson);
      for (County county1: countyList) {
        if (county1.getNAME().contains(county)) {
          countyCode = county1.getCode();
        }
      }
      if (countyCode.equals("-1")) {
        responseMap.put("result", "error");
        responseMap.put("error_type", "error_bad_request");
        responseMap.put("details", "County does not exist");
        return adapter.toJson(responseMap);
      }
      String broadBandJson = this.sendBroadbandRequest(stateCode, countyCode);
      List<List<String>> broadBandData = CensusAPIUtilities.deserializeBroadband(broadBandJson);

      responseMap.put("data", broadBandData);
      responseMap.put("result","success");
      return adapter.toJson(responseMap);

    } catch (IOException e) {
      responseMap.put("result", "error");
      responseMap.put("error_type", "error_datasource");
      return adapter.toJson(responseMap);
    }
  }

  private String sendStateCodeRequest() throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildStateCodeRequest = HttpRequest.newBuilder().uri
        (new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*")).GET().build();
    HttpResponse<String> sentStateCodeResponse =
        HttpClient.newBuilder().build().send(buildStateCodeRequest, HttpResponse.BodyHandlers.ofString());
    return sentStateCodeResponse.body();
  }

  private String sendCountyCodeRequest(String stateCode) throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildCountyCodeRequest = HttpRequest.newBuilder().uri
        (new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode)).GET().build();
    HttpResponse<String> sentCountyCodeResponse =
        HttpClient.newBuilder().build().send(buildCountyCodeRequest, HttpResponse.BodyHandlers.ofString());
    return sentCountyCodeResponse.body();
  }

  private String sendBroadbandRequest(String stateCode, String countyCode) throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildBroadbandRequest = HttpRequest.newBuilder().uri
        (new URI("https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
            + countyCode + "&in=state:" + stateCode)).GET().build();
    HttpResponse<String> sentBroadbandResponse =
        HttpClient.newBuilder().build().send(buildBroadbandRequest, HttpResponse.BodyHandlers.ofString());
    return sentBroadbandResponse.body();
  }

}
