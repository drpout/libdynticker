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

public final class PaymiumExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "EUR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}
	
	public PaymiumExchange(long expiredPeriod) {
		super("Paymium", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC","EUR"));
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String addr = "https://paymium.com/api/v1/data/eur/ticker";
		JsonNode node = new ObjectMapper().readTree(new URL(addr));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(pairs.contains(pair))
			return node.get("price").asText();
		else
			throw new IOException(pair + " is  invalid.");
	}
}
