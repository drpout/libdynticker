package mobi.boilr.libdynticker.exchanges;

import java.io.File;
import java.io.IOException;
import java.util.List;

import mobi.boilr.libdynticker.core.ExchangeTest;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NXTAssetExchangeTest extends ExchangeTest {
	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new NXTAssetExchange(1000);
	}

	@Override
	@After
	public void tearDown() throws Exception {}

	@Test
	public void testGetPairs() {
		List<Pair> pairs;
		try {
			pairs = testExchange.getPairs();
			Assert.assertTrue(pairs.contains(new Pair("mgwBTC", "NXT")));
			Assert.assertFalse(pairs.contains(new Pair("Invalid", "NXT")));
		} catch(IOException e) {
			Assert.fail();
		}
	}

	@Override
	@Test
	public void testGetLastValueWithPairsFromGetPairs() {
		List<Pair> pairs = null;
		try {
			pairs = testExchange.getPairs();
			Assert.assertNotNull(pairs);
			Assert.assertTrue(pairs.size() > 0);
		} catch(IOException e) {
			e.printStackTrace();
			Assert.fail();
		}

		int allowedFails = (int) (pairs.size() * 0.1);
		for(Pair pair : pairs) {
			try {
				double lastValue = testExchange.getLastValue(pair);
				Assert.assertNotNull(lastValue);
			} catch(Exception e) {
				System.err.println(pair + " " + pair.getMarket());
				e.printStackTrace();
				if(--allowedFails == 0)
					Assert.fail();
			}
		}
	}

	@Test
	public void testParseJson() {
		try {
			Pair pair = new Pair("mgwBTC", "NXT", "17554243582654188572");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/nxtassetexchange-ticker.json")));
			String lastValue = testExchange.parseJSON(node, pair);
			Assert.assertEquals(".00019755", lastValue);
		} catch(IOException e) {
			Assert.fail();
		}
	}

}
