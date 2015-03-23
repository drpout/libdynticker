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

public class VirWoxExchange extends Exchange {

	public VirWoxExchange(long expiredPeriod) {
		super("VirWox", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("http://api.virwox.com/api/json.php?method=getInstruments");
		Iterator<JsonNode> elements = node.get("result").getElements();
		while(elements.hasNext()) {
			JsonNode next = elements.next();
			pairs.add(new Pair(next.get("longCurrency").asText(), next.get("shortCurrency").asText()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("http://api.virwox.com/api/json.php?method=getRawTradeData&instrument=" +
				pair + "&timespan=172800");
		if(node.get("result").get("errorCode").asText().equals("OK")) {
			return parseTicker(node, pair);
		} else {
			throw new IOException(node.get("result").get("errorCode").asText());
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		JsonNode data = node.get("result").get("data");
		if(data.asText().equals("null")) {
			throw new IOException("No avaliable trades");
		} else {
			Iterator<JsonNode> elements = data.getElements();
			JsonNode last = null;
			while(elements.hasNext())
				last = elements.next();
			return last.get("price").asText();
		}
	}
}