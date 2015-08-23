package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
		JsonNode node = readJsonFromUrl("https://api.therocktrading.com/v1/funds");
		JsonNode elements = node.get("funds");
		for(JsonNode element : elements){
			String id = element.get("id").asText();
			String coin = element.get("trade_currency").asText();
			String exchange = element.get("base_currency").asText();
			pairs.add(new Pair(coin, exchange, id));
		}
		return Collections.unmodifiableList(pairs);
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://api.therocktrading.com/v1/funds/" + pair.getMarket()+"/ticker");
		if(node.has("last")){
			return parseTicker(node, pair);
		} else {
			throw new IOException();
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}
}