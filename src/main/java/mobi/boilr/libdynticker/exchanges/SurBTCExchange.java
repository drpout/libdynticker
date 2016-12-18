package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class SurBTCExchange extends Exchange {

	public SurBTCExchange(long expiredPeriod) {
		super("SURBTC", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://www.surbtc.com/api/v1/markets");
		if(node.has("markets")) {
			for(JsonNode market : node.get("markets"))
				pairs.add(new Pair(market.get("base_currency").asText(), market.get("quote_currency").asText()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://www.surbtc.com/api/v1/markets/btc-clp/indicators
		JsonNode node = readJsonFromUrl("https://www.surbtc.com/api/v1/markets/" + 
				pair.getCoin().toLowerCase() + "-" + pair.getExchange().toLowerCase() + "/indicators");
		if(node.has("indicators"))
			return parseTicker(node, pair);
		else
			throw new NoMarketDataException(pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		for(JsonNode indicator : node.get("indicators")) {
			if(indicator.get("key").asText().equals("last_price")) {
				String value = indicator.get("value").asText();
				int len = value.length();
				return value.substring(0, len - 2) + "." + value.substring(len - 2, len);
			}
		}
		throw new NoMarketDataException(pair);
	}

}
