import static org.testng.Assert.assertEquals;

import edu.brown.cs.student.main.CensusAPI.CensusAPIUtilities;
import edu.brown.cs.student.main.CensusAPI.County;
import edu.brown.cs.student.main.CensusAPI.State;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class testCensusAPIUtilities {

  @Test
  public void testDeserializeStateReal() throws IOException {
    Map<String, State> stateMap = CensusAPIUtilities.deserializeStateCodes();
    assertEquals("06", stateMap.get("California").getCode());
  }

  @Test
  public void testDeserializeCountyReal() throws IOException {
    Map<String, County> countyMap = CensusAPIUtilities.deserializeCountyCodes("06");
    assertEquals("073", countyMap.get("San Diego County, California").getCode());
  }

  @Test
  public void testDeserializeBroadbandReal() throws IOException {
    List<List<String>> broadbandList = CensusAPIUtilities.deserializeBroadband("06", "073");
    assertEquals("92.9", broadbandList.get(1).get(1));
  }
}
