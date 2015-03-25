package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class BasebitExchange extends Exchange {

	public BasebitExchange(long expiredPeriod) {
		super("Basebit", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<JsonNode> elements = readJsonFromUrl("http://www.basebit.com.br/listpairs").getElements();
		while(elements.hasNext()) {
			JsonNode next = elements.next();
			String[] split = next.asText().split("_");
			pairs.add(new Pair(split[0], split[1]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("http://www.basebit.com.br/quote-" +
				pair.getCoin() + "_" + pair.getExchange());
		if(node.get("result").asText().equals("null")) {
			throw new MalformedURLException(node.get("errorMessage").getTextValue());
		}
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("result").get("last").asText();
	}
}