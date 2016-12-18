package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class MercadoBitcoinExchange extends Exchange {

	public static final List<Pair> PAIRS;
	private static final Pair BTCBRL = new Pair("BTC", "BRL");
	private static final Pair LTCBRL = new Pair("LTC", "BRL");
	
	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(BTCBRL);
		pairs.add(LTCBRL);
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public MercadoBitcoinExchange(long expiredPeriod) {
		super("Mercado Bitcoin", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://www.mercadobitcoin.net/api/";
		if(pair.equals(BTCBRL))
			url += "ticker/";
		else if(pair.equals(LTCBRL))
			url += "ticker_litecoin/";
		JsonNode node = readJsonFromUrl(url);
		if(node.has("ticker"))
			return parseTicker(node, pair);
		else
			throw new NoMarketDataException(pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("ticker").get("last").asText();
	}
}
