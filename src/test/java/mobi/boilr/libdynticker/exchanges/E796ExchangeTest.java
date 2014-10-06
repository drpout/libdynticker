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

public class E796ExchangeTest extends ExchangeTest {
	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new E796Exchange(1000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("BNC BTC Futures (weekly)", "USD");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/796-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("325.00", lastValue);
		} catch(IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetPairs() {
		List<Pair> pairs;
		try {
			pairs = testExchange.getPairs();
			Assert.assertTrue(pairs.contains(new Pair("BNC BTC Futures (weekly)", "USD")));
			Assert.assertTrue(pairs.contains(new Pair("ASICMINER", "BTC")));
			Assert.assertFalse(pairs.contains(new Pair("Invalid", "BTC")));
		} catch(IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetLastValue() {
		try {
			double lastValue = testExchange.getLastValue(new Pair("BNC BTC Futures (weekly)", "USD"));
			Assert.assertNotNull(lastValue);
			lastValue = testExchange.getLastValue(new Pair("ASICMINER", "BTC"));
			Assert.assertNotNull(lastValue);
		} catch(IOException e) {
			Assert.fail();
		}
	}

	@Test(expected = IOException.class)
	public void testFailGetLastValue() throws NumberFormatException, IOException {
		testExchange.getLastValue(new Pair("Invalid", "BTC"));
	}

}
