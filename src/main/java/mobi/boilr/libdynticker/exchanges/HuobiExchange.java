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

public class HuobiExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "CNY"));
		tempPairs.add(new Pair("LTC", "CNY"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public HuobiExchange(long expiredPeriod) {
		super("Huobi", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair.");
		String url = "http://market.huobi.com/staticmarket/ticker_" + pair.getCoin().toLowerCase() + "_json.js";
		return this.parseJSON((new ObjectMapper()).readTree(new URL(url)), pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("ticker").get("last").getTextValue();
	}

}
