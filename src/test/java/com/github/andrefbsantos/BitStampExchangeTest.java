/**
 * 
 */
package com.github.andrefbsantos;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.andrefbsantos.bitstamp.BitstampExchange;
import com.github.andrefbsantos.core.Exchange;
import com.github.andrefbsantos.core.Pair;

/**
 * @author andre
 * 
 */
public class BitStampExchangeTest {

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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPairs() {
		Exchange exchange = new BitstampExchange();
		List<Pair> pairs = exchange.getPairs();
		Assert.assertEquals(pairs.get(0).getCoin(), "BTC");
		Assert.assertEquals(pairs.get(0).getExchange(), "USD");
	}
}
