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

public final class BtcTradeExchange extends Exchange {
	private static final List<Pair> PAIRS;
	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "CNY"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public BtcTradeExchange(long expiredPeriod) {
		super("BtcTrade", expiredPeriod);

	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = new ObjectMapper().readTree(new URL(
				"https://www.btctrade.com/api/ticker/"));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if (PAIRS.contains(pair))
			return node.get("last").toString();
		else
			throw new IOException(pair + " is invalid.");
	}
}
