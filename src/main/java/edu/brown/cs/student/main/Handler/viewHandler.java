package edu.brown.cs.student.main.Handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CSVWrapper;
import edu.brown.cs.student.main.Parser.CSVParser;
import edu.brown.cs.student.main.Parser.StringCreator;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class viewHandler implements Route {
  private CSVWrapper state;

  public viewHandler(CSVWrapper state) {
    this.state = state;
  }

  @Override
  public Object handle(Request request, Response response) {
    Map<String,Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    if (!this.state.checkValidity()){
      Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
      JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
      this.state.setFileValidity(Boolean.FALSE);
      responseMap.put("result", "error");
      responseMap.put("error_type", "datasource");
      responseMap.put("details", "No File Loaded!");
      return adapter.toJson(responseMap);
    }
    responseMap.put("data", this.state.getData());
    responseMap.put("result", "success");
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    return adapter.toJson(responseMap);

  }


}
