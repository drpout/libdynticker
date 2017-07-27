package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BitstampExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "USD"));
		tempPairs.add(new Pair("BTC", "EUR"));
		tempPairs.add(new Pair("EUR", "USD"));
		tempPairs.add(new Pair("XRP", "USD"));
		tempPairs.add(new Pair("XRP", "EUR"));
		tempPairs.add(new Pair("XRP", "BTC"));
		tempPairs.add(new Pair("LTC", "USD"));
		tempPairs.add(new Pair("LTC", "EUR"));
		tempPairs.add(new Pair("LTC", "BTC"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BitstampExchange(long expiredPeriod) {
		super("Bitstamp", expiredPeriod);
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair)) {
			throw new IOException("Invalid pair: " + pair);
		}
		return parseTicker(readJsonFromUrl("https://www.bitstamp.net/api/v2/ticker/" + pair.getCoin().toLowerCase()
				+ pair.getExchange().toLowerCase()), pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}

	@Override
	protected List<Pair> getPairsFromAPI() {
		return pairs;
	}
}
