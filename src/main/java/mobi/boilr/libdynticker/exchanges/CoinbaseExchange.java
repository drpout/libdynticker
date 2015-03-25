package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class CoinbaseExchange extends Exchange {

	public CoinbaseExchange(long expiredPeriod) {
		super("Coinbase", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://api.exchange.coinbase.com/products");
		if(node.has("message"))
			throw new MalformedURLException(node.get("message").getTextValue());
		Iterator<JsonNode> elements = node.getElements();
		for(JsonNode product; elements.hasNext();) {
			product = elements.next();
			pairs.add(new Pair(product.get("base_currency").getTextValue(), product.get("quote_currency").getTextValue()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.exchange.coinbase.com/products/BTC-USD/ticker
		JsonNode node = readJsonFromUrl("https://api.exchange.coinbase.com/products/" +
				pair.getCoin() + "-" + pair.getExchange() + "/ticker");
		if(node.has("message"))
			throw new MalformedURLException(node.get("message").getTextValue());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("price").getTextValue();
	}

}
