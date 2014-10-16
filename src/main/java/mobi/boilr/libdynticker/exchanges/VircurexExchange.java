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

public class VircurexExchange extends Exchange {

	public VircurexExchange(long experiedPeriod) {
		super("Vircurex", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
	IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		URLConnection urlConnection = (new URL("https://api.vircurex.com/api/get_info_for_currency.json")).openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		urlConnection.connect();
		JsonNode coinsNode = (new ObjectMapper()).readTree(urlConnection.getInputStream());
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
		URLConnection urlConnection = (new URL("https://api.vircurex.com/api/get_last_trade.json?base="
				+ pair.getCoin() + "&alt=" + pair.getExchange())).openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		urlConnection.connect();
		JsonNode node = (new ObjectMapper()).readTree(urlConnection.getInputStream());
		if(node.get("status").asBoolean())
			throw new IOException(node.get("status_text").getTextValue());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("value").getTextValue();
	}

}
