package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

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
		String url = "https://bitcoinapi.de/v1/e5bd463707931bca682879320ceb7516/trades.json";
		try {
			JsonNode node = (new ObjectMapper()).readTree(new URL(url));
			return parseJSON(node, pair);
		} catch(JsonParseException e) {
			throw new IOException("API call limit reached.");
		}
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(pair.getExchange().equals("EUR") && pair.getCoin().equals("BTC")) {
			Iterator<JsonNode> elements = node.getElements();
			if(elements.hasNext()) {
				JsonNode lastValueNode = elements.next();
				while(elements.hasNext()) {
					lastValueNode = elements.next();
				}
				return lastValueNode.get("price").toString();
			} else {
				throw new IOException("Empty trade history.");
			}
		} else {
			throw new IOException("Invalid pair.");
		}
	}
}
