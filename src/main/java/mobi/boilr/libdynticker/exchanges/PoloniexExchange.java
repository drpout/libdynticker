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

public class PoloniexExchange extends Exchange {

	public PoloniexExchange(long experiedPeriod) {
		super("Poloniex", experiedPeriod);
	}

	protected String getTickerURL(Pair pair) {
		return "https://poloniex.com/public?command=returnTicker";
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has(pair.getExchange().toUpperCase() + "_" + pair.getCoin().toUpperCase())) {
			return node.get(pair.getExchange() + "_" + pair.getCoin()).get("last").getTextValue();
		} else {
			throw new IOException();
		}
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		return parseJSON(new ObjectMapper().readTree(new URL(this.getTickerURL(pair))), pair);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<String> elements = (new ObjectMapper()).readTree(new URL("https://poloniex.com/public?command=returnTicker")).getFieldNames();
		for(String[] split; elements.hasNext();) {
			split = elements.next().split("_");
			pairs.add(new Pair(split[1], split[0]));
		}
		return pairs;
	}

}
