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

public class BasebitExchangeTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new BasebitExchange(10000);
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
			Assert.assertTrue(pairs.contains(new Pair("BTC", "BRL")));
			Assert.assertFalse(pairs.contains(new Pair("Invalid", "BTC")));
		} catch(IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testParseTicker() {
		try {
			Pair pair = new Pair("LTC", "BTC");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/basebit-ticker.json")));
			String lastValue = testExchange.parseTicker(node, pair);
			Assert.assertEquals("0.00001000", lastValue);
		} catch(IOException e) {
			Assert.fail();
		}
	}

}
