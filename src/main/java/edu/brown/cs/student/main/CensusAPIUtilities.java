package edu.brown.cs.student.main;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CensusAPIUtilities {
  private CensusAPIUtilities() {

  }
  public static List<State> deserializeStateCodes(String jsonList) throws IOException {
    List<State> stateList = new ArrayList<>();
    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, List.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);
    List<List<String>> statesData = adapter.fromJson(jsonList);
    for (int i = 1; i < statesData.size(); i++) {
      List<String> row = statesData.get(i);
      State state = new State(row.get(0), row.get(1));
      stateList.add(state);
    }
    return stateList;
  }

  public static List<County> deserializeCountyCodes(String jsonList) throws IOException {
    List<County> countyList = new ArrayList<>();
    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, List.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);
    List<List<String>> countyData = adapter.fromJson(jsonList);
    for (int i = 1; i < countyData.size(); i++) {
      List<String> row = countyData.get(i);
      County county = new County(row.get(0), row.get(1), row.get(2));
      countyList.add(county);
    }
    return countyList;
  }

  public static List<List<String>> deserializeBroadband(String jsonList) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, List.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);
    return adapter.fromJson(jsonList);
  }

}
