package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public abstract class BTCTraderExchange extends Exchange {
	protected static List<Pair> PAIRS;
	protected String api;

	public BTCTraderExchange(String name, long expiredPeriod, String api) {
		super(name, expiredPeriod);
		this.api = api;
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(getPairs().contains(pair)) {
		String url = api + "api/ticker/";
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		return parseJSON(node, pair);
		} else {
			throw new IOException(pair + " not found");
		}
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}
}