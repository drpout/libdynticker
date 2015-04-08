package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;

public final class AllcoinExchange extends Exchange {

	public AllcoinExchange(long expiredPeriod) {
		super("Allcoin", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode elements = readJsonFromUrl("https://www.allcoin.com/api2/pairs").get("data");
		Iterator<String> fieldNames = elements.getFieldNames();
		String[] split;
		String element;
		while(fieldNames.hasNext()) {
			element = fieldNames.next();
			// Status : 1: online -1: closed temporarily -2: closed permanently
			// Don't add when markets closed permanently
			if(elements.get(element).get("status").getTextValue().equals("1") || elements.get(element).get("status").getTextValue().equals("-1")) {
				split = element.split("_");
				pairs.add(new Pair(split[0], split[1]));
			}
		}

		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
	IOException, NoMarketDataException {
		// https://www.allcoin.com/api2/pair/LTC_BTC
		JsonNode node = readJsonFromUrl("https://www.allcoin.com/api2/pair/" +
				pair.getCoin() + "_" + pair.getExchange());
		if(node.get("code").getIntValue() > 0) {
			return parseTicker(node, pair);
		} else {
			throw new MalformedURLException(node.get("data").getTextValue());
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		JsonNode jsonNode = node.get("data").get("trade_price");
		if(jsonNode == null) {
			throw new NoMarketDataException(pair);
		} else {
			return jsonNode.asText();
		}
	}
}
