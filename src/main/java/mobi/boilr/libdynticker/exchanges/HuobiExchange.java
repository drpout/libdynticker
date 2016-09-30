package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class HuobiExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "CNY"));
		tempPairs.add(new Pair("LTC", "CNY"));
		tempPairs.add(new Pair("BTC", "USD"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public HuobiExchange(long expiredPeriod) {
		super("Huobi", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		JsonNode node;
		if(pair.getExchange().equals("CNY"))
			// http://api.huobi.com/staticmarket/detail_btc_json.js
			node = readJsonFromUrl(
					"http://api.huobi.com/staticmarket/detail_" + pair.getCoin().toLowerCase() + "_json.js");
		else
			node = readJsonFromUrl(
					"http://api.huobi.com/usdmarket/detail_" + pair.getCoin().toLowerCase() + "_json.js");
		if(node.has("error"))
			throw new IOException(node.get("error").asText());
		else
			return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("p_new").asText();
	}
}
