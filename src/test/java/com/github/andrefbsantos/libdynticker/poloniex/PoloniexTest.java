package com.github.andrefbsantos.libdynticker.poloniex;

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

public class PoloniexTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new PoloniexExchange(10000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseJson() {

		JsonNode node;
		try {
			node = (new ObjectMapper().readTree(new File("src/test/json/poloniex-ticker.json")));

			Pair pair = new Pair("XMR", "BTC");
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("0.00452783", lastValue);
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
			Assert.assertTrue(pairs.contains(new Pair("LTC", "BTC")));
			Assert.assertTrue(pairs.contains(new Pair("XMR", "BTC")));

			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));

		} catch (IOException e) {
			Assert.fail();
		}

	}

	@Test
	public void testGetLastValue() {
		try {
			double lastValue = testExchange.getLastValue(new Pair("XMR", "BTC"));
			Assert.assertNotNull(lastValue);
		} catch (IOException e) {
			Assert.fail();
		}
	}

}
