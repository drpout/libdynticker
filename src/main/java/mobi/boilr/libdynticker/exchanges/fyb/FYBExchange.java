package mobi.boilr.libdynticker.exchanges.fyb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public abstract class FYBExchange extends Exchange {
	private String domain;
	private List<Pair> pairs;
	private int tid = 0;

	public FYBExchange(String name, long expiredPeriod, String domain, String quoteCurrency) {
		super(name, expiredPeriod);
		this.domain = domain;
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", quoteCurrency));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		// https://domain.xxx/api/XXX/trades.json?since=tid
		return parseTicker(
				readJsonFromUrl(
						"https://" + domain + "/api/" + pairs.get(0).getExchange() + "/trades.json?since=" + tid),
				pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		JsonNode ticker = node.get(node.size() - 1);
		tid = ticker.get("tid").asInt(0) - 1;
		return ticker.get("price").asText();
	}

}
