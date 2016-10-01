package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BitcoinToYouExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "BRL"));
		tempPairs.add(new Pair("LTC", "BRL"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BitcoinToYouExchange(long expiredPeriod) {
		super("BitcoinToYou", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		String url = "https://www.bitcointoyou.com/API/ticker";
		if(pair.getCoin().equals("LTC"))
			url += "_litecoin";
		url += ".aspx";
		return parseTicker(readJsonFromUrl(url), pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("ticker").get("last").asText();
	}

}
