package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class TheRockTradingExchange extends Exchange {

	public TheRockTradingExchange(long expiredPeriod) {
		super("The Rock Trading", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		String url = "https://www.therocktrading.com/api/tickers/currency";
		List<Pair> pairs = new ArrayList<Pair>();

		JsonNode node = new ObjectMapper().readTree(new URL(url));

		Iterator<String> elements = node.get("result").get("tickers").getFieldNames();

		while(elements.hasNext()) {
			String code = elements.next();
			pairs.add(new Pair(code.substring(0, 3), code.substring(3, 6)));
		}

		return Collections.unmodifiableList(pairs);
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://www.therocktrading.com/api/ticker/" + pair.getCoin() + pair.getExchange();
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has("result")){
			return node.get("result").getElements().next().get("last").getTextValue();
		} else {
			throw new IOException();
		}
	}
}