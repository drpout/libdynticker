package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
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

public class PeatioExchange extends Exchange {

	public PeatioExchange(long expiredPeriod) {
		super("Peatio", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
	IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<JsonNode> elements = (new ObjectMapper()).readTree(new URL("https://peatio.com:443/api/v2/markets.json")).getElements();
		for(String[] pairSplit; elements.hasNext();) {
			pairSplit = elements.next().get("name").getTextValue().split("/");
			pairs.add(new Pair(pairSplit[0], pairSplit[1]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
	IOException {
		// https://peatio.com:443/api/v2/tickers/dogcny.json
		String url = "https://peatio.com:443/api/v2/tickers/" + pair.getCoin().toLowerCase() + pair.getExchange().toLowerCase() + ".json";
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		if(node.has("error"))
			throw new MalformedURLException(node.get("error").get("message").getTextValue());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("ticker").get("last").getTextValue();
	}

}
