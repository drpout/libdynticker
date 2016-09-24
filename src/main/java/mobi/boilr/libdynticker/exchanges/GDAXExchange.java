package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class GDAXExchange extends Exchange {

	public GDAXExchange(long expiredPeriod) {
		super("GDAX", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://api.gdax.com/products");
		if(node.has("message"))
			throw new IOException(node.get("message").asText());
		else {
			for(JsonNode product : node) {
				pairs.add(new Pair(product.get("base_currency").asText(), product.get("quote_currency").asText()));
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.gdax.com/products/BTC-USD/ticker
		JsonNode node = readJsonFromUrl("https://api.gdax.com/products/" + pair.getCoin() +
				"-" + pair.getExchange() + "/ticker");
		if(node.has("message"))
			throw new IOException(node.get("message").asText());
		else
			return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("price").asText();
	}

}
