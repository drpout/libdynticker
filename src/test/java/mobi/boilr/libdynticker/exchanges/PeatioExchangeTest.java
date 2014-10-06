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

public class PeatioExchangeTest extends ExchangeTest {
	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new PeatioExchange(1000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("DOG", "CNY");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/peatio-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("0.00229", lastValue);
		} catch(IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetPairs() {
		List<Pair> pairs;
		try {
			pairs = testExchange.getPairs();
			Assert.assertTrue(pairs.contains(new Pair("BTC", "CNY")));
			Assert.assertTrue(pairs.contains(new Pair("DOG", "CNY")));
			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
		} catch(IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetLastValue() {
		try {
			double lastValue = testExchange.getLastValue(new Pair("DOG", "CNY"));
			Assert.assertNotNull(lastValue);
		} catch(IOException e) {
			Assert.fail();
		}
	}

	@Test(expected = IOException.class)
	public void testFailGetLastValue() throws NumberFormatException, IOException {
		testExchange.getLastValue(new Pair("InvalidCoin", "CNY"));
	}
}
