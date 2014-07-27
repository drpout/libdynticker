package com.github.andrefbsantos.libdynticker.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;

/**
 * Abstract template for Exchange
 * 
 * @author andre
 * 
 */
public abstract class Exchange {

	private String url;

	public Exchange(String url) {
		this.url = url;
	}

	/**
	 * 
	 * @return Returns a list of pairs coin/exchange
	 */
	public abstract List<Pair> getPairs();

	/**
	 * 
	 * 
	 * @param pair
	 *            of exchange/coin
	 * 
	 * @return Returns the last value of the exchange for a given pair
	 *         coin/exchange
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws JsonProcessingException
	 */
	public abstract String getLastValue(Pair pair)
			throws JsonProcessingException, MalformedURLException, IOException;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
