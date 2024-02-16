package edu.brown.cs.student.main.Cache;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a datasource interface, allows for multiple implementations of datasource
 */
public interface Datasource {

  /**
   * Method that gets the map of states
   *
   * @return a map of state names as keys and state objects as values.
   * @throws IOException generic java exception to cover CensusAPIUtilities sending a request
   */
  Map getStates() throws IOException;

  /**
   * Method that gets a map of counties in a given state.
   *
   * @param stateCode String state code to associate all counties with.
   * @return a map with county names as keys and county objects as values
   * @throws IOException generic java exception to cover CensusAPIUtilities sending a request.
   */
  Map getCountyCache(String stateCode) throws IOException;

  /**
   * Method that gets the resultant data containing broadband information.
   *
   * @param stateCode String state code to search for
   * @param countyCode String county code to search for
   * @return a list of rows containing the headers and broadband information
   * @throws IOException generic java exception to cover CensusAPIUtilities requests.
   */
  List<List<String>> getBroadbandData(String stateCode, String countyCode) throws IOException;
}
