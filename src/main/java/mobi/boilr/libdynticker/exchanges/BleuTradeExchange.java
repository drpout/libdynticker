package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public final class BleuTradeExchange extends Exchange {

	public BleuTradeExchange(long expiredPeriod) {
		super("BleuTrade", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<JsonNode> elements = readJsonFromUrl("https://bleutrade.com/api/v2/public/getmarkets").get("result").getElements();
		JsonNode element;
		String coin, exchange;
		boolean isActive;
		while(elements.hasNext()) {
			element = elements.next();
			isActive = element.get("IsActive").getTextValue().equals("true");
			if(isActive) {
				coin = element.get("MarketCurrency").getTextValue();
				exchange = element.get("BaseCurrency").getTextValue();
				pairs.add(new Pair(coin, exchange));
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://bleutrade.com/api/v2/public/getticker?market=" +
				pair.getExchange() + "_" + pair.getCoin());
		if(node.get("success").getTextValue().equals("true")) {
			return parseTicker(node, pair);
		} else {
			throw new IOException(node.get("message").getTextValue());
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("result").getElements().next().get("Last").getTextValue();
	}
}
