package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class GatecoinExchange extends Exchange {

	public GatecoinExchange(long expiredPeriod) {
		super("Gatecoin", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://api.gatecoin.com/Public/LiveTickers");
		if(node.get("responseStatus").get("message").asText().equals("OK")) {
			for(JsonNode ticker : node.get("tickers")) {
				String currPair = ticker.get("currencyPair").asText();
				pairs.add(new Pair(currPair.substring(0, 3), currPair.substring(3, 6)));
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.gatecoin.com/Public/Transactions/BTCEUR?count=1
		JsonNode node = readJsonFromUrl("https://api.gatecoin.com/Public/Transactions/" + pair.getCoin() + pair.getExchange() + "?count=1");
		String message = node.get("responseStatus").get("message").asText();
		if(message.equals("OK"))
			return parseTicker(node, pair);
		else
			throw new IOException(message);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("transactions").get(0).get("price").asText();
	}

}
