package mobi.boilr.libdynticker.btc38;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mobi.boilr.libdynticker.core.ExchangeTest;
import mobi.boilr.libdynticker.core.Pair;

public class BTC38ExchangeTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new BTC38Exchange(10000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("LTC", "BTC");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/btc38-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			System.out.println(lastValue);
			Assert.assertEquals("0.008", lastValue);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testGetPairs() {
		List<Pair> pairs;
		try {
			pairs = testExchange.getPairs();
			Assert.assertTrue(pairs.contains(new Pair("LTC", "CNY")));
			Assert.assertTrue(pairs.contains(new Pair("BTC", "CNY")));
			Assert.assertTrue(pairs.contains(new Pair("LTC", "BTC")));
			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testGetLastValue() {
		double lastValue;
		List<Pair> pairs = new ArrayList<Pair>();

		pairs.add(new Pair("LTC", "CNY"));
		pairs.add(new Pair("BTC", "CNY"));
		pairs.add(new Pair("LTC", "BTC"));

		try {
			for (Pair pair : pairs) {
				lastValue = testExchange.getLastValue(pair);
				Assert.assertNotNull(lastValue);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
