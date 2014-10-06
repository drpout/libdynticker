/**
 *
 */
package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

/**
 * @author andre
 *
 */
public class BleuTradeExchange extends Exchange {

	public BleuTradeExchange(long experiedPeriod) {
		super("BleuTrade", experiedPeriod);
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://bleutrade.com/api/v2/public/getticker?market=" + pair.getExchange() + "_" + pair.getCoin();
		return parseJSON((new ObjectMapper()).readTree(new URL(url)), pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if (node.get("success").getTextValue().equals("true")) {
			return node.get("result").getElements().next().get("Last").getTextValue();
		} else {
			throw new IOException(node.get("message").getTextValue());
		}
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<JsonNode> elements = (new ObjectMapper()).readTree(new URL("https://bleutrade.com/api/v2/public/getmarkets")).get("result").getElements();
		while (elements.hasNext()) {
			JsonNode next = elements.next();
			boolean isActive = next.get("IsActive").getTextValue().equals("true");
			if (isActive) {
				String coin = next.get("MarketCurrency").getTextValue();
				String exchange = next.get("BaseCurrency").getTextValue();
				Pair pair = new Pair(coin, exchange);
				pairs.add(pair);
			}
		}
		return pairs;
	}
}
