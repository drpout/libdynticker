package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class CleverCoinExchange extends Exchange {
	public static final List<Pair> pairs;
	
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC","EUR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}
	
	public CleverCoinExchange( long expiredPeriod) {
		super("CleverCoin", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException,
			NoMarketDataException {
		if(pairs.contains(pair)){
			JsonNode node = readJsonFromUrl("https://api.clevercoin.com/v1/ticker");
			return parseTicker(node, pair);
		} else
			throw new IOException("Invalid pair: " + pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException,
			NoMarketDataException {
		return node.get("last").asText();
	}

}
