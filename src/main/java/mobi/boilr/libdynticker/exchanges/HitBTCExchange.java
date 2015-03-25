package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class HitBTCExchange extends Exchange {

	public HitBTCExchange(long expiredPeriod) {
		super("HitBTC", expiredPeriod);
	}
	
	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<JsonNode> elements = readJsonFromUrl("https://api.hitbtc.com/api/1/public/symbols")
				.get("symbols").getElements();
		for(String textValue; elements.hasNext();) {
			textValue = elements.next().get("symbol").getTextValue();
			if(textValue.length() == 6) {
				pairs.add(new Pair(textValue.substring(0, 3),
						textValue.substring(3, 6)));
			} else if(textValue.startsWith("DOGE")) {
				pairs.add(new Pair(textValue.substring(0, 4),
						textValue.substring(4, 7)));
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
			throw new IOException(node.get("message").getTextValue());
		} else {
			return parseTicker(node, pair);
		}
	}
	
	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return node.get("last").getTextValue();
	}

}
