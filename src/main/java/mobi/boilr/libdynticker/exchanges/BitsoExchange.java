package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class BitsoExchange extends Exchange {

	private static final List<Pair> PAIRS;;

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "MXN"));
		PAIRS = Collections.unmodifiableList(pairs);
	}
	
	public BitsoExchange(long expiredPeriod) {
		super("Bitso", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.bitso.com/v2/ticker?book=btc_mxn
		JsonNode node = readJsonFromUrl("https://api.bitso.com/v2/ticker?book=" +
				pair.getCoin() + "_" + pair.getExchange());
		if(node.has("error"))
			throw new IOException(node.get("error").get("message").getTextValue());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}
}
