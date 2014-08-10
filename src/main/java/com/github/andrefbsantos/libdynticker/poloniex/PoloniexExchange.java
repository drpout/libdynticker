package com.github.andrefbsantos.libdynticker.poloniex;

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
public class PoloniexExchange extends Exchange {

	/**
	 *
	 */
	private static final long serialVersionUID = -2215369226500768293L;

	public PoloniexExchange(long experiedPeriod) {
		super("Poloniex", experiedPeriod);
	}

	public PoloniexExchange() {
		super("Poloniex");
	}

	protected String getTickerURL(Pair pair) {
		return "https://poloniex.com/public?command=returnTicker";
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get(pair.getExchange() + "_" + pair.getCoin()).get("last").getTextValue();
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		return parseJSON(new ObjectMapper().readTree(new URL(this.getTickerURL(pair))), pair);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();

		Iterator<String> elements = (new ObjectMapper()).readTree(new URL("https://poloniex.com/public?command=returnTicker")).getFieldNames();

		while (elements.hasNext()) {
			String element = elements.next();
			String[] split = element.split("_");
			String coin = split[1];
			String exchange = split[0];
			pairs.add(new Pair(coin, exchange));
		}

		return pairs;
	}

}
