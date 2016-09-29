package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class OneBTCXEExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "USD"));
		tempPairs.add(new Pair("BTC", "EUR"));
		tempPairs.add(new Pair("BTC", "CNY"));
		tempPairs.add(new Pair("BTC", "RUB"));
		tempPairs.add(new Pair("BTC", "CHF"));
		tempPairs.add(new Pair("BTC", "JPY"));
		tempPairs.add(new Pair("BTC", "GBP"));
		tempPairs.add(new Pair("BTC", "CAD"));
		tempPairs.add(new Pair("BTC", "AUD"));
		tempPairs.add(new Pair("BTC", "AED"));
		tempPairs.add(new Pair("BTC", "BGN"));
		tempPairs.add(new Pair("BTC", "CZK"));
		tempPairs.add(new Pair("BTC", "DKK"));
		tempPairs.add(new Pair("BTC", "HKD"));
		tempPairs.add(new Pair("BTC", "HRK"));
		tempPairs.add(new Pair("BTC", "HUF"));
		tempPairs.add(new Pair("BTC", "ILS"));
		tempPairs.add(new Pair("BTC", "INR"));
		tempPairs.add(new Pair("BTC", "MUR"));
		tempPairs.add(new Pair("BTC", "MXN"));
		tempPairs.add(new Pair("BTC", "NOK"));
		tempPairs.add(new Pair("BTC", "NZD"));
		tempPairs.add(new Pair("BTC", "PLN"));
		tempPairs.add(new Pair("BTC", "RON"));
		tempPairs.add(new Pair("BTC", "SEK"));
		tempPairs.add(new Pair("BTC", "SGD"));
		tempPairs.add(new Pair("BTC", "THB"));
		tempPairs.add(new Pair("BTC", "TRY"));
		tempPairs.add(new Pair("BTC", "ZAR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public OneBTCXEExchange(long expiredPeriod) {
		super("1BTCXE", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://1btcxe.com/api/stats?currency=MXN
		JsonNode node = readJsonFromUrl("https://1btcxe.com/api/stats?currency=" + pair.getExchange());
		if(node.has("errors"))
			throw new IOException(node.get("errors").get(0).get("message").asText());
		else
			return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("stats").get("last_price").asText();
	}

}
