package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BitcurexExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "PLN"));
		tempPairs.add(new Pair("BTC", "EUR"));
		tempPairs.add(new Pair("USD", "EUR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BitcurexExchange(long expiredPeriod) {
		super("Bitcurex", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair)) {
			throw new IOException("Invalid pair: " + pair);
		}
		// https://bitcurex.com/api/eur/trades.json
		JsonNode node = readJsonFromUrl("https://bitcurex.com/api/"
				+ pair.getExchange().toLowerCase() + "/trades.json");
		if(node.getElements().hasNext())
			return parseTicker(node, pair);
		else
			throw new IOException("Invalid pair: " + pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get(0).get("price").asText();
	}
}
