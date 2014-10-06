package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class OKCoinExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "USD"));
		tempPairs.add(new Pair("LTC", "USD"));
		tempPairs.add(new Pair("BTC", "CNY"));
		tempPairs.add(new Pair("LTC", "CNY"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public OKCoinExchange(long experiedPeriod) {
		super("OKCoin", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "";
		if(pair.getExchange().equals("USD")) {
			// https://www.okcoin.com/api/ticker.do?symbol=ltc_usd&ok=1
			url = "https://www.okcoin.com/api/ticker.do?symbol=" + pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase() + "&ok=1";
		} else if(pair.getExchange().equals("CNY")) {
			url = "https://www.okcoin.cn/api/ticker.do?symbol=" + pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase();
		} else {

		}
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("ticker").get("last").getTextValue();
	}
}
