package edu.brown.cs.student.main.Handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.Search.CSVSearch;
import edu.brown.cs.student.main.Search.CSVWrapper;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** Class responsible for handling requests to search a CSV */
public class searchHandler implements Route {

  /** the instance variable representing the shared csv values/data that multiple handlers use */
  private CSVWrapper state;

  /**
   * The constructor of searchHandler. Takes in a CSVWrapper which contains useful data about the
   * contents of a CSV file that will be searched
   *
   * @param state - - the wrapper class that holds CSV file data
   */
  public searchHandler(CSVWrapper state) {
    this.state = state;
  }

  /**
   * Method handles incoming viewcsv requests. Checks and responds if a csv has not been loaded or
   * if the proper params were not included in the search request. Else searches the loaded CSV file
   * for the specified value (optionally at the specified header/index). Converts the search data
   * into a JSON file and outputs it along with the appropriate server response. Method also checks
   * to make sure index requests are valid else returns an error JSON
   *
   * @param request - the incoming user request
   * @param response - the outgoing response back to the user
   * @return - a JSON string representing the rows of the CSV file with the specified value along
   *     with appropriate server response
   */
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);

    if (!this.state.checkValidity()) {
      return this.validityHelper(adapter, responseMap);
    }

    String value = request.queryParams("value");
    if (value != null) {
      responseMap.put("value", value);
    } else {
      responseMap.put("result", "error");
      responseMap.put("error_type", "error_bad_request");
      responseMap.put("details", "No value inputted!");
      return adapter.toJson(responseMap);
    }
    CSVSearch searcher = new CSVSearch(this.state.getData(), value);
    String columnName = request.queryParams("columnName");
    String columnIndex = request.queryParams("columnIndex");

    try {
      //List<List<String>> responseData =
      this.searchHelper(searcher, columnName, columnIndex, responseMap);

    } catch (IndexOutOfBoundsException e) {
      responseMap.put("result", "error");
      responseMap.put("error_type", "error_bad_request");
      responseMap.put("details", "Index/Column does not exist!");
      return adapter.toJson(responseMap);
    }

    responseMap.put("result", "success");
    responseMap.put("data", searcher.getResult());
    return adapter.toJson(responseMap);
  }

  private void searchHelper(CSVSearch searcher, String columnName,
                                          String columnIndex, Map<String, Object> responseMap) {
    if (columnName != null) {
      responseMap.put("columnName", columnName);
      searcher.search(columnName);
    } else if (columnIndex != null) {
      responseMap.put("columnIndex", columnIndex);
      int index = Integer.parseInt(columnIndex);
      searcher.search(index);
    } else {
      searcher.search();
    }
  }

    private String validityHelper(JsonAdapter<Map<String, Object>> adapter, Map responseMap){
        this.state.setFileValidity(Boolean.FALSE);
        responseMap.put("result", "error");
        responseMap.put("error_type", "datasource");
        responseMap.put("details", "No file loaded!");
        return adapter.toJson(responseMap);
    }


  }
