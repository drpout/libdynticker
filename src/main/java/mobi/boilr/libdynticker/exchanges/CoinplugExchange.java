package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class CoinplugExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "KRW"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public CoinplugExchange(long expiredPeriod) {
		super("Coinplug", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		JsonNode node = readJsonFromUrl("https://www.coinplug.com/web/open/spot_rate");
		if(node.get("code").asText().equals("1"))
			return parseTicker(node, pair);
		else
			throw new IOException(node.get("message").asText());
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("data").asText();
	}

}
