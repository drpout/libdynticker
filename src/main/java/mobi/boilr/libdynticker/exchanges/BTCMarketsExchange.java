package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BTCMarketsExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "AUD"));
		tempPairs.add(new Pair("LTC", "AUD"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BTCMarketsExchange(long expiredPeriod) {
		super("BTC Markets", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.btcmarkets.net/market/BTC/AUD/tick
		JsonNode node = readJsonFromUrl("https://api.btcmarkets.net/market/"
				+ pair.getCoin() + "/" + pair.getExchange() + "/tick");
		if(node.has("success"))
			throw new IOException(node.get("errorMessage").asText());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("lastPrice").asText();
	}

}
