package edu.brown.cs.student.main.CensusAPI;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.brown.cs.student.main.EvictionPolicy;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.eclipse.jetty.util.IO;


public class ACSDataSource {

    private Cache<String, Map> cache;
    private Map<String, State> stateMap;


    public ACSDataSource(long limit, EvictionPolicy policy) {
        this.stateMap = new HashMap<>();
        switch (policy) {
            case SIZE:
                CacheBuilder<Object, Object> sizeBuilder = CacheBuilder.newBuilder()
                    .maximumSize(limit);
                this.cache = sizeBuilder.build();
                break;
            case TIME:
                CacheBuilder<Object, Object> timeBuilder = CacheBuilder.newBuilder()
                    .expireAfterAccess(Duration.ofMinutes(limit));
                this.cache = timeBuilder.build();
                break;
        }
    }

    public ACSDataSource(EvictionPolicy policy) {
        this.stateMap = new HashMap<>();
        switch (policy) {
            case REFERENCE:
                CacheBuilder<Object, Object> referenceBuilder = CacheBuilder.newBuilder()
                    .weakKeys().softValues();
                this.cache = referenceBuilder.build();
                break;
            case NONE:
                CacheBuilder<Object, Object> noneBuilder = CacheBuilder.newBuilder();
                this.cache = noneBuilder.build();
                break;

        }

    }

    public Map<String, State> getStates() throws IOException {
        if (this.stateMap.isEmpty()) {
            this.stateMap = CensusAPIUtilities.deserializeStateCodes();
        }
        return this.stateMap;
    }


    public Map getCountyCache(String stateCode) throws IOException {
        Map<String,County> countyMap;
        if (this.cache.getIfPresent(stateCode) == null) {
            countyMap = CensusAPIUtilities.deserializeCountyCodes(stateCode);
            this.cache.put(stateCode, countyMap);
            return this.cache.asMap().get(stateCode);
        }
        return this.cache.asMap().get(stateCode);
    }

    public List<List<String>> getBroadbandData(String stateCode, String countyCode) throws IOException {
        List<List<String>> broadBandData;
        Map<String, List<List<String>>> broadBandMap = new HashMap<>();
        if (this.cache.getIfPresent(stateCode + countyCode) == null) {
            broadBandData = CensusAPIUtilities.deserializeBroadband(stateCode,countyCode);
            broadBandMap.put(stateCode+countyCode,broadBandData);
            this.cache.put(stateCode+countyCode, broadBandMap);
            return broadBandData;
        }
        broadBandMap = this.cache.asMap().get(stateCode+countyCode);
        broadBandData = broadBandMap.get(stateCode+countyCode);
        return broadBandData;
    }










}
