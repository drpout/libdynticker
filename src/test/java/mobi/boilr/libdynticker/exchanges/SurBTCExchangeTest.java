package mobi.boilr.libdynticker.exchanges;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mobi.boilr.libdynticker.core.ExchangeTest;
import mobi.boilr.libdynticker.core.Pair;

public class SurBTCExchangeTest extends ExchangeTest {
	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new SurBTCExchange(10000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Override
	@Test
	public void testGetPairs() {
		List<Pair> pairs;
		try {
			pairs = testExchange.getPairs();
			Assert.assertNotEquals(0, pairs.size());
			Assert.assertTrue(pairs.contains(new Pair("BTC", "CLP")));
			Assert.assertTrue(pairs.contains(new Pair("BTC", "COP")));
			Assert.assertFalse(pairs.contains(new Pair("Invalid", "BTC")));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testParseTicker() {
		try {
			Pair pair = new Pair("BTC", "CLP");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/surbtc-ticker.json")));
			String lastValue = testExchange.parseTicker(node, pair);
			Assert.assertEquals("409938.29", lastValue);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
