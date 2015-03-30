package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public final class CoinutExchange extends Exchange {

	public CoinutExchange(long expiredPeriod) {
		super("Coinut", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://coinut.com/api/assets");
		Iterator<JsonNode> elements = node.getElements();
		while(elements.hasNext()){
			String key = elements.next().asText();
			pairs.add(new Pair(key.substring(0, 3), key.substring(3, 6)));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://coinut.com/api/tick/" +
				pair.getCoin() + pair.getExchange());
		if(node.has("error")) {
			throw new IOException(node.get("error").asText());
		} else {
			return parseTicker(node, pair);
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("tick").asText();
	}
}