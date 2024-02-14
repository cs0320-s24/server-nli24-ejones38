import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.Search.CSVWrapper;
import edu.brown.cs.student.main.Cache.ACSDataSource;
import edu.brown.cs.student.main.Cache.EvictionPolicy;
import edu.brown.cs.student.main.Handler.broadbandHandler;
import edu.brown.cs.student.main.Handler.loadHandler;
import edu.brown.cs.student.main.Handler.searchHandler;
import edu.brown.cs.student.main.Handler.viewHandler;
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

public class testHandlers {
  private final JsonAdapter<Map<String,Object>> adapter;
  public testHandlers() {
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
    CSVWrapper csvData = new CSVWrapper();
    Spark.get("loadcsv", new loadHandler(csvData));
    Spark.get("viewcsv", new viewHandler(csvData));
    Spark.get("searchcsv", new searchHandler(csvData));
    Spark.get("broadband", new broadbandHandler(new ACSDataSource(EvictionPolicy.NONE)));
    Spark.init();
    Spark.awaitInitialization();
  }

  @AfterEach
  public void teardown() {
    Spark.unmap("loadcsv");
    Spark.unmap("viewcsv");
    Spark.unmap("searchcsv");
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
  public void testNoEndpoint() throws IOException {
    HttpURLConnection clientConnection = tryRequest("jesus");
    assertEquals(404, clientConnection.getResponseCode());
  }

  @Test
  public void testSuccessLoad() throws IOException{
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=census/ri_census.csv");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("success", response.get("result"));
  }

  @Test
  public void testLoadNoPath() throws IOException{
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=jim");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error", response.get("result"));
  }

  @Test
  public void testLoadNoParam() throws IOException{
    HttpURLConnection clientConnection = tryRequest("loadcsv");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error", response.get("result"));
  }

  @Test
  public void testSuccessView() throws IOException{
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=census/ri_census.csv");
    assertEquals(200, clientConnection.getResponseCode());
    HttpURLConnection clientConnection1 = tryRequest("viewcsv");
    assertEquals(200, clientConnection1.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("success", response.get("result"));
  }
  @Test
  public void testViewWithoutLoad() throws IOException{
    HttpURLConnection clientConnection1 = tryRequest("viewcsv");
    assertEquals(200, clientConnection1.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertEquals("error", response.get("result"));
  }
  @Test
  public void testSuccessSearchValue() throws IOException{
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=census/ri_census.csv");
    assertEquals(200, clientConnection.getResponseCode());
    HttpURLConnection clientConnection1 = tryRequest("searchcsv?value=Bristol");
    assertEquals(200, clientConnection1.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertEquals("success", response.get("result"));
  }

  @Test
  public void testNoSearchValue() throws IOException{
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=census/ri_census.csv");
    assertEquals(200, clientConnection.getResponseCode());
    HttpURLConnection clientConnection1 = tryRequest("searchcsv");
    assertEquals(200, clientConnection1.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertEquals("error", response.get("result"));
  }
  @Test
  public void testBadValue() throws IOException{
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=census/ri_census.csv");
    assertEquals(200, clientConnection.getResponseCode());
    HttpURLConnection clientConnection1 = tryRequest("searchcsv?value=oops");
    assertEquals(200, clientConnection1.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertEquals("success", response.get("result"));
  }
  @Test
  public void testColNameSuccess() throws IOException{
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=census/ri_census.csv");
    assertEquals(200, clientConnection.getResponseCode());
    HttpURLConnection clientConnection1 = tryRequest("searchcsv?value=Providence&columnName=City/Town");
    assertEquals(200, clientConnection1.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertEquals("success", response.get("result"));
    assertEquals("City/Town", response.get("columnName"));
  }
  @Test
  public void testColIndexSuccess() throws IOException{
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=census/ri_census.csv");
    assertEquals(200, clientConnection.getResponseCode());
    HttpURLConnection clientConnection1 = tryRequest("searchcsv?value=Providence&columnIndex=0");
    assertEquals(200, clientConnection1.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertEquals("success", response.get("result"));
    assertEquals("0", response.get("columnIndex"));
  }
  @Test
  public void testColIndexOutofBounds() throws IOException{
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=census/ri_census.csv");
    assertEquals(200, clientConnection.getResponseCode());
    HttpURLConnection clientConnection1 = tryRequest("searchcsv?value=Providence&columnIndex=-1");
    assertEquals(200, clientConnection1.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertEquals("error", response.get("result"));
  }

  @Test
  public void testColNameNoExist() throws IOException{
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=census/ri_census.csv");
    assertEquals(200, clientConnection.getResponseCode());
    HttpURLConnection clientConnection1 = tryRequest("searchcsv?value=Providence&columnName=jim");
    assertEquals(200, clientConnection1.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertEquals("error", response.get("result"));
  }


  @Test
  public void testSuccess() throws IOException {
    HttpURLConnection clientConnection = tryRequest("broadband?state=Washington&county=King%20County");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("success", response.get("result"));
  }

  @Test
  public void testSuccessDetail() throws IOException {
    HttpURLConnection clientConnection = tryRequest("broadband?state=Washington&county=King%20County");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    List<List<String>> godsList = (List<List<String>>) response.get("data");
    assertEquals("93.3", godsList.get(1).get(1));
  }
  @Test
  public void testNoState() throws IOException {
    HttpURLConnection clientConnection = tryRequest("broadband?state=jim&county=King%20County");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error", response.get("result"));
  }
  @Test
  public void testNoCounty() throws IOException {
    HttpURLConnection clientConnection = tryRequest("broadband?state=Washington&county=VladniraDepena");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error", response.get("result"));
  }

}
