package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class CoinMateExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "EUR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public CoinMateExchange(long expiredPeriod) {
		super("CoinMate", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://coinmate.io/api/ticker?currencyPair=BTC_USD
		JsonNode node = readJsonFromUrl("https://coinmate.io/api/ticker?currencyPair="
			+ pair.getCoin() + "_" + pair.getExchange());
		if(node.get("error").getBooleanValue())
			throw new IOException(node.get("errorMessage").asText());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("data").get("last").asText();
	}

}
