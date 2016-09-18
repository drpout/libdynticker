package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class CryptoFacilitiesExchange extends Exchange {

	public CryptoFacilitiesExchange(long expiredPeriod) {
		super("Crypto Facilities", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("https://www.cryptofacilities.com/derivatives/api/v2/instruments");
		if(node.get("result").asText().equals("success")) {
			for(JsonNode inst : node.get("instruments")) {
				pairs.add(new Pair(inst.get("symbol").asText().toUpperCase(), "USD"));
			}
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://www.cryptofacilities.com/derivatives/api/v2/tickers");
		if(node.get("result").asText().equals("error")) {
			throw new IOException();
		} else {
			return parseTicker(node, pair);
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		for(JsonNode ticker : node.get("tickers")) {
			if(ticker.get("symbol").asText().equalsIgnoreCase(pair.getCoin()))
				return ticker.get("last").asText();
		}
		throw new NoMarketDataException(pair);
	}

}
