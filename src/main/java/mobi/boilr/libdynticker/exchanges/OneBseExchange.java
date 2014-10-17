package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class OneBseExchange extends Exchange {

	public OneBseExchange(long expiredPeriod) {
		super("1Bce", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		String url = "https://www.1bse.com/allprices.json";
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = new ObjectMapper().readTree(new URL(url));

		if(node.get("success").getTextValue().equals("1")) {
			JsonNode ret = node.get("return");
			Iterator<String> fieldNames = ret.getFieldNames();
			String[] split;
			while(fieldNames.hasNext()) {
				split = fieldNames.next().split("_");
				pairs.add(new Pair(split[0].toUpperCase(), split[1].toUpperCase()));
			}
		}
		else {
			throw new IOException("Could not retreive pairs from " + getName() + ".");
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://www.1bse.com/allprices.json";
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		if(node.get("success").getTextValue().equals("1"))
			return parseJSON(node, pair);
		else
			throw new MalformedURLException();
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		return node.get("return").get(pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase()).get("price").getTextValue();
	}
}
