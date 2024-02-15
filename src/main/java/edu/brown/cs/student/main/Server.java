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

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the various
 * handlers (2).
 *
 * <p>Notice that the OrderHandler takes in a state (menu) that can be shared if we extended the
 * restaurant They need to share state (a menu). This would be a great opportunity to use dependency
 * injection. If we needed more endpoints, more functionality classes, etc. we could make sure they
 * all had the same shared state.
 */
public class Server {
  private CSVWrapper state;

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

  public static void main(String[] args) {
    new Server(new CSVWrapper());
  }
}
