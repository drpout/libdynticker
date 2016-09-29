package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class BitXExchange extends Exchange {

	public BitXExchange(long expiredPeriod) {
		super("BitX", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		for(JsonNode ticker : readJsonFromUrl("https://api.mybitx.com/api/1/tickers").get("tickers")) {
			String pair = ticker.get("pair").asText();
			pairs.add(new Pair(pair.substring(0, 3), pair.substring(3, 6)));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.mybitx.com/api/1/ticker?pair=XBTZAR
		JsonNode node = readJsonFromUrl(
				"https://api.mybitx.com/api/1/ticker?pair=" + pair.getCoin() + pair.getExchange());
		if(node.has("error"))
			throw new IOException(node.get("error").asText());
		else
			return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("last_trade").asText();
	}

}
