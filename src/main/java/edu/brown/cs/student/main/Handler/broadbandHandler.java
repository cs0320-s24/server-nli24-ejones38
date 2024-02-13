package edu.brown.cs.student.main.Handler;

import static spark.Spark.connect;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CensusAPI.CensusAPIUtilities;
import edu.brown.cs.student.main.CensusAPI.County;
import edu.brown.cs.student.main.CensusAPI.State;
import java.io.IOException;
import java.lang.reflect.Type;
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
      Map<String, State> stateMap = CensusAPIUtilities.deserializeStateCodes();
      String stateCode = "-1"; //need to do exception catching
      String countyCode = "-1";
      if (stateMap.get(state) != null) {
        State state1 = stateMap.get(state);
        stateCode = state1.getCode();
      }
      else {
        responseMap.put("result", "error");
        responseMap.put("error_type", "error_bad_request");
        responseMap.put("details", "State does not exist");
        return adapter.toJson(responseMap);
      }


      Map<String, County> countyMap = CensusAPIUtilities.deserializeCountyCodes(stateCode);
      String fullCounty = county + ", " + state;

      if (countyMap.get(fullCounty) != null) {
        County county1 = countyMap.get(fullCounty);
        countyCode = county1.getCode();
      } else {
        responseMap.put("result", "error");
        responseMap.put("error_type", "error_bad_request");
        responseMap.put("details", "County does not exist");
        return adapter.toJson(responseMap);
      }

      List<List<String>> broadBandData = CensusAPIUtilities.deserializeBroadband(stateCode, countyCode);

      responseMap.put("data", broadBandData);
      responseMap.put("result","success");
      return adapter.toJson(responseMap);

    } catch (IOException e) {
      responseMap.put("result", "error");
      responseMap.put("error_type", "error_datasource");
      return adapter.toJson(responseMap);
    }
  }



}
