package com.github.andrefbsantos.libdynticker.bitstamp;

import java.util.ArrayList;
import java.util.List;

import com.github.andrefbsantos.core.Exchange;
import com.github.andrefbsantos.core.Pair;

/**
 * Specialization of exchange for Bitstamp.com
 * 
 * @author andre
 * 
 * 
 */
public class BitstampExchange extends Exchange {

	public BitstampExchange() {
		super("https://www.bitstamp.net/api/ticker/");
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
	public String getLastValue(Pair pair) {
		super.connect();
		return null;
	}
}
