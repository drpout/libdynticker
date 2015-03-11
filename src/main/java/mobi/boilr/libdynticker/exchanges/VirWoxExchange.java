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
		String url = "http://api.virwox.com/api/json.php?method=getInstruments";
		List<Pair> pairs = new ArrayList<Pair>();

		JsonNode node = new ObjectMapper().readTree(new URL(url));
		Iterator<JsonNode> elements = node.get("result").getElements();

		while(elements.hasNext()) {
			JsonNode next = elements.next();
			pairs.add(new Pair(next.get("longCurrency").asText(), next.get("shortCurrency").asText()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "http://api.virwox.com/api/json.php?method=getRawTradeData&instrument=" + pair + "&timespan=172800";
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.get("result").get("errorCode").asText().equals("OK")) {
			JsonNode data = node.get("result").get("data");
			if(data.asText().equals("null")) {
				throw new IOException("No avaliable trades");
			}else{
				Iterator<JsonNode> elements = data.getElements();
			JsonNode last = null;
			while(elements.hasNext())
				last = elements.next();
			return last.get("price").asText();
			}
		}
		throw new IOException(node.get("result").get("errorCode").asText());
	}
}