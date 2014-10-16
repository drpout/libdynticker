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

public class BTCEExchange extends Exchange {

	public BTCEExchange(long experiedPeriod) {
		super("BTC-E", experiedPeriod);
	}

	protected String getTickerURL(Pair pair) {
		return "https://btc-e.com/api/3/ticker/" + pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase();
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has(pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase())) {
			return node.get(pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase()).get("last").toString();
		} else {
			throw new IOException(node.get("error").getTextValue());
		}
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		return parseJSON(new ObjectMapper().readTree(new URL(this.getTickerURL(pair))), pair);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<String> elements = (new ObjectMapper()).readTree(new URL("https://btc-e.com/api/3/info")).get("pairs").getFieldNames();
		for(String[] split; elements.hasNext();) {
			split = elements.next().toUpperCase().split("_");
			pairs.add(new Pair(split[0], split[1]));
		}
		return pairs;
	}

}
