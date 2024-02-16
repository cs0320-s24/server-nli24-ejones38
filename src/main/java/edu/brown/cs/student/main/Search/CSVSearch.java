package edu.brown.cs.student.main.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Search class that implements the searching functionality, with different methods unique to
 * different cases of user input. It searches based on the values it gets from the CSVParser class.
 */
public class CSVSearch {

  /** The value to be searched for. */
  String searchValue;

  /** List of rows containing all the searched values. This is the ultimate output of search. */
  List<List<String>> result;

  /** List of lists/rows that contains the data to be searched through */
  List<List<String>> data;

  /**
   * Constructor for Search that assigns instance variables to associated parameters and initializes
   * a new arrayList to hold final output.
   *
   * @param data: A list of lists/rows that contains the data to be searched through.
   * @param searchValue: the String to be searched.
   */
  public CSVSearch(List<List<String>> data, String searchValue) {
    this.data = data;
    this.searchValue = searchValue;
    this.result = new ArrayList<>();
  }

  /** Search method given only 2 arguments of user input: FileName and SearchValue. */
  public void search() {
    List<List<String>> file = this.data;
    for (int i = 0; i < file.size(); i++) {
      if (file.get(i)
          .contains(
              this.searchValue)) { // no need to implement equals because don't need to iterate
        // through each row
        this.result.add(file.get(i));
      }
    }
  }

  /**
   * Method to search by index.
   *
   * @param columnIndex: the column index to search under.
   */
  public void search(int columnIndex) throws IndexOutOfBoundsException {
    List<List<String>> file = this.data;
    if (columnIndex < 0 || columnIndex >= file.size()) {
      throw new IndexOutOfBoundsException();
    }
    for (int i = 0; i < file.size(); i++) {
      if (file.get(i).get(columnIndex).equals(this.searchValue)) {
        this.result.add(file.get(i));
      }
    }
  }

  /**
   * Method to search by name.
   *
   * @param columnName: name of Column that the searchValue is under.
   */
  public void search(String columnName) throws IndexOutOfBoundsException {
    int columnIndex = -1;
    List<List<String>> file = this.data;
    for (int i = 0; i < file.get(0).size(); i++) {
      if (file.get(0).get(i).equals(columnName)) {
        columnIndex = i; // matching the right column
      }
    }
    if (columnIndex == -1) {
      throw new IndexOutOfBoundsException();
    }
    for (int i = 1; i < file.size(); i++) { // skip header row
      if (file.get(i).get(columnIndex).equals(this.searchValue)) {
        this.result.add(file.get(i));
      }
    }
  }

  /**
   * Getter method for the resulting output with all rows.
   *
   * @return: A list of rows that contain the search value.
   */
  public List<List<String>> getResult() {
    return this.result;
  }
}
