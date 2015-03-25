package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public class BitKonanExchange extends Exchange {
	private static final List<Pair> PAIRS;
	private static final Pair LTCUSD = new Pair("LTC", "BTC");
	private static final Pair BTCUSD = new Pair("BTC", "USD");

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(LTCUSD);
		pairs.add(BTCUSD);
		PAIRS = Collections.unmodifiableList(pairs);
	}
	
	public BitKonanExchange(long expiredPeriod) {
		super("BitKonan", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://bitkonan.com/api/ticker
		String url = "https://bitkonan.com/api/";
		if(pair.equals(BTCUSD)) {
			url += "ticker";
		} else if(pair.equals(LTCUSD)) {
			url += "ltc_ticker";
		} else {
			throw new IOException("Invalid pair: " + pair);
		}
		JsonNode node = readJsonFromUrl(url);
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}

}
