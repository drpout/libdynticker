package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class CCEXExchange extends Exchange {

	public CCEXExchange(long expiredPeriod) {
		super("C-CEX", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://c-cex.com/t/pairs.json");
		TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {
		};
		List<String> symbols = (new ObjectMapper()).readValue(node.get("pairs"), typeRef);
		String[] pairSplit;
		for (String sym : symbols) {
			pairSplit = sym.toUpperCase().split("-");
			pairs.add(new Pair(pairSplit[0], pairSplit[1]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://c-cex.com/t/btc-usd.json
		JsonNode node = readJsonFromUrl("https://c-cex.com/t/"
				+ pair.getCoin().toLowerCase() + "-" + pair.getExchange().toLowerCase() + ".json");
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("ticker").get("lastprice").asText();
	}

}
