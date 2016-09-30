package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class IndacoinExchange extends Exchange {
	private static final String API_URL = "https://indacoin.com/api/ticker";

	public IndacoinExchange(long expiredPeriod) {
		super("Indacoin", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<Map.Entry<String,JsonNode>> elements = readJsonFromUrl(API_URL).getFields();
		String[] split;
		for(Map.Entry<String,JsonNode> entry; elements.hasNext();) {
			entry = elements.next();
			if(!entry.getValue().get("last_price").asText().isEmpty()) {
				split = entry.getKey().split("_");
				pairs.add(new Pair(split[0], split[1]));
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl(API_URL);
		if(node.has(pair.getCoin() + "_" + pair.getExchange()))
			return parseTicker(node, pair);
		else
			throw new IOException("Invalid pair: " + pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get(pair.getCoin() + "_" + pair.getExchange()).get("last_price").asText();
	}

}
