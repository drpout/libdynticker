package mobi.boilr.libdynticker.core;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import mobi.boilr.libdynticker.core.exception.APICallLimitReachedException;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

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

	@Test
	public void testGetLastValueWithPairsFromGetPairs() {
		List<Pair> pairs = null;
		try {
			pairs = testExchange.getPairs();
			Assert.assertNotNull(pairs);
			Assert.assertFalse(pairs.isEmpty());
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
			catch(SocketTimeoutException e) {
				System.err.println(e.getMessage());
			}
			catch(APICallLimitReachedException e) {
				System.err.println(e.getMessage());
			}
			catch(NoMarketDataException e) {
				System.err.println(e.getMessage());
			}
			catch(Exception e) {
				e.printStackTrace();
				Assert.fail();
			}
		}
	}

	@Test(expected = IOException.class)
	public void testInvalidPair() throws IOException, NumberFormatException, NoMarketDataException {
		testExchange.getLastValue(new Pair("InvalidCoin", "InvalidExchange"));
	}
}
