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

public final class YobitExchange extends Exchange {

	public YobitExchange(long expiredPeriod) {
		super("Yobit", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		URLConnection uc = new URL("https://yobit.net/api/3/info").openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode elements = (new ObjectMapper()).readTree(uc.getInputStream()).get("pairs");
		Iterator<String> fieldNames = elements.getFieldNames();
		String[] split;
		String element;
		while(fieldNames.hasNext()) {
			element = fieldNames.next();
			split = element.split("_");
			pairs.add(new Pair(split[0].toUpperCase(), split[1].toUpperCase()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
	IOException {
		String url = "https://yobit.net/api/3/ticker/" + pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase();
		URLConnection uc = new URL(url).openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has(pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase())) {
			return node.get(pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase()).get("last").asText();
		} else {
			throw new IOException(node.get("error").asText());
		}
	}
}