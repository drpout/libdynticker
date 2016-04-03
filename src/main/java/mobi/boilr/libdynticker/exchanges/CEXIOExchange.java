package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class CEXIOExchange extends Exchange {
	private static final String markets;
	
	static {
		final String[] marketList = {"USD", "EUR", "RUB", "BTC", "LTC"};
		String tempMarkets = "";
		for(String market : marketList){
			tempMarkets += market + "/";
		}
		markets = tempMarkets;
	}

	public CEXIOExchange(long expiredPeriod) {
		super("CEX.IO", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		// TODO check if https://cex.io/api/currency_limit is available
		// https://cex.io/api/currency_limit is specified in the api, but not responding
		// this is a better alternative when it is working, searching for markets is unreliable
		JsonNode node = readJsonFromUrl("https://cex.io/api/tickers/" + markets);
		if(node.get("ok").asText().equals("ok")){
			List<Pair> pairs = new ArrayList<Pair>();
			for(JsonNode n : node.get("data")){
				String[] split = n.get("pair").asText().split(":");
				pairs.add(new Pair(split[0], split[1]));
			}
			return pairs;
		} else {
			throw new MalformedURLException(node.get("error").asText());
		}
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://cex.io/api/last_price/"
				+ pair.getCoin() + "/" + pair.getExchange());
		if(node.has("error"))
			throw new MalformedURLException(node.get("error").getTextValue());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("lprice").getTextValue();
	}

}
