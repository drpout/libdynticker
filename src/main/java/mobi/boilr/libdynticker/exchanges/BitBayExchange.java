package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BitBayExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "PLN"));
		tempPairs.add(new Pair("BTC", "USD"));
		tempPairs.add(new Pair("BTC", "EUR"));
		tempPairs.add(new Pair("LTC", "BTC"));
		tempPairs.add(new Pair("LTC", "PLN"));
		tempPairs.add(new Pair("LTC", "USD"));
		tempPairs.add(new Pair("LTC", "EUR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BitBayExchange(long expiredPeriod) {
		super("BitBay", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://market.bitbay.pl/API/Public/BTCPLN/ticker.json
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		JsonNode node = readJsonFromUrl("https://market.bitbay.pl/API/Public/"
				+ pair.getCoin() + pair.getExchange() + "/ticker.json");
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}

}
