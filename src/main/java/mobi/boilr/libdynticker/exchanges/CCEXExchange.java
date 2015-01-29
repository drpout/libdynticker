package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public final class CCEXExchange extends Exchange {

	public CCEXExchange(long expiredPeriod) {
		super("C-CEX", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
	IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		URL url = new URL("https://c-cex.com/t/pairs.json");
		URLConnection uc = url.openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream());
		TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {
		};
		List<String> symbols = (new ObjectMapper()).readValue(node.get("pairs"), typeRef);
		String[] pairSplit;
		for (String sym : symbols) {
			pairSplit = sym.toUpperCase().split("-");
			pairs.add(new Pair(pairSplit[0], pairSplit[1]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
	IOException {
		// https://c-cex.com/t/btc-usd.json
		URL url = new URL("https://c-cex.com/t/"
				+ pair.getCoin().toLowerCase() + "-" + pair.getExchange().toLowerCase() + ".json");
		URLConnection uc = url.openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();

		JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("ticker").get("lastprice").asText();
	}

}
