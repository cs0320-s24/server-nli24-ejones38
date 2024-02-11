package edu.brown.cs.student.main.Parser;

import java.util.List;

/**
 * This interface defines a method that allows the parser to convert each row into an object of some
 * arbitrary passed type. For developers interested in using the parser, the parser should take in a
 * class that implements this interface.
 */
public interface CreatorFromRow<T> {

  /**
   * Generic create method to turn a List of Strings into an object of generic type.
   *
   * @param row: list of strings from the CSV
   * @return: the generic object
   * @throws FactoryFailureException: captures general errors
   */
  T create(List<String> row) throws FactoryFailureException;
}
