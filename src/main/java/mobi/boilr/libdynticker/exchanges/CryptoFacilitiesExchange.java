package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class CryptoFacilitiesExchange extends Exchange {

	public CryptoFacilitiesExchange(long expiredPeriod) {
		super("Crypto Facilities", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("F-XBT:USD-Jun15", "USD"));
		pairs.add(new Pair("F-XBT:USD-Sep15", "USD"));
		pairs.add(new Pair("F-XBT:USD-Dec15", "USD"));
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://www.cryptofacilities.com/derivatives/api/ticker?tradeable=" + pair.getCoin() + "&unit=" + pair.getExchange();
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.get("result").asText().equals("error")) {
			throw new IOException();
		} else {
			return node.get("last").asText();
		}
	}

}
