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

public class ItBitExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("XBT", "USD"));
		tempPairs.add(new Pair("XBT", "SGD"));
		tempPairs.add(new Pair("XBT", "EUR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public ItBitExchange(long experiedPeriod) {
		super("itBit", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.itbit.com/v1/markets/XBTUSD/ticker
		String url = "https://api.itbit.com/v1/markets/" + pair.getCoin() + pair.getExchange() + "/ticker";
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		// TODO Check for error
		if(node.has("error"))
			throw new MalformedURLException(node.get("error").get("message").getTextValue());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		return node.get("lastPrice").getTextValue();
	}

}
