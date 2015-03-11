package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class CoinsetterExchange extends Exchange {
	private final static List<Pair> PAIRS;

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "USD"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public CoinsetterExchange(long expiredPeriod) {
		super("Coinsetter", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://api.coinsetter.com/v1/marketdata/last";
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(PAIRS.contains(pair)) {
			return node.get("last").getElements().next().get("price").asText();
		} else {
			throw new IOException();
		}
	}
}
