package edu.brown.cs.student.main.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * CSV Parser class that handles generic parsing and postprocessing of a CSV. Includes additional
 * methods for testing and user functionality, but can be easily edited/deleted for developers'
 * purpose.
 *
 * @param <T>: generic Type to convert into.
 */
public class CSVParser<T> implements AutoCloseable {

  /** Instance variable for the reader - typically either File or String */
  Reader reader;

  /** Instance variable for the CreatorFromRow Class that implements the interface. */
  CreatorFromRow<T> creator;
  /** Instance variable for the finalList of parsed output. */
  List<T> finalList;

  /** Example regex to split strings in a CSV row into individual strings, separated by commas. */
  static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  /**
   * Parser Constructor that assigns instance variables to associated parameters.
   *
   * @param reader: the given reader from user input.
   * @param creator: the creator to turn rows into a specific object type.
   */
  public CSVParser(Reader reader, CreatorFromRow<T> creator) {
    this.creator = creator;
    this.reader = reader;
  }

  /**
   * Method to remove quotes and trim out spaces from strings.
   *
   * @param arg: the string to be processed.
   * @return: the processed string.
   */
  public static String postprocess(String arg) {
    return arg
        // Remove a beginning quote, if present
        .replaceAll("^\"", "")
        // Remove an ending quote, if present
        .replaceAll("\"$", "")
        // Replace double-double-quotes with double-quotes
        .replaceAll("\"\"", "\"")
        .trim();
  }

  /**
   * Method to parse through a CSV, adding all results to a finalList that represents the parsed
   * output.
   *
   * @throws FactoryFailureException: exception from create method.
   * @throws IOException: Java exception for general errors like FileNotFound.
   */
  public void parse() throws FactoryFailureException, IOException {
    try (BufferedReader buffReader =
        new BufferedReader(this.reader)) { // wrapping reader in Bufferedreader
      String line;
      List<T> totalList = new ArrayList<>();
      while ((line = buffReader.readLine()) != null) {
        String[] resultArray = regexSplitCSVRow.split(line);
        for (int i = 0; i < resultArray.length; i++) {
          resultArray[i] = postprocess(resultArray[i]);
        }
        List<String> resultList = List.of(resultArray);
        totalList.add(this.creator.create(resultList));
      }
      this.finalList = totalList;
    }
  }

  /**
   * Getter method to get the parsed values to pass to search class.
   *
   * @return: a list of generic type holding the parsed output.
   */
  public List<T> getFinalList() {
    return this.finalList;
  }

  /** Method to enable CSVParser to be AutoCloseable, can fit within try blocks. */
  public void close() {}
}
