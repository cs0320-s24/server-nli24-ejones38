package edu.brown.cs.student.main.Cache;

import edu.brown.cs.student.main.CensusAPI.County;
import edu.brown.cs.student.main.CensusAPI.State;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents mocked data, primarily for testing use.
 */
public class MockedACSDataSource implements Datasource {

  /**
   * Method that sets and gets the map of states based on developer input. Here is where developers
   * should choose what they want their mocked data to look like.
   * @return the Map of state names as keys pointing to state objects as values.
   */
  public Map getStates() {
    State california = new State("California", "06");
    State washington = new State("Washington", "53");
    Map<String, State> stateMap = new HashMap();
    stateMap.put("California", california);
    stateMap.put("Washington", washington);
    return stateMap;
  }

  /**
   * Method that sets and gets the counties for the particular state. Mocked data needs to be filled
   * in here as well.
   * @param stateCode String state code to associate all counties with.
   * @return a map of county names as keys and county objects as values.
   */
  public Map getCountyCache(String stateCode) {
    County napa = new County("Napa County, California", "06", "055");
    County sd = new County("San Diego County, California", "06", "073");
    Map<String, County> countyMap = new HashMap();
    countyMap.put("Napa County, California", napa);
    countyMap.put("San Diego County, California", sd);
    return countyMap;
  }


  /**
   * Method that sets and gets the broadband data, essentially creating the mocked output.
   * @param stateCode String state code to search for
   * @param countyCode String county code to search for
   * @return the List of rows including the header and broadband data.
   */
  public List<List<String>> getBroadbandData(String stateCode, String countyCode) {
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
