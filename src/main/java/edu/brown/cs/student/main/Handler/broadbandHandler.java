package edu.brown.cs.student.main.Handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
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

    if (state == null || county == null) {
      responseMap.put("state", state);
      responseMap.put("county", county);
      responseMap.put("result", "error");
      responseMap.put("error_type", "error_bad_request");
      responseMap.put("details", state == null ? "error_arg_state" : "error_arg_county");
      return adapter.toJson(responseMap);
    }
    try {
      String stateCodesJson = this.sendStateCodeRequest();
      responseMap.put("result","success");
    } catch (Exception e) {

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

}
