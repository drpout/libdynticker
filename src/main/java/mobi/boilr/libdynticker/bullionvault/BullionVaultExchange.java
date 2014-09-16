package mobi.boilr.libdynticker.bullionvault;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public class BullionVaultExchange extends Exchange {

	public BullionVaultExchange(long experiedPeriod) {
		super("BullionVaul", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();

		String[] securities = { "AUX", "AGX" };
		String[] valuations = { "USD", "GBP", "EUR", "JPY", "AUD", "CAD", "CHF" };

		for(String security : securities) {
			for(String valuation : valuations) {
				pairs.add(new Pair(security, valuation));
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://www.galmarley.com/prices/prices.json?version=v2&interval=5&batch=Update&securityId=" + pair.getCoin() + "&valuationSecurityId=" + pair.getExchange();
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has("latestPrice")) {

			return String.valueOf(node.get("latestPrice").get("price"));
		} else {
			throw new IOException();
		}
	}
}
