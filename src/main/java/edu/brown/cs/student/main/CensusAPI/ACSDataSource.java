package edu.brown.cs.student.main.CensusAPI;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.brown.cs.student.main.EvictionPolicy;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;


public class ACSDataSource {
    private Cache<String, Object> cache;


    public ACSDataSource(long limit, EvictionPolicy policy) {
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

    public Map<String,State> populateStateCache(String key) throws IOException, ExecutionException {
        Callable<Map<String,State>> valueLoader = () -> {
            Map<String,State> stateMap = CensusAPIUtilities.deserializeStateCodes();
            this.cache.put(key,stateMap);
           return stateMap;
        };
        Map<String,State> stateMap = this.getOrLoadValue(key,valueLoader);
        return stateMap;
    }
    public Map<String,County> populateCountyCache(String stateCode) throws ExecutionException{
        Callable<Map<String,County>> valueLoader = () -> {
            Map<String,County> countyMap = CensusAPIUtilities.deserializeCountyCodes(stateCode);
            this.cache.put(stateCode,countyMap);
            return countyMap;
        };
        Map<String,County> countyMap = this.getOrLoadValue(stateCode,valueLoader);
        return countyMap;
    }

    private <V> V getOrLoadValue(String key, Callable<V> valueLoader) throws ExecutionException {
        return (V) this.cache.get(key,valueLoader);
    }







}
