package mobi.boilr.libdynticker.exchanges;

import java.io.File;
import java.io.IOException;
import java.util.List;

import mobi.boilr.libdynticker.core.ExchangeTest;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class BitMEXExchangeTest extends ExchangeTest {

  @Override
  @Before
  public void setUp() throws Exception {
    testExchange = new BitMEXExchange(1000);
  }

  @Override
  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetPairs() {
    List<Pair> pairs;
    try {
      pairs = testExchange.getPairs();
      Assert.assertTrue(pairs.contains(new Pair("XBT", "USD", "XBT24H")));
      Assert.assertTrue(pairs.contains(new Pair("ETH", "XBT", "ETH7D")));
      Assert.assertFalse(pairs.contains(new Pair("DOGE", "XBT", "DOGE1H")));
    }
    catch(IOException e) {
      Assert.fail();
    }
  }

  @Test
  public void testParseTicker() {
    try {
      Pair pair = new Pair("XBT", "USD", "XBT24H");
      JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/bitmex-ticker.json")));
      String lastValue = testExchange.parseTicker(node, pair);
      Assert.assertEquals("416.47", lastValue);
    }
    catch(IOException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }
}
