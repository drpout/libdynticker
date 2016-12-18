package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class LocalBitcoinsExchange extends Exchange {

	public LocalBitcoinsExchange(long expiredPeriod) {
		super("LocalBitcoins", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		JsonNode node = readJsonFromUrl("https://localbitcoins.com/bitcoinaverage/ticker-all-currencies/");
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<String> exchanges = node.getFieldNames();
		while(exchanges.hasNext()) {
			// LocalBitcoins only deals with BTC to others currencies
			pairs.add(new Pair("BTC", exchanges.next()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://localbitcoins.com/bitcoinaverage/ticker-all-currencies/");
		if(node.has(pair.getExchange().toUpperCase()))
			return parseTicker(node, pair);
		else
			throw new NoMarketDataException(pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get(pair.getExchange()).get("rates").get("last").asText();
	}
}
