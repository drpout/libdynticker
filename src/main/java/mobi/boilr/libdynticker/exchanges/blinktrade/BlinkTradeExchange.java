package mobi.boilr.libdynticker.exchanges.blinktrade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public abstract class BlinkTradeExchange extends Exchange {
	private List<Pair> pairs;

	public BlinkTradeExchange(String name, long expiredPeriod, String quoteCurrency) {
		super(name, expiredPeriod);
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", quoteCurrency));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		// https://api.blinktrade.com/api/v1/VEF/ticker?crypto_currency=BTC
		JsonNode node = readJsonFromUrl("https://api.blinktrade.com/api/v1/" + pair.getExchange()
				+ "/ticker?crypto_currency=" + pair.getCoin());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("last").asText();
	}

}
