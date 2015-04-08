package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class TheRockTradingExchange extends Exchange {

	public TheRockTradingExchange(long expiredPeriod) {
		super("The Rock Trading", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://www.therocktrading.com/api/tickers/currency");
		Iterator<String> elements = node.get("result").get("tickers").getFieldNames();
		while(elements.hasNext()) {
			String code = elements.next();
			pairs.add(new Pair(code.substring(0, 3), code.substring(3, 6)));
		}
		return Collections.unmodifiableList(pairs);
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://www.therocktrading.com/api/ticker/" +
				pair.getCoin() + pair.getExchange());
		if(node.has("result")){
			return parseTicker(node, pair);
		} else {
			throw new IOException();
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("result").getElements().next().get("last").getTextValue();
	}
}