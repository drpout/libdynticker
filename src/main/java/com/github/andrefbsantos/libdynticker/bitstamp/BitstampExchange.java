package com.github.andrefbsantos.libdynticker.bitstamp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

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

	/**
	 *
	 */
	private static final long serialVersionUID = 8420288560764284730L;

	public BitstampExchange(long experiedPeriod) {
		super("Bitstamp", experiedPeriod);
	}

	protected String getTickerURL(Pair pair) {
		return "https://www.bitstamp.net/api/ticker/";
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("last").getTextValue();
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		return parseJSON(new ObjectMapper().readTree(new URL(this.getTickerURL(pair))), pair);
	}

	@Override
	protected List<Pair> getPairsFromAPI() {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "USD"));
		return pairs;
	}
}
