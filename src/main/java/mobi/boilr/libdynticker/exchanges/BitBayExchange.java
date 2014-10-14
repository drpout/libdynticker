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

public class BitBayExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "PLN"));
		tempPairs.add(new Pair("BTC", "USD"));
		tempPairs.add(new Pair("BTC", "EUR"));
		tempPairs.add(new Pair("LTC", "BTC"));
		tempPairs.add(new Pair("LTC", "PLN"));
		tempPairs.add(new Pair("LTC", "USD"));
		tempPairs.add(new Pair("LTC", "EUR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BitBayExchange(long experiedPeriod) {
		super("BitBay", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://market.bitbay.pl/API/Public/BTCPLN/ticker.json
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair.");
		URLConnection urlConnection = (new URL("https://market.bitbay.pl/API/Public/"
				+ pair.getCoin() + pair.getExchange() + "/ticker.json")).openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		urlConnection.connect();
		JsonNode node = (new ObjectMapper()).readTree(urlConnection.getInputStream());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		return node.get("last").toString();
	}

}
