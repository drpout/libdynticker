package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class QuoineExchange extends Exchange {

	public QuoineExchange(long expiredPeriod) {
		super("Quoine", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://api.quoine.com/products");
		for(JsonNode product : node)
			pairs.add(new Pair(product.get("base_currency").asText(), product.get("quoted_currency").asText()));
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.quoine.com/products/code/CASH/BTCUSD
		JsonNode node = readJsonFromUrl("https://api.quoine.com/products/code/CASH/" + pair.getCoin() + pair.getExchange());
		if(node.has("last_traded_price"))
			return parseTicker(node, pair);
		else
			throw new NoMarketDataException(pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last_traded_price").asText();
	}

}
