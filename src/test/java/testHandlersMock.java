import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.Cache.ACSDataSource;
import edu.brown.cs.student.main.Cache.EvictionPolicy;
import edu.brown.cs.student.main.Cache.MockedACSDataSource;
import edu.brown.cs.student.main.Handler.broadbandHandler;
import edu.brown.cs.student.main.Handler.loadHandler;
import edu.brown.cs.student.main.Handler.searchHandler;
import edu.brown.cs.student.main.Handler.viewHandler;
import edu.brown.cs.student.main.Search.CSVWrapper;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import spark.Spark;

public class testHandlersMock {

  private final JsonAdapter<Map<String,Object>> adapter;
  public testHandlersMock() {
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    this.adapter = moshi.adapter(type);
  }
  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  @BeforeEach
  public void setup() {
    Spark.get("broadband", new broadbandHandler(new MockedACSDataSource()));
    Spark.init();
    Spark.awaitInitialization();
  }

  @AfterEach
  public void teardown() {
    Spark.unmap("broadband");
    Spark.awaitStop();
  }
  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    return clientConnection;
  }





}
