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
import org.codehaus.jackson.map.ObjectMapper;

public class ANXExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "USD"));
		tempPairs.add(new Pair("BTC", "HKD"));
		tempPairs.add(new Pair("BTC", "EUR"));
		tempPairs.add(new Pair("BTC", "CAD"));
		tempPairs.add(new Pair("BTC", "SGD"));
		tempPairs.add(new Pair("BTC", "JPY"));
		tempPairs.add(new Pair("BTC", "CHF"));
		tempPairs.add(new Pair("BTC", "GBP"));
		tempPairs.add(new Pair("BTC", "NZD"));
		tempPairs.add(new Pair("LTC", "BTC"));
		tempPairs.add(new Pair("PPC", "BTC"));
		tempPairs.add(new Pair("PPC", "LTC"));
		tempPairs.add(new Pair("NMC", "BTC"));
		tempPairs.add(new Pair("DOGE", "BTC"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public ANXExchange(long experiedPeriod) {
		super("ANX", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://anxpro.com/api/2/DOGEBTC/money/ticker
		URLConnection urlConnection = (new URL("https://anxpro.com/api/2/"
				+ pair.getCoin() + pair.getExchange() + "/money/ticker")).openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		urlConnection.connect();
		JsonNode node = (new ObjectMapper()).readTree(urlConnection.getInputStream());
		if(node.has("error"))
			throw new MalformedURLException(node.get("result").getTextValue());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		return node.get("data").get("last").get("value").getTextValue();
	}

}
