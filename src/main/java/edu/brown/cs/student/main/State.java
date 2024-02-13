package edu.brown.cs.student.main;

public class State {
  private String NAME;
  private String state;

  public State (String NAME, String state) {
    this.NAME = NAME;
    this.state = state;
  }
  public String getName() {
    return this.NAME;
  }
  public String getCode() {
    return this.state;
  }

}
