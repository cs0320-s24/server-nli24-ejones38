package edu.brown.cs.student.main.Handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CSVWrapper;
import edu.brown.cs.student.main.Parser.CSVParser;
import edu.brown.cs.student.main.Parser.StringCreator;
import edu.brown.cs.student.main.Search.CSVSearch;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class searchHandler implements Route {
  private CSVWrapper state;
  public searchHandler(CSVWrapper state) {
    this.state = state;
  }
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String,Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    if (!this.state.checkValidity()){
      this.state.setFileValidity(Boolean.FALSE);
      responseMap.put("result", "error");
      responseMap.put("error_type", "datasource");
      responseMap.put("details", "No file loaded!");
      return adapter.toJson(responseMap);
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
    CSVSearch searcher = new CSVSearch(this.state.getData(),value);
    String columnName = request.queryParams("columnName");
    String columnIndex = request.queryParams("columnIndex");

    try {
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

    } catch (IndexOutOfBoundsException e) {
      responseMap.put("result", "error");
      responseMap.put("error_type", "error_bad_request");
      responseMap.put("details", "Index/Column does not exist!");
      return adapter.toJson(responseMap);
    }

    responseMap.put("result", "success");
    responseMap.put("data",searcher.getResult());
    return adapter.toJson(responseMap);

  }
}
