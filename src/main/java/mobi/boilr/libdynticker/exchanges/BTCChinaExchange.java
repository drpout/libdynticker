package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BTCChinaExchange extends Exchange {

	public BTCChinaExchange(long expiredPeriod) {
		super("BTCChina", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<String> fieldNames = readJsonFromUrl("https://data.btcchina.com/data/ticker?market=all").getFieldNames();
		while (fieldNames.hasNext()) {
			String[] split = fieldNames.next().split("_");
			String coin = split[1].substring(0, 3).toUpperCase();
			String exchange = split[1].substring(3, 6).toUpperCase();
			pairs.add(new Pair(coin, exchange));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://data.btcchina.com/data/ticker?market=btccny
		String url = "https://data.btcchina.com/data/ticker?market=" +
				pair.getCoin().toLowerCase() + pair.getExchange().toLowerCase();
		return this.parseTicker(readJsonFromUrl(url), pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("ticker").get("last").asText();
	}

}
