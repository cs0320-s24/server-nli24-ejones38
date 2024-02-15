package edu.brown.cs.student.main.Cache;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Datasource {

  Map getStates() throws IOException;

  Map getCountyCache(String stateCode) throws IOException;

  List<List<String>> getBroadbandData(String stateCode, String countyCode) throws IOException;
}
