package com.github.andrefbsantos.libdynticker.bitstamp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
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
	public String getLastValue(Pair pair) throws JsonProcessingException,
			MalformedURLException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(new URL(this.getUrl()));
		String lastValue = node.get("last").getTextValue();
		System.out.println(lastValue);
		return lastValue;
	}
}
