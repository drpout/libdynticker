package com.github.andrefbsantos.libdynticker.mintpal;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

/**
 * 
 */

/**
 * Specialization of Exchange for MintPal
 * 
 * @author andre
 * 
 */
public class MintPalExchange extends Exchange {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.andrefbsantos.libdynticker.core.Exchange#getPairs()
	 */
	@Override
	public List<Pair> getPairs() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();

		Iterator<JsonNode> elements = (new ObjectMapper())
				.readTree(new URL("https://api.mintpal.com/v2/market/summary/"))
				.get("data").getElements();

		while (elements.hasNext()) {
			JsonNode element = elements.next();
			String coin = element.get("code").getTextValue();
			String exchange = element.get("exchange").getTextValue();
			pairs.add(new Pair(coin, exchange));
		}

		return pairs;
	}

	@Override
	protected String getTickerURL(Pair pair) {
		return "https://api.mintpal.com/v2/market/stats/" + pair.getCoin() + "/"
				+ pair.getExchange();
	}

	@Override
	protected String parseJSON(JsonNode node, Pair pair) {
		return node.get("data").get("last_price").getTextValue();
	}
}
