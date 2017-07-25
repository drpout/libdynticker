package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class SurBTCExchange extends Exchange {

	private static final String BASE_URL = "https://www.surbtc.com/api/v2/markets/";

	public SurBTCExchange(long expiredPeriod) {
		super("SURBTC", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl(BASE_URL);
		if(node.has("markets")) {
			for(JsonNode market : node.get("markets"))
				pairs.add(new Pair(market.get("base_currency").asText(), market.get("quote_currency").asText()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://www.surbtc.com/api/v2/markets/btc-clp/ticker
		JsonNode node = readJsonFromUrl(BASE_URL + pair.getCoin().toLowerCase() + "-" +
				pair.getExchange().toLowerCase() + "/ticker");
		if(node.has("ticker"))
			return parseTicker(node, pair);
		else
			throw new NoMarketDataException(pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("ticker").get("last_price").get(0).asText();
	}

}
