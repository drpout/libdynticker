package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public final class CryptonitExchange extends Exchange {

	public CryptonitExchange(long expiredPeriod) {
		super("Cryptonit", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		TypeReference<List<String[]>> typeReference = new TypeReference<List<String[]>>() {};
		List<String[]> elements = new ObjectMapper().readValue(
				readJsonFromUrl("http://cryptonit.net/apiv2/rest/public/pairs.json"), typeReference);
		for(String[] node : elements) {
			pairs.add(new Pair(node[1], node[0]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://cryptonit.net/apiv2/rest/public/ccorder.json?bid_currency=" +
				pair.getExchange() + "&ask_currency=" + pair.getCoin() + "&rate=1");
		String last = node.get(0).getTextValue();
		if(last.equals("currency pair not available"))
			throw new IOException(last);
		else
			return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get(0).getTextValue();
	}

}
