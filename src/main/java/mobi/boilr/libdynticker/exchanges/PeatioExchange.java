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

public abstract class PeatioExchange extends Exchange {
	private String domain;

	public PeatioExchange(String name, long expiredPeriod, String domain) {
		super(name, expiredPeriod);
		this.domain = domain;
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
	IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<JsonNode> elements = readJsonFromUrl("https://" + domain + "/api/v2/markets.json").getElements();
		for(String[] pairSplit; elements.hasNext();) {
			pairSplit = elements.next().get("name").getTextValue().split("/");
			pairs.add(new Pair(pairSplit[0], pairSplit[1]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
	IOException {
		// https://domain.xxx/api/v2/tickers/dogcny.json
		JsonNode node = readJsonFromUrl("https://" + domain + "/api/v2/tickers/" +
				pair.getCoin().toLowerCase() + pair.getExchange().toLowerCase() + ".json");
		if(node.has("error"))
			throw new MalformedURLException(node.get("error").get("message").getTextValue());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("ticker").get("last").getTextValue();
	}

}
