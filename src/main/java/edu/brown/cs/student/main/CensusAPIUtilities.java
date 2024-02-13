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
    List<State> stateList;
    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, State.class);
    JsonAdapter<List<State>> adapter = moshi.adapter(listType);
    stateList = adapter.fromJson(jsonList);
    return stateList;
  }

  public static List<County> deserializeCountyCodes(String jsonList) throws IOException {
    List<County> countyList;
    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, County.class);
    JsonAdapter<List<County>> adapter = moshi.adapter(listType);
    countyList = adapter.fromJson(jsonList);
    return countyList;
  }

}
