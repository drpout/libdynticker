package com.github.andrefbsantos.libdynticker.hitbtc;

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

public class HitbtcExchangeTest extends ExchangeTest {
	/**
	 * @throws java.lang.Exception
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new HitbtcExchange();

	}

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("BTC", "EUR");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/hitbtc-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("431.05", lastValue);
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
			Assert.assertTrue(pairs.contains(new Pair("BTC", "USD")));
			Assert.assertTrue(pairs.contains(new Pair("BTC", "EUR")));
			Assert.assertTrue(pairs.contains(new Pair("DOGE", "BTC")));
			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));

		} catch (IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetLastValue() {
		try {
			double lastValue = testExchange.getLastValue(new Pair("BTC", "EUR"));
			Assert.assertNotNull(lastValue);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
