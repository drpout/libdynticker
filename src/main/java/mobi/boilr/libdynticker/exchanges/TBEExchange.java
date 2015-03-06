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

public class TBEExchange extends Exchange {

	public TBEExchange(long expiredPeriod) {
		super("Thailand Bitcoin Exchange", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		String url = "https://bx.in.th/api/pairing/";
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		Iterator<String> fieldNames = node.getFieldNames();
		
		while(fieldNames.hasNext()) {
			String id = fieldNames.next();
			pairs.add(new Pair(node.get(id).get("secondary_currency").asText(), 
					node.get(id).get("primary_currency").asText(), id));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(pair.getMarket() == null) {
			throw new IOException("Invalid Market.");
		}
		String url = "https://bx.in.th/api/";
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has(pair.getMarket())) {
			return node.get(pair.getMarket()).get("last_price").asText();
		} else {
			throw new IOException("Market " + pair.getMarket() + " not found.");
		}
	}
}
