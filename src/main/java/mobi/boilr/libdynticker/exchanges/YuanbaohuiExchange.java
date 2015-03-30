package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class YuanbaohuiExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("YBC", "CNY"));
		tempPairs.add(new Pair("LTC", "CNY"));
		tempPairs.add(new Pair("DOGE", "CNY"));
		tempPairs.add(new Pair("AXF", "CNY"));
		tempPairs.add(new Pair("IRC", "CNY"));
		tempPairs.add(new Pair("YBC", "BTC"));
		tempPairs.add(new Pair("YBY", "BTC"));
		tempPairs.add(new Pair("LTC", "BTC"));
		tempPairs.add(new Pair("DOGE", "BTC"));
		tempPairs.add(new Pair("YBY", "YBC"));
		tempPairs.add(new Pair("LTC", "YBC"));
		tempPairs.add(new Pair("DOGE", "YBC"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public YuanbaohuiExchange(long expiredPeriod) {
		super("Yuanbaohui", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		// https://yuanbaohui.com/json/ybc2cny_order?t=0.8060067854962146
		try {
			JsonNode node = readJsonFromUrl("https://yuanbaohui.com/json/"
				+ pair.getCoin().toLowerCase() + "2" + pair.getExchange().toLowerCase()
				+ "_order?t=0.8060067854962146");
			return parseTicker(node, pair);
		} catch(IOException e) {
			if(e.getMessage().contains("403"))
				throw new MalformedURLException("Invalid pair: " + pair);
			else
				throw e;
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("d").getElements().next().get("p").getTextValue();
	}

}
