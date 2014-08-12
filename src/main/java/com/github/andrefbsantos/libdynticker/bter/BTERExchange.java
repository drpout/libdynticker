package com.github.andrefbsantos.libdynticker.bter;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
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
public class BTERExchange extends Exchange {

	/**
	 *
	 */
	private static final long serialVersionUID = -7381868474342713326L;

	public BTERExchange(long experiedPeriod) {
		super("BTER", experiedPeriod);
	}

	protected String getTickerURL(Pair pair) {
		return "http://data.bter.com/api/1/ticker/" + pair.getCoin() + "_" + pair.getExchange();
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("last").toString();
	}

	@Override
	public double getLastValue(Pair pair) throws IOException {
		URL url = new URL(this.getTickerURL(pair));
		URLConnection uc = url.openConnection();

		// BTER doesn't awnser calls from java, this property masks it as a browser call
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

		uc.connect();
		JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream());
		return Double.parseDouble(this.parseJSON(node, pair));
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		URL url = new URL(this.getTickerURL(pair));
		URLConnection uc = url.openConnection();
		// BTER doesn't awnser calls from java, this property masks it as a browser call
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		return parseJSON(new ObjectMapper().readTree(uc.getInputStream()), pair);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();

		URL url = new URL("http://data.bter.com/api/1/pairs");
		URLConnection uc = url.openConnection();

		// BTER doesn't answer calls from java, this property masks it as a browser call
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

		uc.connect();

		Iterator<JsonNode> elements = (new ObjectMapper()).readTree(uc.getInputStream()).getElements();

		while (elements.hasNext()) {
			String element = elements.next().getTextValue();
			String[] split = element.split("_");
			String coin = split[0].toUpperCase();
			String exchange = split[1].toUpperCase();
			pairs.add(new Pair(coin, exchange));
		}

		return pairs;
	}
}
