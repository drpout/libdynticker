package com.github.andrefbsantos.libdynticker.core;

import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Abstract tests for other exchanges that inherit this class
 *
 * @author andre
 *
 */
@Ignore
public class ExchangeTest {
	protected Exchange testExchange;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetLastValueWithPairsFromGetPairs() {
		List<Pair> pairs = null;
		try {
			pairs = testExchange.getPairs();
			Assert.assertNotNull(pairs);
			Assert.assertTrue(pairs.size() > 0);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}

		int numberOfExamples;
		// numberOfExamples = pairs.size();
		numberOfExamples = 1;
		for (int i = 0; i < numberOfExamples && i < pairs.size(); i++) {
			try {
				Pair pair = pairs.get(i);
				double lastValue = testExchange.getLastValue(pair);
				Assert.assertNotNull(lastValue);
				System.out.println(String.format("%-20s %-10s %-20s", testExchange.getClass().getSimpleName(), pair, lastValue));
			} catch (IOException e) {
				e.printStackTrace();
				Assert.fail();
			}
		}
	}

	@Test
	public void testSecondGetPairsFromAPIDoesActive() {
		try {
			Exchange exchange = Mockito.spy(testExchange);
			exchange.setExperiedPeriod(-1);
			exchange.getPairs();
			verify(exchange, Mockito.times(1)).getPairsFromAPI();
			Assert.assertNotNull(exchange.getTimestamp());
			exchange.getPairs();
			verify(exchange, Mockito.times(2)).getPairsFromAPI();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testSecondGetPairsFromAPIDoesntActive() {
		try {
			Exchange exchange = Mockito.spy(testExchange);
			exchange.setExperiedPeriod(Long.MAX_VALUE);
			exchange.getPairs();
			verify(exchange, Mockito.times(1)).getPairsFromAPI();
			Assert.assertNotNull(exchange.getTimestamp());
			exchange.getPairs();
			verify(exchange, Mockito.times(1)).getPairsFromAPI();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testSerialize() {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(testExchange);
			byte[] bytes = byteArrayOutputStream.toByteArray();
			objectOutputStream.close();
			byteArrayOutputStream.close();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			ObjectInputStream in = new ObjectInputStream(byteArrayInputStream);
			Exchange newExchange = (Exchange) in.readObject();
			in.close();
			byteArrayInputStream.close();

			Assert.assertEquals(testExchange.getClass(), newExchange.getClass());
			Assert.assertEquals(testExchange.getExperiedPeriod(), newExchange.getExperiedPeriod());
			Assert.assertEquals(testExchange.getTimestamp(), newExchange.getTimestamp());
			Assert.assertEquals(testExchange.getPairs(), newExchange.getPairs());

		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testName() {
		Assert.assertEquals(testExchange.getName() + "Exchange", testExchange.getClass().getSimpleName());
	}
}
