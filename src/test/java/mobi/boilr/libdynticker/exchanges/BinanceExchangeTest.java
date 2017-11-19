package mobi.boilr.libdynticker.exchanges;

import mobi.boilr.libdynticker.core.ExchangeTest;
import mobi.boilr.libdynticker.core.Pair;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BinanceExchangeTest extends ExchangeTest {
    @Override
    @Before
    public void setUp() throws Exception {
        testExchange = new BinanceExchange(1000);
    }

    @Test
    public void testGetPairs() {
        List<Pair> pairs;
        try{
            pairs = testExchange.getPairs();
            Assert.assertTrue(pairs.contains(new Pair("BTC", "USDT")));
            Assert.assertTrue(pairs.contains(new Pair("ETH", "BTC")));
            Assert.assertFalse(pairs.contains(new Pair("111", "222")));
            Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
        } catch (IOException e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testParseTicker() throws Exception {
        try {
            JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/binance-ticker.json")));
            String lastValue = testExchange.parseTicker(node,  new Pair("ETC", "BTC"));
            Assert.assertEquals("0.00243100", lastValue);
        }
        catch(IOException e) {
            Assert.fail();
        }
    }
}
