package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public final class BTC38Exchange extends Exchange {

	public BTC38Exchange(long expiredPeriod) {
		super("BTC38", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		String[] exchanges = { "cny", "btc" };
		String addr = "http://api.btc38.com/v1/ticker.php?c=all&mk_type=";
		for(String exch : exchanges) {
			JsonNode node = readJsonFromUrl(addr + exch);
			Iterator<String> coins = node.getFieldNames();
			for(String coin; coins.hasNext();) {
				coin = coins.next();
				if(node.get(coin).get("ticker").isObject()) {
					pairs.add(new Pair(coin.toUpperCase(), exch.toUpperCase()));
				}
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("http://api.btc38.com/v1/ticker.php?c=" + 
				pair.getCoin() + "&mk_type=" + pair.getExchange());
		if(!node.has("ticker") || !node.get("ticker").isObject())
			throw new IOException("No data for pair " + pair + ".");
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("ticker").get("last").toString();
	}
}
