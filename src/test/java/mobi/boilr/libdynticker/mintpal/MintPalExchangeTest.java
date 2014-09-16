package mobi.boilr.libdynticker.mintpal;

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

/**
 * @author andre
 *
 */
public class MintPalExchangeTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new MintPalExchange(10000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	/**
	 *
	 */
	@Test
	public void testParseJson() {
		JsonNode node;
		try {
			node = (new ObjectMapper().readTree(new File("src/test/json/mintpal-ticker.json")));
			Pair pair = new Pair("XMR", "BTC");
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("0.00437000", lastValue);
		} catch (IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetPairs() {
		List<Pair> pairs;
		try {
			pairs = testExchange.getPairs();
			Assert.assertNotEquals(0, pairs.size());
			Assert.assertTrue(pairs.contains(new Pair("AUR", "BTC")));
			Assert.assertTrue(pairs.contains(new Pair("DOGE", "BTC")));
			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
		} catch (IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetLastValue() {
		double lastValue;
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("DOGE", "BTC"));
		pairs.add(new Pair("AUR", "BTC"));
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
