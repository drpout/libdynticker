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

public class CoinutExchange extends Exchange {

	public CoinutExchange(long expiredPeriod) {
		super("Coinut", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		String url = "https://coinut.com/api/assets";
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		Iterator<JsonNode> elements = node.getElements();
		while(elements.hasNext()){
			String key = elements.next().asText();
			pairs.add(new Pair(key.substring(0, 3), key.substring(3, 6)));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://coinut.com/api/tick/" + pair.getCoin() + pair.getExchange();
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has("error")) {
			throw new IOException(node.get("error").asText());
		} else {
			return node.get("tick").asText();
		}
	}
}