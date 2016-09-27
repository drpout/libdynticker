package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class CoinfloorExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("XBT", "GBP"));
		tempPairs.add(new Pair("XBT", "EUR"));
		tempPairs.add(new Pair("XBT", "PLN"));
		tempPairs.add(new Pair("XBT", "USD"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public CoinfloorExchange(long expiredPeriod) {
		super("Coinfloor", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://webapi.coinfloor.co.uk:8090/bist/XBT/GBP/ticker/
		JsonNode node = readJsonFromUrl(
				"https://webapi.coinfloor.co.uk:8090/bist/" + pair.getCoin() + "/" + pair.getExchange() + "/ticker/");
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}

}
