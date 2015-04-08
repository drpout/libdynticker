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

public final class CavirtexExchange extends Exchange {

	public CavirtexExchange(long expiredPeriod) {
		super("Cavirtex", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
			IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://www.cavirtex.com/api2/ticker.json");
		if(node.get("status").getTextValue().equals("ok")) {
			Iterator<String> pairCode = node.get("ticker").getFieldNames();
			for(String next; pairCode.hasNext();) {
				next = pairCode.next().toString();
				pairs.add(new Pair(next.substring(0, 3), next.substring(3, 6)));
			}
			return pairs;
		} else {
			throw new IOException(node.get("message").getTextValue());
		}
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
			IOException {
		JsonNode node = readJsonFromUrl("https://www.cavirtex.com/api2/ticker.json?currencypair=" +
			pair.getCoin() + pair.getExchange());
		if(node.get("status").getTextValue().equals("ok")) {
			return this.parseTicker(node, pair);
		} else {
			throw new IOException(node.get("message").getTextValue());
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("ticker").get(pair.getCoin() + pair.getExchange()).get("last").toString();
	}
}