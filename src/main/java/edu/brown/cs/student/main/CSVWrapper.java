package edu.brown.cs.student.main;

import java.io.Reader;

public class CSVWrapper {
  private Boolean fileValidity;
  private Reader reader;
  public CSVWrapper() {

  }
  public void setFileValidity(Boolean validity) {
    this.fileValidity = validity;
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }

}
