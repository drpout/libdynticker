package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class PoloniexExchange extends Exchange {
	private static final String API_URL = "https://poloniex.com/public?command=returnTicker";

	public PoloniexExchange(long expiredPeriod) {
		super("Poloniex", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<String> elements = readJsonFromUrl(API_URL).getFieldNames();
		for(String[] split; elements.hasNext();) {
			split = elements.next().split("_");
			pairs.add(new Pair(split[1], split[0]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl(API_URL);
		if(node.has(pair.getExchange() + "_" + pair.getCoin())) {
			return parseTicker(node, pair);
		} else {
			throw new NoMarketDataException(pair);
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get(pair.getExchange() + "_" + pair.getCoin()).get("last").asText();
	}

}
