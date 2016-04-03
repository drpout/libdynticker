package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class QuadrigaCXExchange extends Exchange {
	public final static List<Pair> PAIRS;

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "CAD"));
		pairs.add(new Pair("BTC", "USD"));
		pairs.add(new Pair("ETH", "BTC"));
		pairs.add(new Pair("ETH", "CAD"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public QuadrigaCXExchange(long expiredPeriod) {
		super("Quadriga CX", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://api.quadrigacx.com/v2/ticker?book=" +
				pair.getCoin() + "_" + pair.getExchange());
		if(node.has("error")) {
			throw new IOException(node.get("error").get("message").asText());
		} else {
			return parseTicker(node, pair);
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}

}
