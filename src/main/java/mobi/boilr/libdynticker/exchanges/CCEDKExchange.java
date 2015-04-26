package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;

public final class CCEDKExchange extends Exchange {

	public CCEDKExchange(long expiredPeriod) {
		super("CCEDK", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
	IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://www.ccedk.com/api/v1/currency/list");
		if(node.get("errors").isBoolean() && !node.get("errors").getBooleanValue()) {
			Map<String, String> currencies = new HashMap<String, String>();
			Iterator<JsonNode> elements = node.get("response").get("entities").getElements();
			for(JsonNode element; elements.hasNext();) {
				element = elements.next();
				currencies.put(element.get("currency_id").asText(), element.get("iso").asText());
			}
			node = readJsonFromUrl("https://www.ccedk.com/api/v1/pair/list");
			if(node.get("errors").isBoolean() && !node.get("errors").getBooleanValue()) {
				elements = node.get("response").get("entities").getElements();
				for(JsonNode element; elements.hasNext();) {
					element = elements.next();
					pairs.add(new Pair(currencies.get(element.get("currency_id_from").asText()), currencies.get(element.get("currency_id_to")
							.asText()), element.get("pair_id").asText()));
				}
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
 IOException, NoMarketDataException {
		JsonNode node = readJsonFromUrl("https://www.ccedk.com/api/v1/trade/list?pair_id=" + pair.getMarket());
		if(node.get("errors").isObject())
			throw new MalformedURLException(node.get("errors").toString());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException, NoMarketDataException {
		Iterator<JsonNode> elements = node.get("response").get("entities").getElements();
		if(elements.hasNext())
			return elements.next().get("price").asText();
		else
			throw new NoMarketDataException(pair);
	}

}