package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class KrakenExchange extends Exchange {

	private static final List<String> fiat;
	
	static{
		List<String> tempFiat = new ArrayList<String>();
		tempFiat.add("USD");
		tempFiat.add("EUR");
		tempFiat.add("GBP");
		tempFiat.add("CAD");
		tempFiat.add("JPY");
		fiat = Collections.unmodifiableList(tempFiat);
	}
		
	public KrakenExchange(long expiredPeriod) {
		super("Kraken", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://api.kraken.com/0/public/AssetPairs").get("result");
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
		JsonNode node = readJsonFromUrl("https://api.kraken.com/0/public/Ticker?pair=" + pairCode(pair));
		if(node.get("error").getElements().hasNext()) {
			throw new MalformedURLException(node.get("error").getElements().next().getTextValue());
		} else {
			return parseTicker(node, pair);
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("result").get(pairCode(pair)).get("c").getElements().next().getTextValue();
	}

	private String pairCode(Pair pair) {
		String exchangeCode = fiat.contains(pair.getExchange()) ? "Z" : "X";
		String coinCode = fiat.contains(pair.getCoin()) ? "Z" : "X";
		return coinCode + pair.getCoin() + exchangeCode + pair.getExchange();
	}
}
