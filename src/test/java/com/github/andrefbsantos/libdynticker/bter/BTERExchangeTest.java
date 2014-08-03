package com.github.andrefbsantos.libdynticker.bter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.andrefbsantos.libdynticker.core.ExchangeTest;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class BTERExchangeTest extends ExchangeTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new BTERExchange();
	}

	/**
	 * @throws java.lang.Exception
	 */
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
	public void testParseJson() {
		try {
			Pair pair = new Pair("BTC", "USD");
			JsonNode node;

			node = (new ObjectMapper().readTree(new File("src/test/json/bter-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("605", lastValue);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testGetLastValue() {
		try {
			double lastValue = testExchange.getLastValue(new Pair("BTC", "USD"));
			Assert.assertNotNull(lastValue);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
