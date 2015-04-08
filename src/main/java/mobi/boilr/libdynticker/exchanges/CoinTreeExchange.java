package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class CoinTreeExchange extends Exchange {

	private static final List<Pair> PAIRS;

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "AUD"));
		PAIRS = Collections.unmodifiableList(pairs);
	}
	
	public CoinTreeExchange(long expiredPeriod) {
		super("CoinTree", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!PAIRS.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		JsonNode node = readJsonFromUrl("https://www.cointree.com.au/api/price/" +
				pair.getCoin() + "/" + pair.getExchange());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("Spot").asText();
	}
}