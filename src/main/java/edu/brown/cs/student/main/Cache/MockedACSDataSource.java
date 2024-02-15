package edu.brown.cs.student.main.Cache;

import edu.brown.cs.student.main.CensusAPI.County;
import edu.brown.cs.student.main.CensusAPI.State;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockedACSDataSource implements Datasource {

  public Map getStates() throws IOException {
    State california = new State("California", "06");
    State washington = new State("Washington", "53");
    Map<String, State> stateMap = new HashMap();
    stateMap.put("California", california);
    stateMap.put("Washington", washington);
    return stateMap;
  }

  @Override
  public Map getCountyCache(String stateCode) throws IOException {
    County napa = new County("Napa County, California", "06", "055");
    County sd = new County("San Diego County, California", "06", "073");
    Map<String, County> countyMap = new HashMap();
    countyMap.put("Napa County, California", napa);
    countyMap.put("San Diego County, California", sd);
    return countyMap;
  }

  @Override
  public List<List<String>> getBroadbandData(String stateCode, String countyCode)
      throws IOException {
    List<List<String>> broadBandList = new ArrayList<>();
    List<String> innerList =
        new ArrayList<>(Arrays.asList("San Diego County, California", "92.9", "06", "073"));
    List<String> outerList =
        new ArrayList<>(Arrays.asList("NAME", "S2802_C03_022E", "state", "county"));
    broadBandList.add(outerList);
    broadBandList.add(innerList);
    return broadBandList;
  }
}
