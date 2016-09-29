package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class KorbitExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "KRW"));
		tempPairs.add(new Pair("ETH", "KRW"));
		tempPairs.add(new Pair("ETC", "KRW"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public KorbitExchange(long expiredPeriod) {
		super("Korbit", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		// https://api.korbit.co.kr/v1/ticker?currency_pair=btc_krw
		return parseTicker(readJsonFromUrl("https://api.korbit.co.kr/v1/ticker?currency_pair="
				+ pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase()), pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}

}
