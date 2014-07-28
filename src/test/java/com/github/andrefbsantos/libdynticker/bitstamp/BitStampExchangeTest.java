/**
 * 
 */
package com.github.andrefbsantos.libdynticker.bitstamp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.andrefbsantos.libdynticker.core.ExchangeException;
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
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

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
		Assert.assertEquals(pairs.get(0).getCoin(), "BTC");
		Assert.assertEquals(pairs.get(0).getExchange(), "USD");
	}

	@Test
	public void testParseJson() {
		String coin = "BTC";
		String exchange = "USD";
		JsonNode node;
		try {
			node = (new ObjectMapper().readTree(new File(
					"src/test/json/bitstamp.json")));
			String lastValue = testExchange.parseJSON(node, coin, exchange);
			Assert.assertEquals("600.15", lastValue);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testGetLastValue() {
		try {
			testExchange.getLastValue(new Pair("BTC", "USD"));
		} catch (ExchangeException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
