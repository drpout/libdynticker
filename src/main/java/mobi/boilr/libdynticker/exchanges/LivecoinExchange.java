package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class LivecoinExchange extends Exchange {

	private static final String API_URL = "https://api.livecoin.net/exchange/ticker";

	public LivecoinExchange(long expiredPeriod) {
		super("Livecoin", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl(API_URL);
		if(node.has("success"))
			throw new IOException(node.get("errorMessage").asText());
		else {
			for(JsonNode ticker : node) {
				String[] split = ticker.get("symbol").asText().split("/");
				pairs.add(new Pair(split[0], split[1]));
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.livecoin.net/exchange/ticker?currencyPair=BTC/USD
		JsonNode node = readJsonFromUrl(API_URL + "?currencyPair=" + pair.getCoin() + "/" + pair.getExchange());
		if(node.has("success"))
			throw new IOException(node.get("errorMessage").asText());
		else
			return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last").asText();
	}

}
