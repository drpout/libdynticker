package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class HitBTCExchange extends Exchange {

	public HitBTCExchange(long expiredPeriod) {
		super("HitBTC", expiredPeriod);
	}
	
	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://api.hitbtc.com/api/1/public/symbols");
		if(node.has("message"))
			throw new IOException(node.get("message").asText());
		else {
			String symName;
			for(JsonNode sym : node.get("symbols")) {
				symName = sym.get("symbol").asText();
				if(symName.length() == 6) {
					pairs.add(new Pair(symName.substring(0, 3), symName.substring(3, 6)));
				} else if(symName.startsWith("DOGE")) {
					pairs.add(new Pair(symName.substring(0, 4), symName.substring(4, 7)));
				}
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.hitbtc.com/api/1/public/DRKBTC/ticker
		JsonNode node = readJsonFromUrl("https://api.hitbtc.com/api/1/public/" +
				pair.getCoin() + pair.getExchange() + "/ticker");
		if(node.has("message")) {
			throw new IOException(node.get("message").asText());
		} else {
			return parseTicker(node, pair);
		}
	}
	
	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("last").asText();
	}

}
