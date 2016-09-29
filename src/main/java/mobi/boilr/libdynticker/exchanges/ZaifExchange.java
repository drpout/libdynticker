package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class ZaifExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<String> coins = Arrays.asList(new String[] { "BTC", "XEM", "MONA" });
		List<String> exchanges = Arrays.asList(new String[] { "JPY", "BTC" });
		List<Pair> tempPairs = new ArrayList<Pair>();
		for(String coin : coins) {
			for(String exchange : exchanges) {
				if(!coin.equals(exchange)) {
					tempPairs.add(new Pair(coin, exchange));
				}
			}
		}
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public ZaifExchange(long expiredPeriod) {
		super("Zaif", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		// https://api.zaif.jp/api/1/last_price/btc_jpy
		JsonNode node = readJsonFromUrl("https://api.zaif.jp/api/1/last_price/" + pair.getCoin().toLowerCase() + "_"
				+ pair.getExchange().toLowerCase());
		if(node.has("error"))
			throw new IOException(node.get("error").asText());
		else
			return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last_price").asText();
	}

}
