package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BTCXIndiaExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "INR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BTCXIndiaExchange(long expiredPeriod) {
		super("BTCXIndia", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://api.btcxindia.com/ticker/");
		if(node.get("ticker").asText().equals(pair.getCoin() + "_" + pair.getExchange()))
			return parseTicker(node, pair);
		else
			throw new IOException("Invalid pair: " + pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last_traded_price").asText();
	}

}
