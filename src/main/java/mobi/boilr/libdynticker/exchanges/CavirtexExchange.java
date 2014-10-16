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

public class CavirtexExchange extends Exchange {

	public CavirtexExchange(long experiedPeriod) {
		super("Cavirtex", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
			IOException {
		String url = "https://www.cavirtex.com/api2/ticker.json";
		List<Pair> pairs = new ArrayList<Pair>();
		URLConnection uc = (new URL(url)).openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream());
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
		String url = "https://www.cavirtex.com/api2/ticker.json?currencypair=" + pair.getCoin() + pair.getExchange();
		URLConnection uc = (new URL(url)).openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream());
		if(node.get("status").getTextValue().equals("ok")) {
			return this.parseJSON(node, pair);
		} else {
			throw new IOException(node.get("message").getTextValue());
		}
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("ticker").get(pair.getCoin() + pair.getExchange()).get("last").toString();
	}
}