package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class CryptopiaExchange extends Exchange {

	public CryptopiaExchange(long expiredPeriod) {
		super("Cryptopia", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://www.cryptopia.co.nz/api/GetTradePairs");
		if(node.get("Message").isNull())
			for(JsonNode pair : node.get("Data")) {
				if(pair.get("Status").asText().equals("OK"))
					pairs.add(new Pair(pair.get("Symbol").asText(), pair.get("BaseSymbol").asText(),
							pair.get("Id").asText()));
			}
		else
			throw new IOException(node.get("Message").asText());
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://www.cryptopia.co.nz/api/GetMarket/100
		JsonNode node = readJsonFromUrl("https://www.cryptopia.co.nz/api/GetMarket/" + pair.getMarket());
		if(node.get("Message").isNull())
			return parseTicker(node, pair);
		else
			throw new MalformedURLException(node.get("Message").asText());
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("Data").get("LastPrice").asText();
	}

}
