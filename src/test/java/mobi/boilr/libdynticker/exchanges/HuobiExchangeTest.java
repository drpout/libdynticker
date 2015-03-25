package mobi.boilr.libdynticker.exchanges;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import mobi.boilr.libdynticker.core.ExchangeTest;
import mobi.boilr.libdynticker.core.Pair;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class HuobiExchangeTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new HuobiExchange(10000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPairs() {
		List<Pair> pairs;
		try {
			pairs = testExchange.getPairs();
			Assert.assertTrue(pairs.contains(new Pair("BTC", "CNY")));
			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
		} catch (IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testParseTicker() {
		try {
			Pair pair = new Pair("BTC", "CNY");
			String file = "src/test/json/houbi-ticker.js";
			String data = "";
			BufferedReader in = new BufferedReader(new FileReader(file));
			String inputLine;
			while((inputLine = in.readLine()) != null)
				data += inputLine;
			in.close();
			String lastValue = (((HuobiExchange) testExchange).getLast(data, pair));
			Assert.assertEquals("1689.7", lastValue);
		} catch(IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
