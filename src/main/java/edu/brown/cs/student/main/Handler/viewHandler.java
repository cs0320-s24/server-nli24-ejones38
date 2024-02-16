package edu.brown.cs.student.main.Handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.Search.CSVWrapper;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** Class responsible for handling requests to view a CSV */
public class viewHandler implements Route {

  /** the instance variable representing the shared csv values/data that multiple handlers use */
  private CSVWrapper state;

  /**
   * The constructor of viewHandler. Takes in a CSVWrapper which contains useful data about a CSV
   * file to be viewed.
   *
   * @param state - the wrapper class that holds CSV file data
   */
  public viewHandler(CSVWrapper state) {
    this.state = state;
  }

  /**
   * Method handles incoming viewcsv requests. Checks and responds if a csv has not been loaded.
   * Else converts previously parsed CSV file into a JSON File and outputs it along with the
   * appropriate server response.
   *
   * @param request - the incoming user request
   * @param response - the outgoing response back to the user
   * @return - a JSON string representing the contents of the CSV file along with the appropriate
   *     server response
   */
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    if (!this.state.checkValidity()) {
      Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
      JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
      return this.validityHelper(adapter, responseMap);
    }
    responseMap.put("data", this.state.getData());
    responseMap.put("result", "success");
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    return adapter.toJson(responseMap);
  }

  /**
   * Method used when file has not been loaded. Outputs error response
   *
   * @param adapter - the adapter that turns the map into a JSON file
   * @param responseMap - the map of outputs to be output
   * @return - the JSON file representing error output
   */
  private String validityHelper(JsonAdapter<Map<String, Object>> adapter, Map responseMap) {
    this.state.setFileValidity(Boolean.FALSE);
    responseMap.put("result", "error");
    responseMap.put("error_type", "datasource");
    responseMap.put("details", "No file loaded!");
    return adapter.toJson(responseMap);
  }
}
