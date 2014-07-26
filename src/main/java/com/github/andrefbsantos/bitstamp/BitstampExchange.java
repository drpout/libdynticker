package com.github.andrefbsantos.bitstamp;

import java.util.ArrayList;
import java.util.List;

import com.github.andrefbsantos.core.Exchange;
import com.github.andrefbsantos.core.Pair;

public class BitstampExchange extends Exchange {

	public BitstampExchange() {
		super("https://www.bitstamp.net/api/ticker/");
	}

	@Override
	public List<Pair> getPairs() {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "USD"));
		return pairs;
	}

	@Override
	public String getLastValue() {
		super.connect();
		return null;
	}
}
