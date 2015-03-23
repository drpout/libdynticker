package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public final class MeloticExchange extends Exchange {

	public MeloticExchange(long expiredPeriod) {
		super("Melotic", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
			IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		HttpURLConnection urlConnection = (HttpURLConnection) buildConnection("https://www.melotic.com/api/markets");
		urlConnection.setRequestProperty("Accept", "*/*");
		urlConnection.connect();
		JsonNode elements = (new ObjectMapper()).readTree(urlConnection.getInputStream());
		Iterator<String> fieldNames = elements.getFieldNames();
		for(String[] split; fieldNames.hasNext();) {
			split = fieldNames.next().toUpperCase().split("-");
			pairs.add(new Pair(split[0], split[1]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
			IOException {
		// http://www.melotic.com/api/markets/gold-btc/ticker
		HttpURLConnection urlConnection = (HttpURLConnection) buildConnection("https://www.melotic.com/api/markets/" + pair.getCoin().toLowerCase()
			+ "-" + pair.getExchange().toLowerCase() + "/ticker");
		urlConnection.setRequestProperty("Accept", "*/*");
		urlConnection.connect();
		JsonNode node = (new ObjectMapper()).readTree(urlConnection.getInputStream());
		if(node.has("message"))
			throw new MalformedURLException(node.get("message").getTextValue());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("latest_price").toString();
	}
}
