package com.github.andrefbsantos.libdynticker.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GetAllExchanges {

	private Set<Class<? extends Exchange>> exchanges;

	@Before
	public void setUp() throws Exception {
		exchanges = Exchange.getExchanges();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetExchanges() throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException, IOException {

		for (Class<? extends Exchange> exchangeClass : exchanges) {
			Constructor<? extends Exchange> constructor = exchangeClass.getConstructor(Long.class);
			Exchange newInstance = constructor.newInstance(100000.0);
			Assert.assertNotNull(newInstance);
			Assert.assertNotNull(newInstance.getName());
			List<Pair> pairs = newInstance.getPairs();
			double lastValue = newInstance.getLastValue(pairs.get(0));
			Assert.assertNotNull(lastValue);

		}
	}
}
