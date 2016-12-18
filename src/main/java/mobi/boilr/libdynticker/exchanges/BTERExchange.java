package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BTERExchange extends Exchange {

	public BTERExchange(long expiredPeriod) {
		super("Bter", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		String[] split;
		for(JsonNode pair : readJsonFromUrl("http://data.bter.com/api/1/pairs")) {
			split = pair.asText().toUpperCase().split("_");
			pairs.add(new Pair(split[0], split[1]));
		}
		return pairs;
	}
	
	@Override
	protected String getTicker(Pair pair) throws IOException {
		// http://data.bter.com/api/1/ticker/LTC_BTC
		JsonNode node = readJsonFromUrl("http://data.bter.com/api/1/ticker/" +
				pair.getCoin() + "_" + pair.getExchange());
		if(node.get("result").asText().equals("true")) {
			return parseTicker(node, pair);
		} else {
			throw new IOException(node.get("message").asText());
		}
	}


	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}
}
