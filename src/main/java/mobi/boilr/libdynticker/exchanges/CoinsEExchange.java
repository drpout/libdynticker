package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public final class CoinsEExchange extends Exchange {

	public CoinsEExchange(long expiredPeriod) {
		super("Coins-E", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
			IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = (new ObjectMapper()).readTree(new URL("https://www.coins-e.com/api/v2/markets/list/"));
		if(!node.get("status").getBooleanValue()) {
			throw new IOException(node.get("message").getTextValue());
		}
		Iterator<JsonNode> elements = node.get("markets").getElements();
		for (JsonNode element; elements.hasNext();) {
			element = elements.next();
			if(element.get("status").getTextValue().equals("healthy")) {
				pairs.add(new Pair(element.get("c1").getTextValue(), element.get("c2").getTextValue()));
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
	IOException {
		// https://www.coins-e.com/api/v2/market/DRK_BTC/trades/
		JsonNode node = (new ObjectMapper()).readTree(new URL("https://www.coins-e.com/api/v2/market/"
				+ pair.getCoin() + "_" + pair.getExchange() + "/trades/"));
		if(!node.get("status").getBooleanValue()) {
			throw new IOException(node.get("message").getTextValue());
		}
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		node = node.get("trades");
		if(!node.getElements().hasNext())
			throw new IOException("Trades for " + pair + " are empty.");
		return node.get(0).get("rate").getTextValue();
	}

}
