package edu.brown.cs.student.main.Search;

import edu.brown.cs.student.main.Parser.CSVParser;
import java.util.ArrayList;
import java.util.List;

/**
 * Search class that implements the searching functionality, with different methods unique to
 * different cases of user input. It searches based on the values it gets from the CSVParser class.
 */
public class CSVSearch {

  /** CSVParser that parses rows into a row of strings. */
  CSVParser<List<String>> parser;

  /** The value to be searched for. */
  String searchValue;

  /** Boolean to track whether there are any matches for the searched value. */
  Boolean matched = Boolean.FALSE;

  /** List of rows containing all the searched values. This is the ultimate output of search. */
  List<List<String>> result;

  /**
   * Constructor for Search that takes assigns instance variables to associated parameters and
   * initializes a new arrayList to hold final output.
   *
   * @param parser: the parser that holds data to be searched.
   * @param searchValue: the String to be searched.
   */
  public CSVSearch(CSVParser<List<String>> parser, String searchValue) {
    this.parser = parser;
    this.searchValue = searchValue;
    this.result = new ArrayList<>();
  }

  /** Search method given only 2 arguments of user input: FileName and SearchValue. */
  public void search() {
    List<List<String>> file = this.parser.getFinalList();
    for (int i = 0; i < file.size(); i++) {
      if (file.get(i)
          .contains(
              this.searchValue)) { // no need to implement equals because don't need to iterate
        // through each row
        System.out.println(file.get(i));
        this.result.add(file.get(i));
        this.matched = Boolean.TRUE;
      }
    }
    if (this.matched == Boolean.FALSE) {
      System.out.println("No Matches Found! Sorry!");
    }
  }

  /**
   * Method to search by index, and specifying whether the CSV has headers.
   *
   * @param columnIndex: the column index to search under.
   * @param hasHeader: Boolean representing whether the column has headers.
   */
  public void search(int columnIndex, Boolean hasHeader) {
    List<List<String>> file = this.parser.getFinalList();
    if (hasHeader) { // yes Header
      for (int i = 1; i < file.size(); i++) { // skip Header row
        if (file.get(i).get(columnIndex).equals(this.searchValue)) {
          System.out.println(file.get(i));
          this.result.add(file.get(i));
          this.matched = Boolean.TRUE;
        }
      }
    } else {
      for (int i = 0; i < file.size(); i++) {
        if (file.get(i).get(columnIndex).equals(this.searchValue)) {
          System.out.println(file.get(i));
          this.result.add(file.get(i));
          this.matched = Boolean.TRUE;
        }
      }
    }
    if (this.matched == Boolean.FALSE) {
      System.out.println("No Matches Found! Sorry!");
    }
  }

  /**
   * Method to search by name, this always assumes headers are present because there should be no
   * case where you can say the name of a column, without having columns...
   *
   * @param columnName: name of Column that the searchValue is under.
   */
  public void search(String columnName) {
    int columnIndex = 0;
    List<List<String>> file = this.parser.getFinalList();
    for (int i = 0; i < file.get(0).size(); i++) {
      if (file.get(0).get(i).equals(columnName)) {
        columnIndex = i; // matching the right column
      }
    }
    for (int i = 1; i < file.size(); i++) { // skip header row
      if (file.get(i).get(columnIndex).equals(this.searchValue)) {
        System.out.println(file.get(i));
        this.result.add(file.get(i));
        this.matched = Boolean.TRUE;
      }
    }
    if (this.matched == Boolean.FALSE) {
      System.out.println("No Matches Found! Sorry!");
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

  /**
   * Method for testing edge case of whether the value exists in the CSV.
   *
   * @return: Boolean representing the existence of SearchValue in CSV.
   */
  public Boolean getMatched() {
    return this.matched;
  }
}
