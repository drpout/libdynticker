package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class OKCoinExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "CNY"));
		tempPairs.add(new Pair("LTC", "CNY"));
		tempPairs.add(new Pair("BTC", "USD"));
		tempPairs.add(new Pair("LTC", "USD"));

		tempPairs.add(new Pair("BTC Futures (this week)", "USD"));
		tempPairs.add(new Pair("BTC Futures (next week)", "USD"));
		tempPairs.add(new Pair("BTC Futures (quarter)", "USD"));

		tempPairs.add(new Pair("LTC Futures (this week)", "USD"));
		tempPairs.add(new Pair("LTC Futures (next week)", "USD"));
		tempPairs.add(new Pair("LTC Futures (quarter)", "USD"));

		pairs = Collections.unmodifiableList(tempPairs);
	}

	public OKCoinExchange(long expiredPeriod) {
		super("OKCoin", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		String url = "https://www.okcoin.com/api/";
		String coin = pair.getCoin();
		String exchange = pair.getExchange();
		if(coin.contains("Futures")) {
			String contractTtpe = coin.split("[()]")[1].replace(" ", "_");
			url += "future_ticker.do?symbol=" + coin.substring(0, 3).toLowerCase() + "_" + exchange.toLowerCase() + "&"
					+ "contractType=" + contractTtpe;
			return parseJSONFuture(readJsonFromUrl(url), pair);
		}
		else if(exchange.equals("USD")) {
			// https://www.okcoin.com/api/ticker.do?symbol=ltc_usd&ok=1
			url += "ticker.do?symbol=" + coin.toLowerCase() + "_" + exchange.toLowerCase() + "&ok=1";
		}
		else if(exchange.equals("CNY")) {
			url = "https://www.okcoin.cn/api/ticker.do?symbol=" + coin.toLowerCase() + "_" + exchange.toLowerCase();
		}
		return parseTicker(readJsonFromUrl(url), pair);
	}

	protected String parseJSONFuture(JsonNode node, Pair pair) throws JsonParseException, JsonMappingException, IOException {
		TypeReference<JsonNode[]> typeReference = new TypeReference<JsonNode[]>() {};
		JsonNode[] values = new ObjectMapper().readValue(node.get("ticker"), typeReference);
		return values[0].get("last").toString();
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("ticker").get("last").getTextValue();
	}
}