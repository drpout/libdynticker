package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public class JubiExchange extends Exchange {

	public JubiExchange(long expiredPeriod) {
		super("Jubi", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		Iterator<String> elements = readJsonFromUrl("http://www.jubi.com/coin/allcoin")
				.getFieldNames();
		List<Pair> pairs = new ArrayList<Pair>();
		while(elements.hasNext()) {
			String next = elements.next();
			pairs.add(new Pair(next.toUpperCase(), "CNY"));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("http://www.jubi.com/api/v1/ticker?coin=" +
				pair.getCoin().toLowerCase());
		if(node.has("result")) {
			throw new MalformedURLException("Invalid pair: " + pair);
		} else {
			return parseTicker(node, pair);
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}

}
