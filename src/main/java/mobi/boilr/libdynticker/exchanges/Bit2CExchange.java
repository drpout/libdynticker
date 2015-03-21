package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public final class Bit2CExchange extends Exchange {

	private static final List<Pair> PAIRS;

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "NIS"));
		pairs.add(new Pair("LTC", "BTC"));
		pairs.add(new Pair("LTC", "NIS"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public Bit2CExchange(long expiredPeriod) {
		super("Bit2C", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(PAIRS.contains(pair)){
		String url = "https://www.bit2c.co.il/Exchanges/" + pair.getCoin() + pair.getExchange() + "/Ticker.json";
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		return parseJSON(node, pair);
		}else{
			throw new IOException(pair + " not found");
		}
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		return node.get("ll").asText();
	}
}