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

public class JubiExchange extends Exchange {

	public JubiExchange(long expiredPeriod) {
		super("Jubi", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		String url = "http://www.jubi.com/coin/allcoin";
		Iterator<String> elements = new ObjectMapper().readTree(new URL(url)).getFieldNames();
		List<Pair> pairs = new ArrayList<Pair>();

		while(elements.hasNext()) {
			String next = elements.next();
			pairs.add(new Pair(next.toUpperCase(), "CNY"));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "http://www.jubi.com/api/v1/ticker?coin=" + pair.getCoin().toLowerCase();
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has("result")) {
			throw new IOException("Pair not found");
		} else {
			return node.get("last").asText();
		}
	}

}
