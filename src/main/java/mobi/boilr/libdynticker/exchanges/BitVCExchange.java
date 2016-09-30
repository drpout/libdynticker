package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BitVCExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC Futures (week)", "CNY"));
		tempPairs.add(new Pair("LTC Futures (week)", "CNY"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BitVCExchange(long expiredPeriod) {
		super("BitVC", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		String coin = pair.getCoin();
		String period = coin.split("[()]")[1].replace(" ", "_");
		// https://market.bitvc.com/futures/ticker_btc_week.js
		return parseTicker(readJsonFromUrl(
				"https://market.bitvc.com/futures/ticker_" + coin.substring(0, 3).toLowerCase() + "_" + period + ".js"),
				pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("last").asText();
	}
}
