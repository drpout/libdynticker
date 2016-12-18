package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BitfinexExchange extends Exchange {

	public BitfinexExchange(long expiredPeriod) {
		super("Bitfinex", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
		List<String> symbols = (new ObjectMapper()).readValue(readJsonFromUrl("https://api.bitfinex.com/v1/symbols"), typeRef);
		String coin, exchange;
		for(String sym : symbols) {
			sym = sym.toUpperCase();
			coin = sym.substring(0, 3);
			exchange = sym.substring(3, 6);
			pairs.add(new Pair(coin, exchange));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.bitfinex.com/v1/pubticker/BTCUSD
		JsonNode node = readJsonFromUrl("https://api.bitfinex.com/v1/pubticker/" +
				pair.getCoin() + pair.getExchange());
		if(node.has("message"))
			throw new IOException(node.get("message").asText());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("last_price").asText();
	}

}
