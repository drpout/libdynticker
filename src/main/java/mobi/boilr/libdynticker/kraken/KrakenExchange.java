package mobi.boilr.libdynticker.kraken;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class KrakenExchange extends Exchange {

	public KrakenExchange(long experiedPeriod) {
		super("Kraken", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		URL url = new URL("https://api.kraken.com/0/public/AssetPairs");
		URLConnection uc = url.openConnection();

		// Mask call as if it was made by a browser
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream()).get("result");
		Iterator<String> fieldNames = node.getFieldNames();
		while (fieldNames.hasNext()) {
			String next = fieldNames.next();
			JsonNode jsonNode = node.get(next);
			String coin = jsonNode.get("base").getTextValue().substring(1);
			String exchange = jsonNode.get("quote").getTextValue().substring(1);
			Pair pair = new Pair(coin, exchange);
			pairs.add(pair);
		}

		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://api.kraken.com/0/public/Ticker?pair=" + this.pairCode(pair);
		URLConnection uc = (new URL(url)).openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream());
		return this.parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if (!node.get("error").getElements().hasNext()) {
			return node.get("result").get(this.pairCode(pair)).get("c").getElements().next().getTextValue();
		} else {
			throw new IOException(node.get("error").getElements().next().getTextValue());
		}
	}

	private String pairCode(Pair pair) {
		String exchangeCode = pair.getExchange().equals("USD") || pair.getExchange().equals("EUR") || pair.getExchange().equals("KWR") ? "Z" : "X";
		String coinCode = pair.getCoin().equals("USD") || pair.getCoin().equals("USD") || pair.getCoin().equals("USD") ? "Z" : "X";
		return coinCode + pair.getCoin() + exchangeCode + pair.getExchange();
	}
}
