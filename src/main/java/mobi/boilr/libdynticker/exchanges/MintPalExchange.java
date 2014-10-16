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

public class MintPalExchange extends Exchange {

	public MintPalExchange(long experiedPeriod) {
		super("MintPal", experiedPeriod);
	}

	protected String getTickerURL(Pair pair) {
		return "https://api.mintpal.com/v2/market/stats/" + pair.getCoin() + "/" + pair.getExchange();
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.get("status").getTextValue().equals("success")) {
			return node.get("data").get("last_price").getTextValue();
		} else {
			throw new IOException(node.get("message").getTextValue());
		}
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		return parseJSON(new ObjectMapper().readTree(new URL(this.getTickerURL(pair))), pair);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		JsonNode node = (new ObjectMapper()).readTree(new URL("https://api.mintpal.com/v2/market/summary/"));
		if(node.get("status").getTextValue().equals("success")) {
			List<Pair> pairs = new ArrayList<Pair>();
			Iterator<JsonNode> elements = node.get("data").getElements();
			String coin, exchange;
			for(JsonNode element; elements.hasNext();) {
				element = elements.next();
				coin = element.get("code").getTextValue();
				exchange = element.get("exchange").getTextValue();
				pairs.add(new Pair(coin, exchange));
			}
			return pairs;
		} else {
			throw new IOException(node.get("message").getTextValue());
		}
	}
}
