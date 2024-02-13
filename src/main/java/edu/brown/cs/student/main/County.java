package edu.brown.cs.student.main;

public class County {
  private String NAME;
  private int state;
  private int county;

  public County(String NAME, int state, int county) {
    this.NAME = NAME;
    this.state = state;
    this.county = county;
  }

  public String getNAME() {
    return this.NAME;
  }

  public int getCode() {
    return this.county;
  }



}
