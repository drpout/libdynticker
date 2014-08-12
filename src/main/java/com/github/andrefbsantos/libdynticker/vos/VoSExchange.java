package com.github.andrefbsantos.libdynticker.vos;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class VoSExchange extends Exchange {

	public VoSExchange(long experiedPeriod) {
		super("VoS", experiedPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		String url = "https://api.vaultofsatoshi.com/public/currency";
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		Iterator<JsonNode> elements = node.get("data").getElements();
		List<String> coins = new ArrayList<String>();
		List<String> exchanges = new ArrayList<String>();

		exchanges.add("USD");
		exchanges.add("CAD");

		while (elements.hasNext()) {
			JsonNode next = elements.next();
			String code = next.get("code").getTextValue();
			coins.add(code);
			exchanges.add(code);
		}

		for (String coin : coins) {
			for (String exchange : exchanges) {
				if (!coin.equals(exchange)) {
					Pair pair = new Pair(coin, exchange);
					pairs.add(pair);
				}
			}
		}

		return pairs;

	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://api.vaultofsatoshi.com//public/recent_transactions?order_currency=" + pair.getCoin().toLowerCase() + "&payment_currency=" + pair.getExchange().toLowerCase() + "&count=1";
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if (node.get("status").getTextValue().equals("success")) {
			Iterator<JsonNode> elements = node.get("data").getElements();
			if (elements.hasNext()) {
				JsonNode next = elements.next();
				return next.get("price").get("value").getTextValue();
			} else {
				// When no trade is found, returns 0. That's how blue trade handles markets with no
				// transactions
				return "0";
			}
		} else {
			throw new IOException();
		}
	}
}
