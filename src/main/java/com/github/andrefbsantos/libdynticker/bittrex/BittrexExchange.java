package com.github.andrefbsantos.libdynticker.bittrex;

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
 * @author andre
 *
 */
public class BittrexExchange extends Exchange{

	@Override
	public List<Pair> getPairs() throws IOException {

		List<Pair> pairs = new ArrayList<Pair>();

		Iterator<JsonNode> elements = (new ObjectMapper()).readTree(new URL("https://bittrex.com/api/v1.1/public/getmarkets")).get("result").getElements();

		while (elements.hasNext()) {
			JsonNode element = elements.next();
			String coin = element.get("MarketCurrency").getTextValue();
			String exchange = element.get("BaseCurrency").getTextValue();
			pairs.add(new Pair(coin, exchange));
		}
		
		return pairs;
	}

	protected String getTickerURL(Pair pair) {
		return "https://bittrex.com/api/v1.1/public/getticker?market="+pair.getCoin()+"-"+pair.getExchange();
	}

	protected String parseJSON(JsonNode node, Pair pair) {
		return node.get("result").get("Last").toString();
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		return parseJSON(new ObjectMapper().readTree(new URL(this.getTickerURL(pair))), pair);
	}

}
