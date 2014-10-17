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

public final class BitstampExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "USD"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BitstampExchange(long expiredPeriod) {
		super("Bitstamp", expiredPeriod);
	}

	protected String getTickerURL(Pair pair) {
		return "https://www.bitstamp.net/api/ticker/";
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(pairs.contains(pair)) {
			return node.get("last").getTextValue();
		} else {
			throw new IOException(pair + " is invalid.");
		}
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		return parseJSON(new ObjectMapper().readTree(new URL(this.getTickerURL(pair))), pair);
	}

	@Override
	protected List<Pair> getPairsFromAPI() {
		return pairs;
	}
}
