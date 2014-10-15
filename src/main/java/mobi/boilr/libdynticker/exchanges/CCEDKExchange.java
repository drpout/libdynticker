package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public class CCEDKExchange extends Exchange {

	public CCEDKExchange(long experiedPeriod) {
		super("CCEDK", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
	IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		long currentSeconds = System.currentTimeMillis() / 1000;
		JsonNode node = (new ObjectMapper()).readTree(new URL("https://www.ccedk.com/api/v1/currency/list?nonce=" + currentSeconds));
		if(node.get("errors").isBoolean() && !node.get("errors").getBooleanValue()) {
			Map<String, String> currencies = new HashMap<String, String>();
			Iterator<JsonNode> elements = node.get("response").get("entities").getElements();
			for(JsonNode element; elements.hasNext();) {
				element = elements.next();
				currencies.put(element.get("currency_id").getTextValue(), element.get("iso").getTextValue());
			}
			node = (new ObjectMapper()).readTree(new URL("https://www.ccedk.com/api/v1/pair/list?nonce=" + currentSeconds));
			if(node.get("errors").isBoolean() && !node.get("errors").getBooleanValue()) {
				elements = node.get("response").get("entities").getElements();
				for(JsonNode element; elements.hasNext();) {
					element = elements.next();
					pairs.add(new Pair(currencies.get(element.get("currency_id_from").getTextValue()),
							currencies.get(element.get("currency_id_to").getTextValue()),
							element.get("pair_id").getTextValue()));
				}
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
			IOException {
		long currentSeconds = System.currentTimeMillis() / 1000;
		JsonNode node = (new ObjectMapper()).readTree(new URL("https://www.ccedk.com/api/v1/trade/list?nonce="
				+ currentSeconds + "&pair_id=" + pair.getMarket()));
		if(node.get("errors").isObject())
			throw new MalformedURLException(node.get("errors").toString());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		node = node.get("response").get("entities");
		if(node.isBoolean())
			throw new IOException("Data for " + pair + " is empty.");
		return node.get(0).get("price").getTextValue();
	}

}
