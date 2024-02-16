package edu.brown.cs.student.main.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for the shared state that the different csv functions have to share. This includes
 * the filepath, fileValidity, and parsed data.
 */
public class CSVWrapper {

  /** Boolean that represents the validity of the file. */
  private Boolean fileValidity;

  /** String that represents the filepath. */
  private String filepath;

  /** List of rows that represent parsed data. */
  private List<List<String>> data;

  /** Constructor for the wrapper class. Upon initialization, the fileValidity is set to false. */
  public CSVWrapper() {
    this.fileValidity = Boolean.FALSE;
  }

  /**
   * Setter for the data when loadcsv finishes parsing.
   *
   * @param data List of data to share.
   */
  public void setData(List data) {
    this.data = data;
  }

  /**
   * Gets the data from the shared state.
   *
   * @return list of rows representing parsed data.
   */
  public List<List<String>> getData() {
    List<List<String>> data = new ArrayList<>(this.data);
    return data;
  }

  /**
   * Method that sets the filevalidity on a certain load.
   *
   * @param validity Boolean that represents the validity of the file.
   */
  public void setFileValidity(Boolean validity) {
    this.fileValidity = validity;
  }

  /**
   * Method that sets the filepath to be loaded, viewed, and searched.
   *
   * @param filepath String of the local filepath.
   */
  public void setPath(String filepath) {
    this.filepath = filepath;
  }

  /**
   * Method that checks the validity of a filepath, usually for viewing/searching after loading.
   *
   * @return Boolean that represents validity of filepath.
   */
  public Boolean checkValidity() {
    Boolean isValid = this.fileValidity;
    return isValid;
  }
}
