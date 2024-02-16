package edu.brown.cs.student.main.Cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.brown.cs.student.main.CensusAPI.CensusAPIUtilities;
import edu.brown.cs.student.main.CensusAPI.County;
import edu.brown.cs.student.main.CensusAPI.State;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents the Census Data Source, but connects to the CensusAPI and retrieves the
 * data.
 */
public class ACSDataSource implements Datasource {

  /**
   * Instance variable that represents the cache. The cache stores stateMap, countyMaps, and
   * broadbandMaps.
   */
  private Cache<String, Map> cache;

  /**
   * Instance variable that represents the stateMap. This should only be filled once assuming
   * searches are consistent over time.
   */
  private Map<String, State> stateMap;

  /**
   * Constructor for the ACSDataSource that contains the eviction policies.
   *
   * @param limit the numerical input for size and time
   * @param policy the eviction policy specified
   */
  public ACSDataSource(long limit, EvictionPolicy policy) {
    this.stateMap = new HashMap<>();
    switch (policy) {
      case SIZE:
        CacheBuilder<Object, Object> sizeBuilder = CacheBuilder.newBuilder().maximumSize(limit);
        this.cache = sizeBuilder.build();
        break;
      case TIME:
        CacheBuilder<Object, Object> timeBuilder =
            CacheBuilder.newBuilder().expireAfterAccess(Duration.ofMinutes(limit)); //
        this.cache = timeBuilder.build();
        break;
      case REFERENCE:
        CacheBuilder<Object, Object> referenceBuilder =
            CacheBuilder.newBuilder().weakKeys().softValues().maximumSize(limit);
        this.cache = referenceBuilder.build();
        break;
    }
  }

  /** Constructor for caches where there is no eviction policy. */
  public ACSDataSource() {
    this.stateMap = new HashMap<>();

    CacheBuilder<Object, Object> noneBuilder = CacheBuilder.newBuilder();
    this.cache = noneBuilder.build();
  }

  /**
   * Method that gets the map of states if it exists, and fills it if it doesn't.
   *
   * @return the map of states.
   * @throws IOException generic exception for sending requests to censusAPI.
   */
  @Override
  public Map<String, State> getStates() throws IOException {
    if (this.stateMap.isEmpty()) {
      this.stateMap = CensusAPIUtilities.deserializeStateCodes();
    }
    return this.stateMap;
  }

  /**
   * Method that gets a map of county if it exists, and puts it in the cache if it doesn't already
   * exist in the cache. The cache stores this using the state code as a key, and the county map for
   * that given state as the value.
   *
   * @param stateCode String state code to associate all counties with.
   * @return a map of county names as keys and county objects as values for the given state code.
   * @throws IOException generic java exception for requests to censusAPi.
   */
  @Override
  public Map getCountyCache(String stateCode) throws IOException {
    Map<String, County> countyMap;
    if (this.cache.getIfPresent(stateCode) == null) {
      countyMap = CensusAPIUtilities.deserializeCountyCodes(stateCode);
      this.cache.put(stateCode, countyMap);
      return this.cache.asMap().get(stateCode);
    }
    return this.cache.asMap().get(stateCode);
  }

  /**
   * Method that gets the broadBand data from the cache if it exists or puts it there and returns it
   * if it doesn't.
   *
   * @param stateCode String state code to search for
   * @param countyCode String county code to search for
   * @return a list of rows containing the headers and the broadband information.
   * @throws IOException generic java exception for requests to censusAPI.
   */
  public List<List<String>> getBroadbandData(String stateCode, String countyCode)
      throws IOException {
    List<List<String>> broadBandData;
    Map<String, List<List<String>>> broadBandMap = new HashMap<>();
    if (this.cache.getIfPresent(stateCode + countyCode) == null) {
      broadBandData = CensusAPIUtilities.deserializeBroadband(stateCode, countyCode);
      broadBandMap.put(stateCode + countyCode, broadBandData);
      this.cache.put(stateCode + countyCode, broadBandMap);
      return broadBandData;
    }
    broadBandMap = this.cache.asMap().get(stateCode + countyCode);
    broadBandData = broadBandMap.get(stateCode + countyCode);
    return broadBandData;
  }

  /**
   * Gets and returns the cache and its contents Only used for testing purposes
   *
   * @return the cache of <String, Map>
   */
  public Cache<String, Map> getCache() {
    return this.cache;
  }
}
