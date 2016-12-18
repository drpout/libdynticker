package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BitMarketPlExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "PLN"));
		tempPairs.add(new Pair("BTC", "EUR"));
		tempPairs.add(new Pair("LTC", "PLN"));
		tempPairs.add(new Pair("LTC", "BTC"));
		tempPairs.add(new Pair("DOGE", "PLN"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BitMarketPlExchange(long expiredPeriod) {
		super("BitMarket.pl", expiredPeriod);
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
		// https://www.bitmarket.pl/json/BTCPLN/ticker.json
		JsonNode node = readJsonFromUrl("https://www.bitmarket.pl/json/" +
				pair.getCoin() + pair.getExchange() + "/ticker.json");
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("last").asText();
	}

}
