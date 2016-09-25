package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class CRXzoneExchange extends Exchange {

	public CRXzoneExchange(long expiredPeriod) {
		super("CRXzone", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://www.crxzone.com/API/CurrencyPairs");
		if(node.get("IsSuccess").asBoolean()) {
			for(JsonNode pair : node.get("Result"))
				pairs.add(new Pair(pair.get("PrimaryCurrencyCode").asText(), pair.get("SecondaryCurrencyCode").asText(), 
						pair.get("ID").asText()));
		} else {
			throw new IOException(node.get("ErrorMessage").asText());
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://www.crxzone.com/API/Ticker?currencyPairID=2
		JsonNode node = readJsonFromUrl("https://www.crxzone.com/API/Ticker?currencyPairID=" + pair.getMarket());
		if(node.get("IsSuccess").asBoolean())
			return parseTicker(node, pair);
		else
			throw new IOException(node.get("ErrorMessage").asText());
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("Result").get("Last").asText();
	}

}
