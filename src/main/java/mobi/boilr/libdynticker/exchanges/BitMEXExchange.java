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
		JsonNode node = readJsonFromUrl(API_URL + "/instrument/active?columns=underlying,quoteCurrency");
		if(node.has("error"))
			throw new IOException(node.get("error").get("message").asText());
		else {
			for(JsonNode inst : node) {
				pairs.add(new Pair(inst.get("underlying").asText(), inst.get("quoteCurrency").asText(),
						inst.get("symbol").asText()));
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
