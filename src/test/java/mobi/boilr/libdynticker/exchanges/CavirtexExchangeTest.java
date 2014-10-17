package mobi.boilr.libdynticker.exchanges;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import mobi.boilr.libdynticker.core.ExchangeTest;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class CavirtexExchangeTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new CavirtexExchange(10000);
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
			Assert.assertTrue(pairs.contains(new Pair("BTC", "CAD")));
			Assert.assertTrue(pairs.contains(new Pair("LTC", "CAD")));
			Assert.assertTrue(pairs.contains(new Pair("BTC", "LTC")));
			Assert.assertFalse(pairs.contains(new Pair("InvalidCoin", "BTC")));
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
	public void testParseJson() {
		try {
			Pair pair = new Pair("BTC", "CAD");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/cavirtex-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals("630.00001", lastValue);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
