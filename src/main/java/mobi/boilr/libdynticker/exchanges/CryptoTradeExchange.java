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
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public final class CryptoTradeExchange extends Exchange {

	public CryptoTradeExchange(long expiredPeriod) {
		super("Crypto-Trade", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
			IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = (new ObjectMapper()).readTree(new URL("https://crypto-trade.com/api/1/getpairs"));
		if(node.get("status").getTextValue().equals("success")) {
			node = node.get("data");
			JsonNode currencyPairs = node.get("currency_pairs");
			if(currencyPairs.isArray()) {
				Iterator<JsonNode> elements = currencyPairs.getElements();
				Iterator<String> fieldNames;
				for(String[] pairSplit; elements.hasNext();) {
					fieldNames = elements.next().getFieldNames();
					pairSplit = fieldNames.next().toUpperCase().split("_");
					pairs.add(new Pair(pairSplit[0], pairSplit[1]));
				}
			}
			JsonNode securityPairs = node.get("security_pairs");
			if(securityPairs.isArray()) {
				TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {
				};
				List<String> symbols = (new ObjectMapper()).readValue(securityPairs, typeRef);
				String[] pairSplit;
				for(String sym : symbols) {
					pairSplit = sym.toUpperCase().split("_");
					pairs.add(new Pair(pairSplit[0], pairSplit[1]));
				}
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
			IOException {
		// https://crypto-trade.com/api/1/ticker/btc_usd
		String url = "https://crypto-trade.com/api/1/ticker/" + pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase();
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		if(node.get("status").getTextValue().equals("error"))
			throw new MalformedURLException(node.get("error").getTextValue());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("data").get("last").getTextValue();
	}

}
