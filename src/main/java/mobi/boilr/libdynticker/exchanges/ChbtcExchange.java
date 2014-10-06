package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public class ChbtcExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "CNY"));
		tempPairs.add(new Pair("LTC", "CNY"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public ChbtcExchange(long experiedPeriod) {
		super("CHBTC", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
			IOException {
		// BTC: http://api.chbtc.com/data/ticker
		// LTC: http://api.chbtc.com/data/ltc/ticker
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair.");
		String url = "http://api.chbtc.com/data/";
		if(pair.getCoin().equals("LTC"))
			url += "ltc/";
		url += "ticker";
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("ticker").get("last").getTextValue();
	}

}
