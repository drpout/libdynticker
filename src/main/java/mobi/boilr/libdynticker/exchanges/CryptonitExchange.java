package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
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
		String addr = "http://cryptonit.net/apiv2/rest/public/pairs.json";
		URLConnection uc = new URL(addr).openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		TypeReference<List<String[]>> typeReference = new TypeReference<List<String[]>>() {
		};
		List<String[]> elements = new ObjectMapper().readValue(uc.getInputStream(), typeReference);
		for(String[] node : elements) {
			pairs.add(new Pair(node[1], node[0]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String addr = "https://cryptonit.net/apiv2/rest/public/ccorder.json?bid_currency=" + pair.getExchange() + "&ask_currency=" + pair.getCoin()
				+ "&rate=1";
		URLConnection uc = new URL(addr).openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode node = new ObjectMapper().readTree(uc.getInputStream());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		String last = node.get(0).getTextValue();
		if(last.equals("currency pair not available"))
			throw new IOException(last);
		else
			return last;
	}

}
