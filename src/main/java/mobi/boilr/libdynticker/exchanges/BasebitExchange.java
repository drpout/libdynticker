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

public final class BasebitExchange extends Exchange {

	public BasebitExchange(long expiredPeriod) {
		super("Basebit", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<JsonNode> elements = new ObjectMapper().readTree(new URL("http://www.basebit.com.br/listpairs")).getElements();
		while(elements.hasNext()) {
			JsonNode next = elements.next();
			String[] split = next.asText().split("_");
			pairs.add(new Pair(split[0], split[1]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "http://www.basebit.com.br/quote-" + pair.getCoin() + "_" + pair.getExchange();
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.get("errorMessage").asText().equals("null")) {
			return node.get("result").get("last").asText();
		} else {
			throw new IOException(pair + " not found");
		}
	}
}