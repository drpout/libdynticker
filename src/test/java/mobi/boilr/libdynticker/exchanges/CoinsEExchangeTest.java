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



public class CoinsEExchangeTest extends ExchangeTest {
	@Override
	protected void handleException(Pair pair, Exception e) {
		if(e instanceof IOException && (e.getMessage().contains("empty") || e.getMessage().contains("500"))) {
			System.err.println(pair);
			System.err.println(e);
		} else {
			super.handleException(pair, e);
		}
	}

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new CoinsEExchange(10000);
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
			Assert.assertTrue(pairs.contains(new Pair("DRK", "BTC")));
			Assert.assertTrue(pairs.contains(new Pair("DOGE", "BTC")));
			Assert.assertFalse(pairs.contains(new Pair("Invalid", "BTC")));
		} catch (IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("DRK", "BTC");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/coinse-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("0.00538950", lastValue);
		} catch (IOException e) {
			Assert.fail();
		}
	}

}
