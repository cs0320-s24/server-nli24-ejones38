package edu.brown.cs.student.main.Handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class loadHandler implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String filePath = request.queryParams("filepath");
    Map<String,Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    try (FileReader reader = new FileReader("/Users/nick/Desktop/CS32/Sprints/server-nli24-ejones38/data/census/" + filePath)) {
      responseMap.put("filepath", filePath);
      responseMap.put("result", "success");
      return adapter.toJson(responseMap);
    }
    catch(FileNotFoundException e) {
      responseMap.put("filepath", filePath);
      responseMap.put("result", "error");
      responseMap.put("error_type", "datasource");
      responseMap.put("details", e.getMessage()); //dk if e.getMessage is correct
      return adapter.toJson(responseMap);
    }


  }
}
