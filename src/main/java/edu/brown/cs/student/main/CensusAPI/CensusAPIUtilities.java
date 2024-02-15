package edu.brown.cs.student.main.CensusAPI;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;

/**
 * Class that is responsible for sending requests to external APIs and deserializing the response received
 * from those APIs.
 */
public class CensusAPIUtilities {
  private CensusAPIUtilities() {

  }

  /**
   * Method that sends a request for state codes, deserializes the response and stores them in a stateMap.
   * @return Map of State Names to State Codes, both represented by strings.
   * @throws IOException generic Java exception if the request to the API failed.
   */
  public static Map<String, State> deserializeStateCodes() throws IOException {
    URL requestURL = new URL("https", "api.census.gov", "/data/2010/dec/sf1?get=NAME&for=state:*");
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();//begins connection

    Map<String, State> stateMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, List.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);

    List<List<String>> statesData =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));//reads Jsondata to a List
    clientConnection.disconnect();//ends connection

    for (int i = 1; i < statesData.size(); i++) {//logic to store data in map
      List<String> row = statesData.get(i);
      State state = new State(row.get(0), row.get(1));
      stateMap.put(row.get(0), state);
    }
    return stateMap;
  }

  /**
   * Method that sends a request to the CensusAPI, deserializes the response, and stores it in a Map.
   * @param stateCode String that represents the state code
   * @return Map of county names to county objects, only in a certain state.
   * @throws IOException generic java exception for API request failure.
   */

  public static Map<String, County> deserializeCountyCodes(String stateCode) throws IOException {
    URL requestURL =
        new URL(
            "https",
            "api.census.gov",
            "/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();

    Map<String, County> countyMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, List.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);
    List<List<String>> countyData = adapter.fromJson
        (new Buffer().readFrom(clientConnection.getInputStream()));//read in list of counties
    clientConnection.disconnect();
    for (int i = 1; i < countyData.size(); i++) {//logic to store information in county objects
      List<String> row = countyData.get(i);
      County county = new County(row.get(0), row.get(1), row.get(2));
      countyMap.put(row.get(0), county);
    }
    return countyMap;
  }

  /**
   * Method that sends a request for broadband information given a state code and county code, deserializes it,
   * and returns the output.
   * @param stateCode String parameter that represents the state code to search within.
   * @param countyCode String parameter that represents the county to search for.
   * @return A list of strings containing the broadband information.
   * @throws IOException generic java exception for request failures.
   */
  public static List<List<String>> deserializeBroadband(String stateCode, String countyCode)
      throws IOException {
    URL requestURL =
        new URL(
            "https",
            "api.census.gov",
            "/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                + countyCode
                + "&in=state:"
                + stateCode);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, List.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);
    return adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
  }
}
