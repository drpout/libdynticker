package com.github.andrefbsantos.libdynticker.btceexchange;

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

public class BTCEExchange extends Exchange {

	public BTCEExchange() {
		super("https://btc-e.com/api/3", "last");
	}

	@Override
	public List<Pair> getPairs() throws JsonProcessingException,
			MalformedURLException, IOException {

		List<Pair> pairs = new ArrayList<Pair>();

		Iterator<String> elements = (new ObjectMapper())
				.readTree(new URL("https://btc-e.com/api/3/info")).get("pairs")
				.getFieldNames();

		while (elements.hasNext()) {
			String element = elements.next();
			String[] split = element.split("_");
			String coin = split[0].toUpperCase();
			String exchange = split[0].toUpperCase();
			pairs.add(new Pair(coin, exchange));
		}
		return pairs;
	}

	@Override
	protected String parseJSON(JsonNode node, String coin, String exchange) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String prepareURL(String coin, String exchange) {
		// TODO Auto-generated method stub
		return null;
	}

}
