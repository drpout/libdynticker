package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public final class BitcurexExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "PLN"));
		tempPairs.add(new Pair("BTC", "EUR"));
		tempPairs.add(new Pair("USD", "EUR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BitcurexExchange(long expiredPeriod) {
		super("Bitcurex", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
			IOException {
		// https://bitcurex.com/api/eur/trades.json
		URLConnection urlConnection = (new URL("https://bitcurex.com/api/"
				+ pair.getExchange().toLowerCase() + "/trades.json")).openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		urlConnection.connect();
		JsonNode node = (new ObjectMapper()).readTree(urlConnection.getInputStream());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.getElements().hasNext())
			return node.get(0).get("price").toString();
		else
			throw new IOException(pair + " not found");
	}
}
