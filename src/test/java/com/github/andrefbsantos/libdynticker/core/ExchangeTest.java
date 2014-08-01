package com.github.andrefbsantos.libdynticker.core;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Abstract tests for other exchanges that inherit this class
 *
 * @author andre
 *
 */
@Ignore
public class ExchangeTest {
	protected Exchange testExchange;

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

		int numberOfExamples = 2;
		for (int i = 0; i < numberOfExamples && i < pairs.size(); i++) {
			try {
				Pair pair = pairs.get(i);
				double lastValue = testExchange.getLastValue(pair);
				Assert.assertNotNull(lastValue);
			} catch (IOException e) {
				e.printStackTrace();
				Assert.fail();
			}
		}

	}
}
