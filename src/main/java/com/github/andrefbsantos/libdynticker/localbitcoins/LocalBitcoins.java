package com.github.andrefbsantos.libdynticker.localbitcoins;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class LocalBitcoins extends Exchange {

	public LocalBitcoins(long experiedPeriod) {
		super("LocalBitcoins", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		String url = "https://localbitcoins.com/bitcoinaverage/ticker-all-currencies/";
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<String> exchanges = node.getFieldNames();

		while (exchanges.hasNext()) {
			String exchange = exchanges.next();
			// LocalBitcoins only deals with BTC to others currencies
			Pair pair = new Pair("BTC", exchange);
			pairs.add(pair);
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://localbitcoins.com/bitcoinaverage/ticker-all-currencies/";
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if (node.has(pair.getExchange().toUpperCase())) {
			return node.get(pair.getExchange()).get("rates").get("last").getTextValue();
		} else {
			throw new IOException();
		}
	}
}
