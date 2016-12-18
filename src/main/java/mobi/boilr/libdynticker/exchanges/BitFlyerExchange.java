package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class BitFlyerExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "JPY"));
		tempPairs.add(new Pair("FX_BTC", "JPY"));
		tempPairs.add(new Pair("ETH", "BTC"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public BitFlyerExchange(long expiredPeriod) {
		super("bitFlyer", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://api.bitflyer.jp/v1/ticker?product_code=ETH_BTC
		String productCode = pair.getCoin() + "_" + pair.getExchange();
		JsonNode node = readJsonFromUrl("https://api.bitflyer.jp/v1/ticker?product_code=" + productCode);
		if(node.get("product_code").asText().equals(productCode))
			return parseTicker(node, pair);
		else
			throw new NoMarketDataException(pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("ltp").asText();
	}

}
