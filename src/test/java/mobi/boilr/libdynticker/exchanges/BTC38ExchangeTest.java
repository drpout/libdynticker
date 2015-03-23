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



public class BTC38ExchangeTest extends ExchangeTest {
	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new BitsoExchange(10000);
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
			Assert.assertTrue(pairs.contains(new Pair("BTC", "MXN")));
			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
		}
		catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testParseTicker() {
		try {
			Pair pair = new Pair("BTC", "NMC");
			JsonNode node = (new ObjectMapper().readTree(new File(
"src/test/json/bitso-ticker.json")));
			String lastValue = testExchange.parseTicker(node, pair);
			Assert.assertEquals("3980.00", lastValue);
		}
		catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}