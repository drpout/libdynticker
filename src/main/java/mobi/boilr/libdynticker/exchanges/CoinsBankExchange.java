package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class CoinsBankExchange extends Exchange {

	public CoinsBankExchange(long expiredPeriod) {
		super("CoinsBank", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		for(JsonNode pair : readJsonFromUrl("https://coinsbank.com/sapi/head").get("pairs"))
			pairs.add(new Pair(pair.get("base_code").asText(), pair.get("quote_code").asText()));
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://coinsbank.com/api/bitcoincharts/trades/EURAUD
		JsonNode node = readJsonFromUrl(
				"https://coinsbank.com/api/bitcoincharts/trades/" + pair.getCoin() + pair.getExchange());
		if(node.size() > 0)
			return parseTicker(node, pair);
		else
			throw new IOException();
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get(0).get("price").asText();
	}

}
