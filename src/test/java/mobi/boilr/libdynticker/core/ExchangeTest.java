package mobi.boilr.libdynticker.core;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ExchangeTest {
	protected Exchange testExchange;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	protected void handleException(Pair pair, Exception e) {
		System.err.println(pair);
		e.printStackTrace();
		Assert.fail();
	}

	@Test
	public void testGetLastValueWithPairsFromGetPairs() {
		List<Pair> pairs = null;
		try {
			pairs = testExchange.getPairs();
			Assert.assertNotNull(pairs);
			Assert.assertTrue(pairs.size() > 0);
		}
		catch(IOException e) {
			e.printStackTrace();
			Assert.fail();
		}

		for(Pair pair : pairs) {
			try {
				double lastValue = testExchange.getLastValue(pair);
				Assert.assertNotNull(lastValue);
			}
			catch(Exception e) {
				handleException(pair, e);
			}
		}
	}

	@Test(expected = IOException.class)
	public void testInvalidPair() throws IOException {
		testExchange.getLastValue(new Pair("InvalidCoin", "InvalidExchange"));
	}
}
