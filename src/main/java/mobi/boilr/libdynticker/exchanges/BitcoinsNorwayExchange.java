package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BitcoinsNorwayExchange extends Exchange {

	public BitcoinsNorwayExchange(long expiredPeriod) {
		super("Bitcoins Norway", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://api.bitcoinsnorway.com:8400/ajax/v1/GetProductPairs");
		if(node.get("isAccepted").asBoolean()) {
			for(JsonNode pair : node.get("productPairs"))
				pairs.add(new Pair(pair.get("product1Label").asText(), pair.get("product2Label").asText()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://api.bitcoinsnorway.com:8400/ajax/v1/GetTrades",
				"{\"ins\":\"" + pair.getCoin() + pair.getExchange() + "\",\"startIndex\":-1,\"count\":1}");
		if(node.get("isAccepted").asBoolean())
			return parseTicker(node, pair);
		else
			throw new IOException(node.get("rejectReason").asText());
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("trades").get(0).get("px").asText();
	}

}
