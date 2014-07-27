package com.github.andrefbsantos.libdynticker.mintpal;

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

import com.github.andrefbsantos.libdynticker.ExchangeTest;
import com.github.andrefbsantos.libdynticker.core.ExchangeException;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class MintPalExchangeTest extends ExchangeTest {

	private MintPalExchange testExchange;

	@Before
	public void setUp() throws Exception {
		testExchange = new MintPalExchange();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseJson() {
		String coin = "XMR";
		String exchange = "BTC";
		JsonNode node;
		try {
			node = (new ObjectMapper().readTree(new File(
					"json/mintpal-stats.json")));
			String lastValue = testExchange.parseJSON(node, coin, exchange);
			System.out.println(lastValue);
			Assert.assertEquals("0.00437000", lastValue);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			Assert.fail();
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
			Assert.assertNotEquals(0, pairs.size());
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
	public void testGetLastValue() {

		try {
			testExchange.getLastValue(new Pair("XMR", "BTC"));
		} catch (ExchangeException e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

}
