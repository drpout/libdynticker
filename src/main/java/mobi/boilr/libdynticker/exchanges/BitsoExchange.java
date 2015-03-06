package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class BitsoExchange extends Exchange {

	private static final List<Pair> PAIRS;;

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "MXN"));
		PAIRS = Collections.unmodifiableList(pairs);
	}
	
	public BitsoExchange(long expiredPeriod) {
		super("Bitso", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://api.bitso.com/v2/ticker?book=" + pair.getCoin() + "_" + pair.getExchange();
		URLConnection urlConnection = (new URL(url)).openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		urlConnection.connect();
		JsonNode node = new ObjectMapper().readTree(urlConnection.getInputStream());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has("error")) {
			throw new IOException(node.get("error").get("message").asText());
		} else {
			return node.get("last").asText();
		}
	}
}
