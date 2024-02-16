package edu.brown.cs.student.main.CensusAPI;

/**
 * This class describes the county objects that are wrapper classes for the string information
 * contained about a county in a certain state.
 */
public class County {

  /** String parameter that represents the county code */
  private String county;

  /**
   * The constructor of the county that holds 3 parameters: the name of the county, name of the
   * state and the countyCode, all represented as strings.
   *
   * @param NAME String parameter that holds the county name ("San Diego County, California")
   * @param state String parameter that holds the state Code ("06")
   * @param county String parameter that holds the county Code ("073")
   */
  public County(String NAME, String state, String county) {
    this.county = county;
  }

  /**
   * Method that gets the countyCode associated with a certain County.
   *
   * @return String representing the county code.
   */
  public String getCode() {
    return this.county;
  }
}
