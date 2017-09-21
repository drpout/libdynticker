package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class KrakenExchange extends Exchange {

	public KrakenExchange(long expiredPeriod) {
		super("Kraken", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://api.kraken.com/0/public/AssetPairs");
		if(node.get("error").getElements().hasNext())
			throw new IOException(node.get("error").get(0).asText());
		else {
			String coin;
			for(JsonNode asset : node.get("result")) {
				coin = asset.get("base").asText();
				if(coin.charAt(0) == 'X')
					coin = coin.substring(1);
				pairs.add(new Pair(coin, asset.get("quote").asText().substring(1)));
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl(
				"https://api.kraken.com/0/public/Ticker?pair=" + pair.getCoin() + pair.getExchange());
		if(node.get("error").getElements().hasNext())
			throw new IOException(node.get("error").get(0).asText());
		else
			return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("result").getElements().next().get("c").get(0).asText();
	}
}
