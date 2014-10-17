package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public final class HitBTCExchange extends Exchange {

	public HitBTCExchange(long expiredPeriod) {
		super("HitBTC", expiredPeriod);
	}

	protected String getTickerURL(Pair pair) {
		return "https://api.hitbtc.com/api/1/public/" + pair.getCoin() + pair.getExchange() + "/ticker";
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("last").getTextValue();
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		return parseJSON(new ObjectMapper().readTree(new URL(this.getTickerURL(pair))), pair);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<JsonNode> elements = (new ObjectMapper()).readTree(new URL("https://api.hitbtc.com/api/1/public/symbols")).get("symbols").getElements();
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

}
