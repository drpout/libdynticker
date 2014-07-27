package com.github.andrefbsantos.libdynticker.bitstamp;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

/**
 * Specialization of exchange for Bitstamp.com
 * 
 * @author andre
 * 
 * 
 */
public class BitstampExchange extends Exchange {

	public BitstampExchange() {
		super("https://www.bitstamp.net/api/ticker/", "last");
	}

	/**
	 * 
	 * @return Returns a list of pairs coin/exchange
	 */
	@Override
	public List<Pair> getPairs() {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "USD"));
		return pairs;
	}

	@Override
	protected String prepareURL(String coin, String exchange) {
		return url;
	}

	@Override
	protected String parseJSON(JsonNode node, String coin, String exchange) {
		return node.get(this.getLastValueProperty()).getTextValue();
	}
}
