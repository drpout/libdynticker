package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

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
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
	IOException, NoMarketDataException {
		// https://api.allcoin.com/api/v1/ticker?symbol=LTC_USD
		JsonNode node = readJsonFromUrl("https://api.allcoin.com/api/v1/ticker?symbol=" +
				pair.getCoin() + "_" + pair.getExchange());
		if(node.has("error_code")) {
			throw new MalformedURLException(node.get("error_code").getTextValue());
		} else {
			return parseTicker(node, pair);
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("ticker").get("last").asText();
	}
}
