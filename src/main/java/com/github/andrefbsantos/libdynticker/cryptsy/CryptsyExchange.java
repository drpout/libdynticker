package com.github.andrefbsantos.libdynticker.cryptsy;

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
public class CryptsyExchange extends Exchange {

	public CryptsyExchange(long experiedPeriod) {
		super(experiedPeriod);
	}

	public CryptsyExchange() {
		super();
	}

	protected String getTickerURL(Pair pair) {
		return "http://pubapi.cryptsy.com/api.php?method=singlemarketdata&marketid=" + pair.getMarket();
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("return").get("markets").get(pair.getCoin()).get("lasttradeprice").getTextValue();
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		return parseJSON(new ObjectMapper().readTree(new URL(this.getTickerURL(pair))), pair);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();

		JsonNode markets = (new ObjectMapper()).readTree(new URL("http://pubapi.cryptsy.com/api.php?method=marketdatav2")).get("return").get("markets");

		Iterator<String> fieldNames = markets.getFieldNames();

		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			String coin = markets.get(fieldName).get("primarycode").getTextValue();
			String exchange = markets.get(fieldName).get("secondarycode").getTextValue();
			String market = markets.get(fieldName).get("marketid").getTextValue();
			pairs.add(new Pair(coin, exchange, market));
		}
		return pairs;
	}
}