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
		String url = null;
		if(pair.equals(BTCUSD)) {
			url="https://bitkonan.com/api/ticker";
		} else if(pair.equals(LTCUSD)) {
			url="https://bitkonan.com/api/ltc_ticker";
		}
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}

}
