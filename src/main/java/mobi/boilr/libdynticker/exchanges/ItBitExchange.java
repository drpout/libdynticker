package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class ItBitExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("XBT", "USD"));
		tempPairs.add(new Pair("XBT", "SGD"));
		tempPairs.add(new Pair("XBT", "EUR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public ItBitExchange(long expiredPeriod) {
		super("itBit", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.itbit.com/v1/markets/XBTUSD/ticker
		JsonNode node = readJsonFromUrl("https://api.itbit.com/v1/markets/" +
				pair.getCoin() + pair.getExchange() + "/ticker");
		if(node.has("error"))
			throw new IOException(node.get("error").get("message").asText());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("lastPrice").asText();
	}

}
