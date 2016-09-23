package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

import org.codehaus.jackson.JsonNode;

public class OneExTradeExchange extends Exchange {
	private static final List<Pair> pairs;
	
	static {
		List<String> markets = Arrays.asList(new String[] { 
			"1EX",
			"AUD",
			"BLK",
			"BTA",
			"BTC",
			"CAD",
			"CHF",
			"DASH",
			"DOGE",
			"EMC",
			"EUR",
			"FRK",
			"LTC",
			"RADS",
			"SWIFT",
			"TX",
			"USD",
			"WBB"});
		Collections.sort(markets);
		List<Pair> tempPairs = new ArrayList<Pair>();
		for(String coin : markets){
			for(String exchange : markets){
				if(!coin.equals(exchange)){
					tempPairs.add(new Pair(coin, exchange));
				}
			}
		}
		pairs = Collections.unmodifiableList(tempPairs);
	}
	
	
	public OneExTradeExchange( long expiredPeriod) {
		super("1Ex.Trade", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException,
			NoMarketDataException {
		JsonNode node = readJsonFromUrl("https://1ex.trade/api/stats?market=" + pair.getCoin() + "&currency=" + pair.getExchange());
		if(node.has("errors")){
			for(JsonNode n : node.get("errors")){
				if(n.has("message")){
					throw new MalformedURLException(n.get("message").asText());
				}
			}
			throw new NoMarketDataException(pair);
		}
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException,
			NoMarketDataException {
		return node.get("stats").get("last_price").asText();

	}

}
