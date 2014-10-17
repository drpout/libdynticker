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
import org.codehaus.jackson.type.TypeReference;

public final class BTC100Exchange extends Exchange {

	private static final List<Pair> PAIRS;
	private static final Pair BTC_CNY = new Pair("BTC", "CNY");
	private static final String BTC_CNY_ID = "bit";
	private static final Pair LTC_CNY = new Pair("LTC", "CNY");
	private static final String LTC_CNY_ID = "tic";
	private static final Pair DOGE_CNY = new Pair("DOGE", "CNY");
	private static final String DOGE_CNY_ID = "dic";

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(BTC_CNY);
		pairs.add(LTC_CNY);
		pairs.add(DOGE_CNY);
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public BTC100Exchange(long expiredPeriod) {
		super("BTC100", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = new ObjectMapper().readTree(new URL("https://www.btc100.com/apidata/getdata.json"));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		TypeReference<List<JsonNode>> typeRef = new TypeReference<List<JsonNode>>() {
		};
		List<JsonNode> json = new ObjectMapper().readValue(node, typeRef);
		String id;
		if(pair.equals(BTC_CNY))
			id = BTC_CNY_ID;
		else if(pair.equals(LTC_CNY))
			id = LTC_CNY_ID;
		else if(pair.equals(DOGE_CNY))
			id = DOGE_CNY_ID;
		else
			throw new IOException(pair + " is invalid.");

		return json.get(0).get(id).getTextValue();
	}
}
