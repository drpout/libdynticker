package mobi.boilr.libdynticker.bullionvault;

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

public class BullionVaultExchangeTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new BullionVaultExchange(10000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("AUX", "USD");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/bullionvault-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("41789.44398", lastValue);
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
			Assert.assertTrue(pairs.contains(new Pair("AUX", "USD")));
			Assert.assertTrue(pairs.contains(new Pair("AUX", "EUR")));
			Assert.assertTrue(pairs.contains(new Pair("AGX", "AUD")));
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

		pairs.add(new Pair("AUX", "USD"));
		pairs.add(new Pair("AUX", "EUR"));
		pairs.add(new Pair("AGX", "AUD"));

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
