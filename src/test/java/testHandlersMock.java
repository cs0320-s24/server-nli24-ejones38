import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.Cache.MockedACSDataSource;
import edu.brown.cs.student.main.Handler.broadbandHandler;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class testHandlersMock {

  private final JsonAdapter<Map<String, Object>> adapter;

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

  @Test
  public void testSuccess() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=San%20Diego%20County");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        this.adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("success", response.get("result"));
  }

  @Test
  public void testSuccessDetail() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=San%20Diego%20County");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        this.adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    List<List<String>> godsList = (List<List<String>>) response.get("data");
    assertEquals("92.9", godsList.get(1).get(1));
  }

  @Test
  public void testNoState() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("broadband?state=jim&county=San%20Diego%20County");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        this.adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error", response.get("result"));
  }

  @Test
  public void testNoCounty() throws IOException {
    HttpURLConnection clientConnection = tryRequest("broadband?state=California&county=jim");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        this.adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error", response.get("result"));
  }
}
