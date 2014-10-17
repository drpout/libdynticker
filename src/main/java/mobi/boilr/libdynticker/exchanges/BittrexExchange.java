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

public final class BittrexExchange extends Exchange {

	public BittrexExchange(long expiredPeriod) {
		super("Bittrex", expiredPeriod);
	}

	protected String getTickerURL(Pair pair) {
		return "https://bittrex.com/api/v1.1/public/getticker?market=" + pair.getExchange() + "-" + pair.getCoin();
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("result").get("Last").toString();
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
			IOException {
		JsonNode node = new ObjectMapper().readTree(new URL(this.getTickerURL(pair)));
		if(node.get("success").getBooleanValue()) {
			return parseJSON(node, pair);
		} else {
			throw new MalformedURLException(node.get("message").getTextValue());
		}
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
			IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<JsonNode> elements = (new ObjectMapper()).readTree(new URL("https://bittrex.com/api/v1.1/public/getmarkets")).get("result").getElements();
		JsonNode element;
		String coin, exchange;
		while(elements.hasNext()) {
			element = elements.next();
			if(element.get("IsActive").asBoolean()) {
				coin = element.get("MarketCurrency").getTextValue();
				exchange = element.get("BaseCurrency").getTextValue();
				pairs.add(new Pair(coin, exchange));
			}
		}
		return pairs;
	}
}
