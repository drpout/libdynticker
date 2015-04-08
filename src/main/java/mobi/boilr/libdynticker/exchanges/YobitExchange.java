package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;

public final class YobitExchange extends Exchange {

	public YobitExchange(long expiredPeriod) {
		super("Yobit", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode elements = readJsonFromUrl("https://yobit.net/api/3/info").get("pairs");
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
		String pairCode = pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase();
		JsonNode node = readJsonFromUrl("https://yobit.net/api/3/ticker/" + pairCode);
		if(node.has(pairCode)) {
			return parseTicker(node, pair);
		} else {
			throw new IOException(node.get("error").asText());
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get(pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase())
				.get("last").asText();
	}
}