/**
 *
 */
package com.github.andrefbsantos.libdynticker.bleutrade;

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
public class BleuTradeExchange extends Exchange {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1042920630414897319L;

	public BleuTradeExchange(long experiedPeriod) {
		super(experiedPeriod);
	}

	public BleuTradeExchange() {
		super();
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://bleutrade.com/api/v2/public/getticker?market=" + pair.getExchange() + "_" + pair.getCoin();

		(new ObjectMapper()).readTree(new URL(url)).get("result").getElements().next().get("Last").getTextValue();

		return parseJSON((new ObjectMapper()).readTree(new URL(url)), pair);
	}

	protected String getTickerURL(Pair pair) {
		return "https://bleutrade.com/api/v1/" + pair.getCoin() + "_" + pair.getExchange();
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("result").getElements().next().get("Last").getTextValue();
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();

		Iterator<JsonNode> elements = (new ObjectMapper()).readTree(new URL("https://bleutrade.com/api/v2/public/getmarkets")).get("result").getElements();

		while (elements.hasNext()) {
			JsonNode next = elements.next();
			boolean isActive = next.get("IsActive").getTextValue().equals("true");
			if (isActive) {
				String coin = next.get("MarketCurrency").getTextValue();
				String exchange = next.get("BaseCurrency").getTextValue();
				Pair pair = new Pair(coin, exchange);
				pairs.add(pair);
			} else {
				System.out.println("aaa");
			}

		}
		return pairs;
	}
}