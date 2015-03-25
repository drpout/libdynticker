package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public class TBEExchange extends Exchange {

	public TBEExchange(long expiredPeriod) {
		super("Thailand Bitcoin Exchange", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://bx.in.th/api/pairing/");
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
			throw new IOException("Invalid market.");
		}
		JsonNode node = readJsonFromUrl("https://bx.in.th/api/");
		if(node.has(pair.getMarket())) {
			return parseTicker(node, pair);
		} else {
			throw new IOException("Market " + pair.getMarket() + " not found.");
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get(pair.getMarket()).get("last_price").asText();
	}
}
