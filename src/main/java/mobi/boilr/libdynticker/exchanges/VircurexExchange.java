package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public final class VircurexExchange extends Exchange {

	public VircurexExchange(long expiredPeriod) {
		super("Vircurex", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
	IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode coinsNode = readJsonFromUrl("https://api.vircurex.com/api/get_info_for_currency.json");
		if(coinsNode.get("status").asBoolean())
			throw new IOException(coinsNode.get("status_text").getTextValue());
		Iterator<String> coins = coinsNode.getFieldNames();
		String coin;
		for (Iterator<String> exchanges; coins.hasNext();) {
			coin = coins.next();
			exchanges = coinsNode.get(coin).getFieldNames();
			while (exchanges.hasNext()) {
				pairs.add(new Pair(coin, exchanges.next()));
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
			IOException {
		// https://api.vircurex.com/api/get_last_trade.json?base=BTC&alt=EUR
		JsonNode node = readJsonFromUrl("https://api.vircurex.com/api/get_last_trade.json?base="
				+ pair.getCoin() + "&alt=" + pair.getExchange());
		if(node.get("status").asBoolean())
			throw new IOException(node.get("status_text").getTextValue());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("value").getTextValue();
	}

}
