package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class VaultoroExchange extends Exchange {

	public VaultoroExchange(long expiredPeriod) {
		super("Vaultoro", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://api.vaultoro.com/markets");
		if(node.get("status").asText().equals("success")) {
			JsonNode data = node.get("data");
			pairs.add(new Pair(data.get("MarketCurrency").asText(), data.get("BaseCurrency").asText()));
		} else
			throw new IOException(node.get("status").asText());
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!getPairs().contains(pair))
			throw new IOException("Invalid pair: " + pair);
		JsonNode node = readJsonFromUrl("https://api.vaultoro.com/latesttrades?count=1");
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get(0).get("price").asText();
	}

}
