package edu.brown.cs.student.main.Handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.Cache.Datasource;
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

/**
 * Class responsible for handling requests to retrieve broadband data
 */
public class broadbandHandler implements Route {
  /**
   * Instance variable representing the data cache
   */
  private Datasource cache;

  /**
   * Constructor for broadbandHandler. Takes in a datasource that contains cache functionality
   * @param cache - the datasource representing the cache
   */
  public broadbandHandler(Datasource cache) {
    this.cache = cache;
  }

  /**
   * Secondary constructor for broadbandHandler. Used when no caching is wanted for the data
   */
  public broadbandHandler() {}

  /**
   * Method handles incoming broadband requests. Checks for proper params, and returns the correct response when given
   * malformed params. Else finds state and county codes, collects broadband info and stores all data in the cache for
   * later retrieval. Then returns the JSON with specified info
   * @param request - the incoming user request
   * @param response - the outgoing response back to the user
   * @return - a JSON string representing the broadband containing the specified broadband info along with date/time
   */
  @Override
  public Object handle(Request request, Response response) {
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    Map<String, Object> responseMap = new HashMap<>();
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
      Map<String, State> stateMap;

      if (this.cache != null) {
        stateMap = this.cache.getStates();
      } else {
        stateMap = CensusAPIUtilities.deserializeStateCodes();
      }

      String stateCode = "-1"; // need to do exception catching
      String countyCode = "-1";

      if (stateMap.get(state) != null) {
        State state1 = stateMap.get(state);
        stateCode = state1.getCode();
      } else {
        responseMap.put("result", "error");
        responseMap.put("error_type", "error_bad_request");
        responseMap.put("details", "State does not exist");
        return adapter.toJson(responseMap);
      }

      Map<String, County> countyMap;
      if (this.cache != null) {
        countyMap = this.cache.getCountyCache(stateCode);
      } else {
        countyMap = CensusAPIUtilities.deserializeCountyCodes(stateCode);
      }
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

      List<List<String>> broadBandData;
      if (this.cache != null) {
        broadBandData = this.cache.getBroadbandData(stateCode, countyCode);
      } else {
        broadBandData = CensusAPIUtilities.deserializeBroadband(stateCode, countyCode);
      }

      responseMap.put("data", broadBandData);
      responseMap.put("result", "success");
      return adapter.toJson(responseMap);

    } catch (IOException e) {
      responseMap.put("result", "error");
      responseMap.put("error_type", "error_datasource");
      return adapter.toJson(responseMap);
    }
  }
}
