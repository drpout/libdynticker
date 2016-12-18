package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

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
		JsonNode node = readJsonFromUrl("https://api.kraken.com/0/public/AssetPairs");
		if(node.get("error").getElements().hasNext())
			throw new IOException(node.get("error").getElements().next().asText());
		else {
			for (JsonNode asset : node.get("result"))
				pairs.add(new Pair(asset.get("base").asText().substring(1), asset.get("quote").asText().substring(1)));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://api.kraken.com/0/public/Ticker?pair=" + pairCode(pair));
		if(node.get("error").getElements().hasNext())
			throw new IOException(node.get("error").getElements().next().asText());
		else
			return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("result").get(pairCode(pair)).get("c").getElements().next().asText();
	}

	private String pairCode(Pair pair) {
		String exchangeCode = fiat.contains(pair.getExchange()) ? "Z" : "X";
		String coinCode = fiat.contains(pair.getCoin()) ? "Z" : "X";
		return coinCode + pair.getCoin() + exchangeCode + pair.getExchange();
	}
}
