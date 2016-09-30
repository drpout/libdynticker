package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BittExchange extends Exchange {

	public BittExchange(long expiredPeriod) {
		super("Bitt", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://api.bitt.com:8400/ajax/v1/GetProductPairs");
		if(node.get("isAccepted").asBoolean()) {
			for(JsonNode pair : node.get("productPairs"))
				pairs.add(new Pair(pair.get("product1Label").asText(), pair.get("product2Label").asText()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://api.bitt.com:8400/ajax/v1/GetTicker",
				"{\"productPair\":\"" + pair.getCoin() + pair.getExchange() + "\"}");
		if(node.get("isAccepted").asBoolean())
			return parseTicker(node, pair);
		else
			throw new IOException("Invalid pair: " + pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}

}
