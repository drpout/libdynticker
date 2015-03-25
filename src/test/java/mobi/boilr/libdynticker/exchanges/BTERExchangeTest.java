package mobi.boilr.libdynticker.exchanges;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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



public class BTERExchangeTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new BTERExchange(10000);
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
			Assert.assertTrue(pairs.contains(new Pair("BTC", "USD")));
			Assert.assertTrue(pairs.contains(new Pair("XMR", "BTC")));
			Assert.assertTrue(pairs.contains(new Pair("DOGE", "CNY")));

			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testParseTicker() {
		try {
			Pair pair = new Pair("BTC", "USD");
			JsonNode node;

			node = (new ObjectMapper().readTree(new File("src/test/json/bter-ticker.json")));
			String lastValue = testExchange.parseTicker(node, pair);
			Assert.assertEquals("605", lastValue);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
