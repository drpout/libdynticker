package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

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
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException, IOException {
		String url = null;
		if(pair.equals(BTCBRL)) {
			url = "https://www.mercadobitcoin.net/api/ticker/";
		} else if(pair.equals(LTCBRL)) {
			url = "https://www.mercadobitcoin.net/api/ticker_litecoin/";
		}
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.has("ticker")) {
			String value = node.get("ticker").get("last").asText();
			return value;
		} else {
			throw new IOException("No market data");
		}
	}
}
