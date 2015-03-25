package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class BitstampExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "USD"));
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
		return parseTicker(readJsonFromUrl("https://www.bitstamp.net/api/ticker/"), pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").getTextValue();
	}

	@Override
	protected List<Pair> getPairsFromAPI() {
		return pairs;
	}
}
