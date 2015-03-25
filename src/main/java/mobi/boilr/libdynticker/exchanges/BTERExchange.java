package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class BTERExchange extends Exchange {

	public BTERExchange(long expiredPeriod) {
		super("Bter", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<JsonNode> elements = readJsonFromUrl("http://data.bter.com/api/1/pairs").getElements();
		for(String[] split; elements.hasNext();) {
			split = elements.next().getTextValue().toUpperCase().split("_");
			pairs.add(new Pair(split[0], split[1]));
		}
		return pairs;
	}
	
	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("http://data.bter.com/api/1/ticker/" +
				pair.getCoin() + "_" + pair.getExchange());
		if(node.get("result").getTextValue().equals("true")) {
			return parseTicker(node, pair);
		} else {
			throw new IOException(node.get("message").getTextValue());
		}
	}


	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		JsonNode lastValueNode = node.get("last");
		if(lastValueNode.isTextual())
			return lastValueNode.getTextValue();
		else
			return lastValueNode.asText();
	}
}
