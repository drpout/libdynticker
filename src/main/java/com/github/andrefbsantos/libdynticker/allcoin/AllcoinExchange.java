/**
 *
 */
package com.github.andrefbsantos.libdynticker.allcoin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

/**
 * @author andre
 *
 */
public class AllcoinExchange extends Exchange {
	/**
	 * @param experiedPeriod
	 */
	public AllcoinExchange(long experiedPeriod) {
		super(experiedPeriod);
	}

	public AllcoinExchange() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.github.andrefbsantos.libdynticker.core.Exchange#getPairsFromAPI()
	 */
	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode elements = (new ObjectMapper()).readTree(new URL("https://www.allcoin.com/api2/pairs")).get("data");
		Iterator<String> fieldNames = elements.getFieldNames();

		while (fieldNames.hasNext()) {
			String element = fieldNames.next();
			// Status : 1: online -1: closed temporarily -2: closed permanently
			// Don't add when markets closed permanently
			if (elements.get(element).get("status").getTextValue().equals("1") || elements.get(element).get("status").getTextValue().equals("-1")) {
				String[] split = element.split("_");
				String coin = split[0];
				String exchange = split[1];
				Pair pair = new Pair(coin, exchange);
				pairs.add(pair);
			}
		}

		return pairs;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.github.andrefbsantos.libdynticker.core.Exchange#getTicker(com.github.andrefbsantos.
	 * libdynticker.core.Pair)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.andrefbsantos.libdynticker.core.Exchange#getTicker(com.github.andrefbsantos.
	 * libdynticker.core.Pair)
	 */
	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
			IOException {
		// https://www.allcoin.com/api2/pair/LTC_BTC
		String url = "https://www.allcoin.com/api2/pair/" + pair.getCoin() + "_" + pair.getExchange();
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		if (node.get("code").getIntValue() > 0) {
			return parseJSON(node, pair);
		} else {
			throw new MalformedURLException(node.get("data").getTextValue());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.github.andrefbsantos.libdynticker.core.Exchange#parseJSON(org.codehaus.jackson.JsonNode,
	 * com.github.andrefbsantos.libdynticker.core.Pair)
	 */
	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("data").get("trade_price").getTextValue();
	}
}
