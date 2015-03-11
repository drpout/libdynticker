package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class QuadrigaCXExchange extends Exchange {
	public final static List<Pair> PAIRS;

	static {

		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "CAD"));
		pairs.add(new Pair("BTC", "USD"));
		pairs.add(new Pair("BTC", "XAU"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public QuadrigaCXExchange(long expiredPeriod) {
		super("Quadriga CX", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://api.quadrigacx.com/v2/ticker?book=" + pair.getCoin() + "_" + pair.getExchange();
		URLConnection uc = new URL(url).openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode node = new ObjectMapper().readTree(uc.getInputStream());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has("error")) {
			throw new IOException(node.get("error").get("message").asText());
		} else {
			return node.get("last").asText();
		}
	}

}
