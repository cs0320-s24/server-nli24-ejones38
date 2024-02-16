package edu.brown.cs.student.main;

import static spark.Spark.after;

import edu.brown.cs.student.main.Cache.ACSDataSource;
import edu.brown.cs.student.main.Cache.EvictionPolicy;
import edu.brown.cs.student.main.Handler.broadbandHandler;
import edu.brown.cs.student.main.Handler.loadHandler;
import edu.brown.cs.student.main.Handler.searchHandler;
import edu.brown.cs.student.main.Handler.viewHandler;
import edu.brown.cs.student.main.Search.CSVWrapper;
import spark.Spark;

/** Contains the main() method which starts Spark and runs the various handlers (2). */
public class Server {

  /** the instance variable representing the shared csv values/data that multiple handlers use */
  private CSVWrapper state;

  /**
   * Constructor for the server class. Instantiates the passed in CSVWrapper initializes the server
   * and prepares the handlers and their needed data.
   *
   * @param csv - the wrapper class that holds CSV file data
   */
  public Server(CSVWrapper csv) {
    this.state = csv;
    int port = 3232;
    Spark.port(port);
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });
    Spark.get("loadcsv", new loadHandler(this.state));
    Spark.get("viewcsv", new viewHandler(this.state));
    Spark.get("searchcsv", new searchHandler(this.state));
    ACSDataSource cache = new ACSDataSource(100000000, EvictionPolicy.SIZE);
    Spark.get("broadband", new broadbandHandler(cache));
    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started at http://localhost:" + port);
  }

  /**
   * The main method that runs the server
   *
   * @param args - command line arguments
   */
  public static void main(String[] args) {
    new Server(new CSVWrapper());
  }
}
