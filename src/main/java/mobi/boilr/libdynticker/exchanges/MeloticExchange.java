package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
		URL url = new URL("https://www.melotic.com/api/markets");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestProperty("Accept", "*/*");
		urlConnection.connect();
		InputStream is = urlConnection.getInputStream();
		JsonNode elements = (new ObjectMapper()).readTree(is);
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
		HttpURLConnection urlConnection = (HttpURLConnection) (new URL("https://www.melotic.com/api/markets/"
				+ pair.getCoin().toLowerCase() + "-" + pair.getExchange().toLowerCase() + "/ticker")).openConnection();
		urlConnection.setRequestProperty("Accept", "*/*");
		urlConnection.connect();
		JsonNode node = (new ObjectMapper()).readTree(urlConnection.getInputStream());
		if(node.has("message"))
			throw new MalformedURLException(node.get("message").getTextValue());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("latest_price").toString();
	}
}