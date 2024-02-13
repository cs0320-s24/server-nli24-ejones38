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
      String stateCodesJson = this.sendStateCodeRequest();
      List<State> stateList = CensusAPIUtilities.deserializeStateCodes(stateCodesJson);

      String stateCode = ""; //need to do exception catching
      int countyCode = -1;
      for (State state1 : stateList) {
        if (state1.getName().equals(state)) {
          stateCode = state1.getCode();
        }
      }
//      String countyCodesJson = this.sendCountyCodeRequest(stateCode);
//      List<County> countyList = CensusAPIUtilities.deserializeCountyCodes(countyCodesJson);
//      for (County county1: countyList) {
//        if (county1.getNAME().equals(county)) {
//          countyCode = county1.getCode();
//        }
//      }
      responseMap.put("stateCode", stateCode);
//      responseMap.put("countyCode", countyCode);
      responseMap.put("result","success");
      return adapter.toJson(responseMap);

    } catch (Exception e) {
      System.out.println("you failed");
    }
    return null;
  }

  private String sendStateCodeRequest() throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildStateCodeRequest = HttpRequest.newBuilder().uri
        (new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*")).GET().build();
    HttpResponse<String> sentStateCodeResponse =
        HttpClient.newBuilder().build().send(buildStateCodeRequest, HttpResponse.BodyHandlers.ofString());
    return sentStateCodeResponse.body();
  }

  private String sendCountyCodeRequest(int stateCode) throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildCountyCodeRequest = HttpRequest.newBuilder().uri
        (new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + String.valueOf(stateCode))).GET().build();
    HttpResponse<String> sentCountyCodeResponse =
        HttpClient.newBuilder().build().send(buildCountyCodeRequest, HttpResponse.BodyHandlers.ofString());
    return sentCountyCodeResponse.body();
  }

}
