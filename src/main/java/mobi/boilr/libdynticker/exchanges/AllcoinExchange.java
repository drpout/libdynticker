package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class AllcoinExchange extends Exchange {

	public AllcoinExchange(long expiredPeriod) {
		super("Allcoin", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		for(JsonNode market : readJsonFromUrl("https://www.allcoin.com/marketoverviews/").get("Markets")) {
			pairs.add(new Pair(market.get("Primary").asText(), market.get("Secondary").asText()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.allcoin.com/api/v1/ticker?symbol=LTC_USD
		JsonNode node = readJsonFromUrl("https://api.allcoin.com/api/v1/ticker?symbol=" +
				pair.getCoin() + "_" + pair.getExchange());
		if(node.has("error_code")) {
			throw new IOException(node.get("error_code").asText());
		} else {
			return parseTicker(node, pair);
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("ticker").get("last").asText();
	}
}
