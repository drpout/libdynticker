package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class BittrexExchange extends Exchange {

	public BittrexExchange(long expiredPeriod) {
		super("Bittrex", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://bittrex.com/api/v1.1/public/getmarkets");
		if(node.get("success").asBoolean()) {
			for(JsonNode market : node.get("result")) {
				if(market.get("IsActive").asBoolean()) {
					pairs.add(new Pair(market.get("MarketCurrency").asText(), market.get("BaseCurrency").asText()));
				}
			}
		} else
			throw new IOException(node.get("message").asText());
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://bittrex.com/api/v1.1/public/getticker?market=" +
				pair.getExchange() + "-" + pair.getCoin());
		if(node.get("success").asBoolean()) {
			if(node.get("result").asText().equals("null"))
				throw new NoMarketDataException(pair);
			else
				return parseTicker(node, pair);
		} else
			throw new IOException(node.get("message").asText());
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		String last = node.get("result").get("Last").asText();
		if(last.equals("null"))
			throw new NoMarketDataException(pair);
		return last;
	}
}