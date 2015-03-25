package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class BullionVaultExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		String[] securities = { "AUX", "AGX" };
		String[] valuations = { "USD", "GBP", "EUR", "JPY", "AUD", "CAD", "CHF" };
		for(String security : securities) {
			for(String valuation : valuations) {
				tempPairs.add(new Pair(security, valuation));
			}
		}
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BullionVaultExchange(long expiredPeriod) {
		super("BullionVault", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://www.galmarley.com/prices/prices.json?version=v2&interval=5&batch=Update&securityId="
				+ pair.getCoin() + "&valuationSecurityId=" + pair.getExchange());
		if(node.has("latestPrice")) {
			return parseTicker(node, pair);
		} else {
			throw new IOException("Invalid pair: " + pair);
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return String.valueOf(node.get("latestPrice").get("price"));
	}
}
