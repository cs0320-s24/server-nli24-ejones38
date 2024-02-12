package edu.brown.cs.student.main.Handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CSVWrapper;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class loadHandler implements Route {
  private CSVWrapper state;
  public loadHandler(CSVWrapper state) {
    this.state = state;
  }
  @Override
  public Object handle(Request request, Response response) throws Exception {
    String filePath = request.queryParams("filepath");
    //type http://localhost:3232/loadcsv?filepath=income_by_race.csv
    Map<String,Object> responseMap = new HashMap<>();
    responseMap.put("filepath", filePath);
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    try (FileReader reader = new FileReader("data/" + filePath)) {
      this.state.setFileValidity(Boolean.TRUE);
      this.state.setReader(reader);
      responseMap.put("result", "success");
      return adapter.toJson(responseMap);
    }
    catch(FileNotFoundException e) {
      this.state.setFileValidity(Boolean.FALSE);
      responseMap.put("result", "error");
      responseMap.put("error_type", "datasource");
      responseMap.put("details", "File Not Found!");
      return adapter.toJson(responseMap);
    }


  }
}
