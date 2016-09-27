package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class IndependentReserveExchange extends Exchange {

	public IndependentReserveExchange(long expiredPeriod) {
		super("Independent Reserve", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		JsonNode node = readJsonFromUrl("https://api.independentreserve.com/Public/GetValidPrimaryCurrencyCodes");
		List<String> primary = new ArrayList<String>();
		for(JsonNode curr : node)
			primary.add(curr.asText());
		node = readJsonFromUrl("https://api.independentreserve.com/Public/GetValidSecondaryCurrencyCodes");
		List<String> secondary = new ArrayList<String>();
		for(JsonNode curr : node)
			secondary.add(curr.asText());
		List<Pair> pairs = new ArrayList<Pair>();
		for(String prim : primary) {
			for(String sec : secondary)
				pairs.add(new Pair(prim.toUpperCase(), sec.toUpperCase()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.independentreserve.com/Public/GetMarketSummary?primaryCurrencyCode=XBT&secondaryCurrencyCode=USD
		JsonNode node = readJsonFromUrl(
				"https://api.independentreserve.com/Public/GetMarketSummary?primaryCurrencyCode=" + pair.getCoin()
						+ "&secondaryCurrencyCode=" + pair.getExchange());
		if(node.has("Message"))
			throw new IOException(node.get("Message").asText());
		else
			return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("LastPrice").asText();
	}

}
