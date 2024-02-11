package edu.brown.cs.student.main.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an error provided to catch any error that may occur when you create an object from a row.
 */
public class FactoryFailureException extends Exception {
  /** List of Strings to represent the row */
  final List<String> row;

  /**
   * An exception to catch errors during row conversion.
   *
   * @param message: the message to be printed.
   * @param row: the list of strings from the CSV.
   */
  public FactoryFailureException(String message, List<String> row) {
    super(message);
    this.row = new ArrayList<>(row);
  }
}
