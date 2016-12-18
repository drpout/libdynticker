package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class BTCEExchange extends Exchange {

	public BTCEExchange(long expiredPeriod) {
		super("BTC-E", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<String> elements = readJsonFromUrl("https://btc-e.com/api/3/info").get("pairs").getFieldNames();
		for(String[] split; elements.hasNext();) {
			split = elements.next().toUpperCase().split("_");
			pairs.add(new Pair(split[0], split[1]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String pairCode = pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase();
		JsonNode node = readJsonFromUrl("https://btc-e.com/api/3/ticker/" + pairCode);
		if(node.has(pairCode)) {
			return parseTicker(node, pair);
		} else {
			throw new IOException(node.get("error").asText());
		}
	}


	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		String pairCode = pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase();
		return node.get(pairCode).get("last").asText();
	}
}