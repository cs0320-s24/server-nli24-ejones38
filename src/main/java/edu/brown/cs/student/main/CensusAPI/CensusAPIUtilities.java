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

public class CensusAPIUtilities {
  private CensusAPIUtilities() {

  }

  public static Map<String, State> deserializeStateCodes() throws IOException {
    URL requestURL = new URL("https","api.census.gov","/data/2010/dec/sf1?get=NAME&for=state:*");
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();

    Map<String, State> stateMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, List.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);

    List<List<String>> statesData = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    clientConnection.disconnect();
    for (int i = 1; i < statesData.size(); i++) {
      List<String> row = statesData.get(i);
      State state = new State(row.get(0), row.get(1));
      stateMap.put(row.get(0), state);
    }
    return stateMap;
  }

  public static Map<String, County> deserializeCountyCodes(String stateCode) throws IOException {
    URL requestURL = new URL("https","api.census.gov","/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();

    Map<String, County> countyMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, List.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);
    BufferedSource source = Okio.buffer(Okio.source(clientConnection.getInputStream()));
    List<List<String>> countyData = adapter.fromJson(source);
    clientConnection.disconnect();
    for (int i = 1; i < countyData.size(); i++) {
      List<String> row = countyData.get(i);
      County county = new County(row.get(0), row.get(1), row.get(2));
      countyMap.put(row.get(0), county);
    }
    return countyMap;
  }

  public static List<List<String>> deserializeBroadband(String stateCode, String countyCode) throws IOException {
    URL requestURL = new URL("https","api.census.gov","/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
        + countyCode + "&in=state:" + stateCode);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, List.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);
    BufferedSource source = Okio.buffer(Okio.source(clientConnection.getInputStream()));
    return adapter.fromJson(source);
  }

}
