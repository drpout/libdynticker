package com.github.andrefbsantos.libdynticker.kraken;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.andrefbsantos.libdynticker.core.ExchangeTest;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class KrakenExchangeTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new KrakenExchange();
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("LTC", "XDG");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/kraken-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("37000.00000", lastValue);
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
			Assert.assertTrue(pairs.contains(new Pair("LTC", "USD")));
			Assert.assertTrue(pairs.contains(new Pair("XBT", "USD")));
			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
		} catch (IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetLastValue() {
		try {
			double lastValue = testExchange.getLastValue(new Pair("XBT", "EUR"));
			Assert.assertNotNull(lastValue);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
