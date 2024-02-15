package edu.brown.cs.student.main.CensusAPI;

/**
 * Wrapper Class that represents a state and contains the name and state code of the state.
 */
public class State {

  /**
   * String parameter that represents the name of the state
   */
  private String NAME;

  /**
   * String parameter that represents the state code of the state
   */
  private String state;

  /**
   * Constructor that initializes the instance variables for the state object.
   * @param NAME String parameter of state's name
   * @param state String parameter of state code
   */
  public State(String NAME, String state) {
    this.NAME = NAME;
    this.state = state;
  }

  /**
   * Getter method for the state code
   * @return String that represents the state code.
   */
  public String getCode() {
    return this.state;
  }
}
