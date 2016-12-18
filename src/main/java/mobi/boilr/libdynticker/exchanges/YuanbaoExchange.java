package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class YuanbaoExchange extends Exchange {

	public YuanbaoExchange(long expiredPeriod) {
		super("Yuanbao", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://www.yuanbao.com/coins/markets");
		if(node.get("msg").asText().equals("success")) {
			for(JsonNode pair : node.get("data").get("cny")) {
				pairs.add(new Pair(pair.get("coin_from").asText().toUpperCase(),
						pair.get("coin_to").asText().toUpperCase()));
			}
		} else
			throw new IOException(node.get("msg").asText());
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://www.yuanbao.com/api_market/getInfo_cny/coin/btc
		JsonNode node = readJsonFromUrl(
				"https://www.yuanbao.com/api_market/getInfo_cny/coin/" + pair.getCoin().toLowerCase());
		if(node.get("available_supply").isBoolean())
			throw new MalformedURLException("Invalid pair: " + pair);
		else
			return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("price").asText();
	}

}
