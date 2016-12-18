package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class DSXExchange extends Exchange {
	private static final List<Pair> PAIRS;
	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC","USD"));
		pairs.add(new Pair("BTC","EUR"));
		pairs.add(new Pair("LTC","BTC"));
		pairs.add(new Pair("LTC","USD"));
		pairs.add(new Pair("LTC","EUR"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public DSXExchange(long expiredPeriod) {
		super("Digital Securities Exchange", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		 return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String pairCode = pair.getCoin().toLowerCase() + pair.getExchange().toLowerCase();
		JsonNode node = readJsonFromUrl("https://dsx.uk/mapi/ticker/" + pairCode);
		if(node.has(pairCode))
			return parseTicker(node, pair);
		else
			throw new NoMarketDataException(pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get(pair.getCoin().toLowerCase() +
				pair.getExchange().toLowerCase()).get("last").asText();
	}

}
