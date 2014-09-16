/**
 *
 */
package mobi.boilr.libdynticker.localbitcoins;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mobi.boilr.libdynticker.core.ExchangeTest;
import mobi.boilr.libdynticker.core.Pair;

/**
 * @author andre
 *
 */
public class LocalBitcoinsTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new LocalBitcoins(10000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLastValue() {
		try {
			double lastValue = testExchange.getLastValue(new Pair("BTC", "USD"));
			Assert.assertNotNull(lastValue);
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
			Assert.assertTrue(pairs.contains(new Pair("BTC", "SEK")));
			Assert.assertTrue(pairs.contains(new Pair("BTC", "GBP")));
			Assert.assertTrue(pairs.contains(new Pair("BTC", "PLN")));
			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
		} catch (JsonProcessingException e) {
			Assert.fail();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("BTC", "SEK");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/localbitcoins-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("4175.95", lastValue);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
