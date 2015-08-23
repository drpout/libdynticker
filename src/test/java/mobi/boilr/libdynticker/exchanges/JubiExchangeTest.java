package mobi.boilr.libdynticker.exchanges;

import java.io.File;
import java.io.IOException;

import mobi.boilr.libdynticker.core.ExchangeTest;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JubiExchangeTest extends ExchangeTest {

	@Override
	@Before
	public void setUp() throws Exception {
		testExchange = new JubiExchange(1000);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseTicker() {
		try {
			Pair pair = new Pair("SAK", "BTC");
			JsonNode node = (new ObjectMapper().readTree(new File("src/test/json/jubi-ticker.json")));
			String lastValue = testExchange.parseTicker(node, pair);
			Assert.assertEquals("0.2434", lastValue);
		}
		catch(IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}