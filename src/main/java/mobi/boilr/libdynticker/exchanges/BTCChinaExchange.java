package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class BTCChinaExchange extends Exchange {

	public BTCChinaExchange(long expiredPeriod) {
		super("BTCChina", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		String url = "https://data.btcchina.com/data/ticker?market=all";
		Iterator<String> fieldNames = (new ObjectMapper()).readTree(new URL(url)).getFieldNames();
		while (fieldNames.hasNext()) {
			String next = fieldNames.next();
			String[] split = next.split("_");
			String coin = split[1].substring(0, 3).toUpperCase();
			String exchange = split[1].substring(3, 6).toUpperCase();
			Pair pair = new Pair(coin, exchange);
			pairs.add(pair);
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://data.btcchina.com/data/ticker?market=" + pair.getCoin().toLowerCase() + pair.getExchange().toLowerCase();
		return this.parseJSON((new ObjectMapper()).readTree(new URL(url)), pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("ticker").get("last").getTextValue();
	}

}
