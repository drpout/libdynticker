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

public class CoinMktExchangeTest extends ExchangeTest {
	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new CoinMktExchange(10000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	/*
	 * CoinMkt requires a 15 s interval between calls to the same API URL.
	 */
	@Override
	@Test
	public void testGetLastValueWithPairsFromGetPairs() {
		try {
			Thread.sleep(CoinMktExchange.COINMKT_DELAY);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
			Assert.fail();
		}
		super.testGetLastValueWithPairsFromGetPairs();
	}

	@Test
	public void testGetPairs() {
		List<Pair> pairs;
		try {
			pairs = testExchange.getPairs();
			Assert.assertTrue(pairs.contains(new Pair("BTC", "USD")));
			Assert.assertTrue(pairs.contains(new Pair("DOGE", "BTC")));
			Assert.assertFalse(pairs.contains(new Pair("Invalid", "BTC")));
		}
		catch(IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("DOGE", "BTC");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/coinmkt-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("6.6E-7", lastValue);
		}
		catch(IOException e) {
			Assert.fail();
		}
	}
}
