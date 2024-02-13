package edu.brown.cs.student.main;

public class County {
  private String NAME;
  private String state;
  private String county;

  public County(String NAME, String state, String county) {
    this.NAME = NAME;
    this.state = state;
    this.county = county;
  }

  public String getNAME() {
    return this.NAME;
  }

  public String getCode() {
    return this.county;
  }



}
