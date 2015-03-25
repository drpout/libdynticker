package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public class CoinsetterExchange extends Exchange {
	private final static List<Pair> PAIRS;

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "USD"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public CoinsetterExchange(long expiredPeriod) {
		super("Coinsetter", expiredPeriod);
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
		JsonNode node = readJsonFromUrl("https://api.coinsetter.com/v1/marketdata/last");
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").getElements().next().get("price").asText();
	}
}
