import com.google.common.cache.Cache;
import edu.brown.cs.student.main.Cache.ACSDataSource;
import edu.brown.cs.student.main.Cache.EvictionPolicy;
import edu.brown.cs.student.main.CensusAPI.County;
import org.testng.Assert;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Map;

public class testCache {

    @Test
    public void testCacheAddition() throws IOException {
        ACSDataSource cache = new ACSDataSource(0, EvictionPolicy.SIZE);
        cache.getCountyCache("01");
        Assert.assertEquals(cache.getCacheSize(), 0);

        ACSDataSource cache1 = new ACSDataSource(10, EvictionPolicy.SIZE);
        cache1.getCountyCache("01");
        Assert.assertEquals(cache1.getCacheSize(), 1);
        cache1.getBroadbandData("06", "073");
        Assert.assertEquals(cache1.getCacheSize(), 2);
        }

    @Test
    public void testCacheLimitSize() throws IOException {
        ACSDataSource cache = new ACSDataSource(2, EvictionPolicy.SIZE);
        cache.getCountyCache("01");
        cache.getCountyCache("02");

        Cache<String, Map> data = cache.getCache();
        Assert.assertNotNull(data.getIfPresent("02"));
        Assert.assertNull(data.getIfPresent("07"));

        cache.getCountyCache("06");
        Assert.assertNull(data.getIfPresent("01"));
    }

    @Test
    public void testCacheLimitReference() throws IOException {
        ACSDataSource cache = new ACSDataSource(2, EvictionPolicy.REFERENCE);
        cache.getCountyCache("01");
        cache.getCountyCache("02");

        Cache<String, Map> data = cache.getCache();
        Assert.assertNotNull(data.getIfPresent("02"));
        Assert.assertNull(data.getIfPresent("07"));

        Map<String, County> map1 = data.getIfPresent("02");
        Map<String, County> map2 = data.getIfPresent("02");

        cache.getCountyCache("06");
        Assert.assertNull(data.getIfPresent("01"));

        ACSDataSource cache1 = new ACSDataSource(2, EvictionPolicy.REFERENCE);
        cache.getCountyCache("01");
        cache.getCountyCache("02");

        Cache<String, Map> data1 = cache.getCache();
        Assert.assertNotNull(data.getIfPresent("02"));
        Assert.assertNull(data.getIfPresent("07"));

        Map<String, County> map3 = data.getIfPresent("01");
        Map<String, County> map4 = data.getIfPresent("01");

        cache.getCountyCache("06");
        Assert.assertNull(data.getIfPresent("02"));
    }
}


