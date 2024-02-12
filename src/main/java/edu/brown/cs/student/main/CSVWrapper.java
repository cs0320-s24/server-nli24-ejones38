package edu.brown.cs.student.main;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVWrapper {
  private Boolean fileValidity;
  private String filepath;

  private List<List<String>> data;
  public CSVWrapper() {
    this.fileValidity = Boolean.FALSE;
  }

  public void setData(List data) {
    this.data = data;
  }
  public List<List<String>> getData() {
    List<List<String>> data = new ArrayList<>(this.data);
    return data;
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
