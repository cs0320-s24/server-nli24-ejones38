package edu.brown.cs.student.main.Handler;

import spark.Request;
import spark.Response;
import spark.Route;

public class loadHandler implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String filepath = request.queryParams("filepath");

    return null;
  }
}
