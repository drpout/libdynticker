/**
 * 
 */
package com.github.andrefbsantos;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.andrefbsantos.libdynticker.bitstamp.BitstampExchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

/**
 * @author andre
 * 
 */
public class BitStampExchangeTest {

	BitstampExchange bitstampExchange;

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
		bitstampExchange = new BitstampExchange();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPairs() {
		List<Pair> pairs = bitstampExchange.getPairs();
		Assert.assertEquals(pairs.get(0).getCoin(), "BTC");
		Assert.assertEquals(pairs.get(0).getExchange(), "USD");
	}

	@Test
	public void testGetLastValue() {
		try {
			bitstampExchange.getLastValue(new Pair("BTC", "USD"));
		} catch (JsonProcessingException e) {
			Assert.fail();
		} catch (MalformedURLException e) {
			Assert.fail();
		} catch (IOException e) {
			Assert.fail();
		}
	}
}
