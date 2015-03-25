package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public abstract class BTCTraderExchange extends Exchange {
	protected static List<Pair> PAIRS;
	protected String domain;

	public BTCTraderExchange(String name, long expiredPeriod, String domain) {
		super(name, expiredPeriod);
		this.domain = domain;
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
		JsonNode node = readJsonFromUrl(domain + "api/ticker/");
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}
}