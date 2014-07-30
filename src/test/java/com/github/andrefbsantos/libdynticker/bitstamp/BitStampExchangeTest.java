/**
 * 
 */
package com.github.andrefbsantos.libdynticker.bitstamp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.andrefbsantos.libdynticker.core.Pair;

/**
 * @author andre
 * 
 */
public class BitStampExchangeTest {

	BitstampExchange testExchange;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testExchange = new BitstampExchange();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPairs() {
		List<Pair> pairs = testExchange.getPairs();
		Assert.assertTrue(pairs.contains(new Pair("BTC", "USD")));
		Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
		
	}

	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("BTC","USD");
			JsonNode node;

			node = (new ObjectMapper().readTree(new File(
					"src/test/json/bitstamp.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("600.15", lastValue);
		} catch (IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetLastValue() {
		try {
			float lastValue = testExchange.getLastValue(new Pair("BTC", "USD"));
			Assert.assertNotNull(lastValue);
		} catch (IOException e) {
			Assert.fail();
		}

	}
}
