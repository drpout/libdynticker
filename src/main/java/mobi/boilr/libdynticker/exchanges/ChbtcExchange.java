package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class ChbtcExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "CNY"));
		tempPairs.add(new Pair("LTC", "CNY"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public ChbtcExchange(long expiredPeriod) {
		super("CHBTC", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// BTC: http://api.chbtc.com/data/ticker
		// LTC: http://api.chbtc.com/data/ltc/ticker
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		String url = "http://api.chbtc.com/data/";
		if(pair.getCoin().equals("LTC"))
			url += "ltc/";
		url += "ticker";
		JsonNode node = readJsonFromUrl(url);
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("ticker").get("last").asText();
	}

}
