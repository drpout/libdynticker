package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public final class KrakenExchange extends Exchange {

	private static final List<String> fiat;
	
	static{
		List<String> tempFiat = new ArrayList<String>();
		tempFiat.add("USD");
		tempFiat.add("EUR");
		tempFiat.add("GBP");
		tempFiat.add("JPY");
		fiat = Collections.unmodifiableList(tempFiat);
	}
		
	public KrakenExchange(long expiredPeriod) {
		super("Kraken", expiredPeriod);
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
		String coin, exchange;
		for (JsonNode jsonNode; fieldNames.hasNext();) {
			jsonNode = node.get(fieldNames.next());
			coin = jsonNode.get("base").getTextValue().substring(1);
			exchange = jsonNode.get("quote").getTextValue().substring(1);
			pairs.add(new Pair(coin, exchange));
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
		if(!node.get("error").getElements().hasNext()) {
			return node.get("result").get(this.pairCode(pair)).get("c").getElements().next().getTextValue();
		} else {
			throw new IOException(node.get("error").getElements().next().getTextValue());
		}
	}

	private String pairCode(Pair pair) {
		String exchangeCode = fiat.contains(pair.getExchange()) ? "Z" : "X";
		String coinCode = fiat.contains(pair.getCoin()) ? "Z" : "X";
		return coinCode + pair.getCoin() + exchangeCode + pair.getExchange();
	}
}
