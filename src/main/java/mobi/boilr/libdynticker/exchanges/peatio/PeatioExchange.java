package mobi.boilr.libdynticker.exchanges.peatio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public abstract class PeatioExchange extends Exchange {
	private String domain;

	public PeatioExchange(String name, long expiredPeriod, String domain) {
		super(name, expiredPeriod);
		this.domain = domain;
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://" + domain + "/api/v2/markets.json");
		String[] pairSplit;
		for(JsonNode pair : node) {
			pairSplit = pair.get("name").asText().split("/");
			pairs.add(new Pair(pairSplit[0], pairSplit[1]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://domain.xxx/api/v2/tickers/dogcny.json
		JsonNode node = readJsonFromUrl("https://" + domain + "/api/v2/tickers/" +
				pair.getCoin().toLowerCase() + pair.getExchange().toLowerCase() + ".json");
		if(node.has("error"))
			throw new IOException(node.get("error").get("message").asText());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("ticker").get("last").asText();
	}

}
