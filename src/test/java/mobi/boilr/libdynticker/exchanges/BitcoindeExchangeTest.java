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

public class BitcoindeExchangeTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new BitcoindeExchange(10000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Override
	protected void handleException(Pair pair, Exception e) {
		if(e instanceof IOException && e.getMessage().equals("API call limit reached.")) {
			String pairToString = pair.toString();
			System.err.println(pairToString + " " + e.getMessage());
		} else {
			super.handleException(pair, e);
		}
	}

	@Test
	public void testGetPairs() {
		List<Pair> pairs;
		try {
			pairs = testExchange.getPairs();
			Assert.assertTrue(pairs.contains(new Pair("BTC", "EUR")));
			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
		} catch (IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testParseTicker() {
		try {
			Pair pair = new Pair("BTC", "EUR");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/bitcoinde-ticker.json")));
			String lastValue = testExchange.parseTicker(node, pair);
			Assert.assertEquals("202.95", lastValue);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
