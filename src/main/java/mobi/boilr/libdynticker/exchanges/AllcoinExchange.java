package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public class AllcoinExchange extends Exchange {

	public AllcoinExchange(long experiedPeriod) {
		super("Allcoin", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode elements = (new ObjectMapper()).readTree(new URL("https://www.allcoin.com/api2/pairs")).get("data");
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
	IOException {
		// https://www.allcoin.com/api2/pair/LTC_BTC
		String url = "https://www.allcoin.com/api2/pair/" + pair.getCoin() + "_" + pair.getExchange();
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		if(node.get("code").getIntValue() > 0) {
			return parseJSON(node, pair);
		} else {
			throw new MalformedURLException(node.get("data").getTextValue());
		}
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("data").get("trade_price").getTextValue();
	}
}
