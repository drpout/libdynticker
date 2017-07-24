package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class BitMEXExchange extends Exchange {
	private static final String API_URL = "https://www.bitmex.com/api/v1";

	public BitMEXExchange(long expiredPeriod) {
		super("BitMEX", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl(API_URL + "/instrument/active");
		if(node.has("error"))
			throw new IOException(node.get("error").get("message").asText());
		else {
			String typ, coin, exchange, market;
			for(JsonNode inst : node) {
				typ = inst.get("typ").asText();
				coin = inst.get("underlying").asText();
				exchange = inst.get("quoteCurrency").asText();
				market = inst.get("symbol").asText();
				if(typ.equals("FFICSX")) {
					coin = inst.get("rootSymbol").asText();
					exchange = inst.get("underlying").asText();
				} else if(typ.equals("FFCCSX") && exchange.equals("USD")) {
					exchange = market.substring(3);
				}
				pairs.add(new Pair(coin, exchange, market));
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl(API_URL + "/instrument?symbol=" + pair.getMarket() + "&columns=lastPrice");
		if(node.size() != 1)
			throw new NoMarketDataException(pair);
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get(0).get("lastPrice").asText();
	}
}
