package mobi.boilr.libdynticker.exchanges;

import java.io.File;
import java.io.IOException;
import java.util.List;

import mobi.boilr.libdynticker.core.ExchangeTest;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class BleuTradeExchangeTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new BleuTradeExchange(10000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Override
	@Test
	public void testGetLastValueWithPairsFromGetPairs() {
		List<Pair> pairs = null;
		try {
			pairs = testExchange.getPairs();
			Assert.assertNotNull(pairs);
			Assert.assertTrue(pairs.size() > 0);
		} catch(IOException e) {
			e.printStackTrace();
			Assert.fail();
		}

		int numberOfExamples = 100;
		int size = numberOfExamples <= pairs.size() ? numberOfExamples : pairs.size();
		for(int i = 0; i < size; i++) {
			Pair pair = null;
			try {
				pair = pairs.get(i);
				double lastValue = testExchange.getLastValue(pair);
				Assert.assertNotNull(lastValue);
			} catch(IOException e) {
				System.out.println(pair);
				e.printStackTrace();
				Assert.fail();
			}
		}
	}

	@Test
	public void testGetPairs() {
		List<Pair> pairs;
		try {
			pairs = testExchange.getPairs();
			Assert.assertTrue(pairs.contains(new Pair("LTC", "BTC")));
			Assert.assertTrue(pairs.contains(new Pair("CDN", "SWIFT")));
			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
		} catch(JsonProcessingException e) {
			Assert.fail();
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("LTC", "BTC");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/bleutrade-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("0.01218180", lastValue);
		} catch(IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}