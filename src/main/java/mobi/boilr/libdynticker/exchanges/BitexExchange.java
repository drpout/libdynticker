package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class BitexExchange extends Exchange {

	private static final List<Pair> PAIRS;

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "USD"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public BitexExchange(long expiredPeriod) {
		super("Bitex", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!PAIRS.contains(pair)) {
			throw new IOException("Invalid pair: " + pair);
		}
		JsonNode node = readJsonFromUrl("https://bitex.la/api-v1/rest/btc/market/ticker");
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}
}