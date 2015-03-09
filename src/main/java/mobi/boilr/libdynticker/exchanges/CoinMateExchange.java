package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class CoinMateExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "USD"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public CoinMateExchange(long expiredPeriod) {
		super("CoinMate", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://coinmate.io/api/ticker?currencyPair=BTC_USD
		JsonNode node = (new ObjectMapper()).readTree(new URL(
			"https://coinmate.io/api/ticker?currencyPair="
			+ pair.getCoin() + "_" + pair.getExchange()));
		if(node.get("error").getBooleanValue())
			throw new MalformedURLException(node.get("errorMessage").getTextValue());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		return node.get("data").get("last").asText();
	}

}
