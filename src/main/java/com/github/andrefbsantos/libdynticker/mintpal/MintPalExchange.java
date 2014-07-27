package com.github.andrefbsantos.libdynticker.mintpal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.github.andrefbsantos.libdynticker.Exchange;
import com.github.andrefbsantos.libdynticker.Pair;

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

	/**
	 */
	public MintPalExchange() {
		super("https://api.mintpal.com", "last_price");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.andrefbsantos.libdynticker.core.Exchange#getPairs()
	 */
	@Override
	public List<Pair> getPairs() throws JsonProcessingException,
			MalformedURLException, IOException {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.andrefbsantos.libdynticker.core.Exchange#getLastValue(com.
	 * github.andrefbsantos.libdynticker.core.Pair)
	 */

	@Override
	protected String prepareURL(String coin, String exchange) {
		return "https://api.mintpal.com/v2/market/stats/" + coin + "/"
				+ exchange;
	}

	@Override
	protected String parseJSON(JsonNode node, String coin, String exchange) {
		return node.get("data").get(this.lastValueProperty).getTextValue();
	}
}
