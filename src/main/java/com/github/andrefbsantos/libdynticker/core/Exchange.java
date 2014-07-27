package com.github.andrefbsantos.libdynticker.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Abstract template for Exchange
 * 
 * @author andre
 * 
 */
public abstract class Exchange {

	protected String url;
	protected String lastValueProperty;

	/**
	 * 
	 * @param url
	 * @param lastValueProperty
	 */
	public Exchange(String url, String lastValueProperty) {
		this.url = url;
		this.lastValueProperty = lastValueProperty;
	}

	/**
	 * 
	 * @return Returns a list of pairs coin/exchange
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws JsonProcessingException
	 */
	public abstract List<Pair> getPairs() throws JsonProcessingException,
			MalformedURLException, IOException;

	/**
	 * 
	 * 
	 * @param pair
	 *            of exchange/coin
	 * 
	 * @return Returns the last value of the exchange for a given pair
	 *         coin/exchange
	 * @throws ExchangeException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws JsonProcessingException
	 */
	public String getLastValue(Pair pair) throws ExchangeException {
		return this.getLastValue(pair.getCoin(), pair.getExchange());
	}

	public String getLastValue(String coin, String exchange)
			throws ExchangeException {

		String apiURL = this.prepareURL(coin, exchange);
		JsonNode node;

		try {
			node = (new ObjectMapper()).readTree(new URL(apiURL));
			return this.parseJSON(node, coin, exchange);

		} catch (JsonProcessingException e) {
			throw new ExchangeException();
		} catch (MalformedURLException e) {
			throw new ExchangeException();
		} catch (IOException e) {
			throw new ExchangeException();
		}

	}

	/**
	 * 
	 * @param node
	 * @param exchange
	 * @param coin
	 * @return
	 */
	protected abstract String parseJSON(JsonNode node, String coin,
			String exchange);

	/**
	 * 
	 * @param exchange
	 * @param coin
	 * @return
	 */
	protected abstract String prepareURL(String coin, String exchange);

	/**
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 * @return
	 */
	public String getLastValueProperty() {
		return lastValueProperty;
	}

	/**
	 * 
	 * @param lastValueProperty
	 */
	public void setLastValueProperty(String lastValueProperty) {
		this.lastValueProperty = lastValueProperty;
	}

}
