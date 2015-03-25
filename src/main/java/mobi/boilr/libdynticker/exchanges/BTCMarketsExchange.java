package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

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
			throw new MalformedURLException(node.get("errorMessage").getTextValue());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("lastPrice").toString();
	}

}
