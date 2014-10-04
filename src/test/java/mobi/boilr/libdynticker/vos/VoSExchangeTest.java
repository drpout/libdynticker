package mobi.boilr.libdynticker.vos;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mobi.boilr.libdynticker.core.ExchangeTest;
import mobi.boilr.libdynticker.core.Pair;

public class VoSExchangeTest extends ExchangeTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new VoSExchange(10000);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPairs() {
		List<Pair> pairs;
		try {
			pairs = testExchange.getPairs();
			Assert.assertTrue(pairs.contains(new Pair("BTC", "USD")));
			Assert.assertTrue(pairs.contains(new Pair("BTC", "CAD")));
			Assert.assertTrue(pairs.contains(new Pair("LTC", "BTC")));
			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
			
			for(Pair pair : pairs){
				System.out.println(pair);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("BTC", "CAD");
			JsonNode node;
			node = (new ObjectMapper().readTree(new File("src/test/json/vos-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("622.34038318", lastValue);
		} catch (IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetLastValue() {
		try {
			double lastValue = testExchange.getLastValue(new Pair("BTC", "USD"));
			Assert.assertNotNull(lastValue);
		} catch (IOException e) {
			Assert.fail();
		}
	}
}