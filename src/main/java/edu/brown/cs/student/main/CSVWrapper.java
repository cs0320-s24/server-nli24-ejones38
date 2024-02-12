package edu.brown.cs.student.main;

import java.io.FileReader;
import java.io.Reader;

public class CSVWrapper {
  private Boolean fileValidity;
  private String filepath;
  public CSVWrapper() {
    this.fileValidity = Boolean.FALSE;
  }
  public void setFileValidity(Boolean validity) {
    this.fileValidity = validity;
  }

  public void setPath(String filepath) {
    this.filepath = filepath;
  }
  public String getPath() {
    String filepath = this.filepath;
    return filepath;
  }

  public Boolean checkValidity() {
    Boolean isValid = this.fileValidity;
    return isValid;
  }

}
