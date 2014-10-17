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
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class AtomicTradeExchange extends Exchange {

	public AtomicTradeExchange(long expiredPeriod) {
		super("Atomic Trade", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws MalformedURLException, IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {
		};
		URLConnection urlConnection = (new URL("https://www.atomic-trade.com/SimpleAPI?a=markets")).openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		urlConnection.connect();
		List<String> symbols = (new ObjectMapper()).readValue(urlConnection.getInputStream(), typeRef);
		String[] pairSplit;
		for (String sym : symbols) {
			pairSplit = sym.split("/");
			pairs.add(new Pair(pairSplit[0], pairSplit[1]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws MalformedURLException, IOException {
		// https://www.atomic-trade.com/GetPrices?c=BLC&p=BTC
		URLConnection urlConnection = (new URL("https://www.atomic-trade.com/GetPrices?c="
				+ pair.getCoin() + "&p=" + pair.getExchange())).openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		urlConnection.connect();
		JsonNode node = (new ObjectMapper()).readTree(urlConnection.getInputStream());
		if(node.get("error").getBooleanValue())
			throw new MalformedURLException();
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("price").getTextValue();
	}

}
