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

public class BTC38Exchange extends Exchange {

	public BTC38Exchange(long experiedPeriod) {
		super("BTC38", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		String[] exchanges = { "cny", "btc" };
		String addr = "http://api.btc38.com/v1/ticker.php?c=all&mk_type=";

		for (String exch : exchanges) {
			URL url = new URL(addr + exch);
			URLConnection uc = url.openConnection();
			uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			uc.connect();
			JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream());
			Iterator<String> coins = node.getFieldNames();
			while (coins.hasNext()) {
				String coin = coins.next();
				Pair pair = new Pair(coin.toUpperCase(), exch.toUpperCase());
				pairs.add(pair);
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String addr = "http://api.btc38.com/v1/ticker.php?c=" + pair.getCoin() + "&mk_type=" + pair.getExchange();
		URL url = new URL(addr);
		URLConnection uc = url.openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has("ticker") && node.get("ticker").isObject()) {
			return node.get("ticker").get("last").toString();
		} else {
			throw new IOException("No data for pair " + pair + ".");
		}
	}

}
