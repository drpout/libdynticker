package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BtcboxExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "JPY"));
		tempPairs.add(new Pair("LTC", "JPY"));
		tempPairs.add(new Pair("DOGE", "JPY"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BtcboxExchange(long expiredPeriod) {
		super("BTCBOX", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://www.btcbox.co.jp/coin/allcoin");
		if(node.has(pair.getCoin().toLowerCase()))
			return parseTicker(node, pair);
		else
			throw new MalformedURLException("Invalid pair: " + pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get(pair.getCoin().toLowerCase()).get(1).asText();
	}

}
