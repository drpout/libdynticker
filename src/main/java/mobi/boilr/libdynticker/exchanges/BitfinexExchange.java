package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class BitfinexExchange extends Exchange {

	public BitfinexExchange(long expiredPeriod) {
		super("Bitfinex", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
			IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {
		};
		List<String> symbols = (new ObjectMapper()).readValue(new URL("https://api.bitfinex.com/v1/symbols"), typeRef);
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
	protected String getTicker(Pair pair) throws JsonProcessingException, IOException {
		// https://api.bitfinex.com/v1/pubticker/BTCUSD
		String address = "https://api.bitfinex.com/v1/pubticker/" + pair.getCoin() + pair.getExchange();
		JsonNode node = (new ObjectMapper()).readTree(new URL(address));
		if(node.has("message"))
			throw new MalformedURLException(node.get("message").getTextValue());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("last_price").getTextValue();
	}

}
