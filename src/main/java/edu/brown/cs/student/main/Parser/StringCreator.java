package edu.brown.cs.student.main.Parser;

import java.util.List;

/**
 * The StringCreator Class is a class that implements the CreatorFromRow Interface but converts the
 * row fromCSV Parser into a list of strings.
 */
public class StringCreator implements CreatorFromRow<List<String>> {

  /** Constructor for StringCreator */
  public StringCreator() {}

  /**
   * Method to convert a row from the CSV into a list of strings
   *
   * @param row: a list of strings from the CSV.
   * @return: a list of strings that represents that row.
   * @throws FactoryFailureException: generic exception catching
   */
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
