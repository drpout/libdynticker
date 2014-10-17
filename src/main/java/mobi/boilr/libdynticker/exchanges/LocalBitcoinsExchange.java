package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public final class LocalBitcoinsExchange extends Exchange {

	public LocalBitcoinsExchange(long expiredPeriod) {
		super("LocalBitcoins", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		String url = "https://localbitcoins.com/bitcoinaverage/ticker-all-currencies/";
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
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
		String url = "https://localbitcoins.com/bitcoinaverage/ticker-all-currencies/";
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has(pair.getExchange().toUpperCase())) {
			return node.get(pair.getExchange()).get("rates").get("last").getTextValue();
		} else {
			throw new IOException();
		}
	}
}
