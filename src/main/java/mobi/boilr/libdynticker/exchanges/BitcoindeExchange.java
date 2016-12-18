package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.APICallLimitReachedException;

public final class BitcoindeExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "EUR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BitcoindeExchange(long expiredPeriod) {
		super("Bitcoin.de", expiredPeriod);

	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair)) {
			throw new IOException("Invalid pair: " + pair);
		}
		try {
			JsonNode node = readJsonFromUrl("https://bitcoinapi.de/v1/e5bd463707931bca682879320ceb7516/trades.json");
			return parseTicker(node, pair);
		} catch(JsonParseException e) {
			throw new APICallLimitReachedException();
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		Iterator<JsonNode> elements = node.getElements();
		if(elements.hasNext()) {
			JsonNode lastValueNode = elements.next();
			while(elements.hasNext()) {
				lastValueNode = elements.next();
			}
			return lastValueNode.get("price").asText();
		} else {
			throw new IOException("Empty trade history.");
		}
	}
}
