package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class CoinsecureExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "INR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public CoinsecureExchange(long expiredPeriod) {
		super("Coinsecure", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		JsonNode node = readJsonFromUrl("https://api.coinsecure.in/v0/noauth/lasttrade");
		if(node.has("error"))
			throw new IOException(node.get("error").asText());
		else
			return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		String value = node.get("result").get(0).get(0).get("lasttrade").get(0).get("bid").get(0).get(0).get("rate")
				.asText();
		int len = value.length();
		return value.substring(0, len - 2) + "." + value.substring(len - 2, len);
	}

}
