package com.github.andrefbsantos.libdynticker.core;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.reflections.Reflections;

/**
 * Abstract template for Exchange
 *
 * @author andre
 *
 */
public abstract class Exchange {

	private long experiedPeriod;
	private List<Pair> pairs;
	private Timestamp timestamp = null;
	private String name;

	public Exchange(String name, long experiedPeriod) {
		this.setExperiedPeriod(experiedPeriod);
		this.name = name;
	}

	// /**
	// * Initialize with a period of one week(7*24*60*60*1000)
	// *
	// */
	// public Exchange(String name) {
	// this.setExperiedPeriod(604800000);
	// this.name = name;
	// }

	/**
	 *
	 *
	 * @param pair
	 *            of exchange/coin
	 *
	 * @return Returns the last value of the exchange for a given pair
	 *         coin/exchange. We have to use a double because some exchanges
	 *         measure values in satoshis (10^-8). A float has just 24 bits
	 *         of precision which is not enough to represent 8 decimal places.
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public double getLastValue(Pair pair) throws NumberFormatException, IOException {
		double lastValue = Double.parseDouble(this.getTicker(pair));
		return lastValue;
	}

	final public List<Pair> getPairs() throws IOException {
		long currentTime = System.currentTimeMillis();
		if(timestamp == null) {
			this.pairs = this.getPairsFromAPI();
			timestamp = new Timestamp(currentTime);
			return this.pairs;
		} else if((currentTime - getTimestamp().getTime()) < getExperiedPeriod()) {
			return this.pairs;
		} else {
			// TODO throw a custom exception where there is no internet connection. The exception
			// includes the previous list of pairs and the timestamp.
			return this.pairs = this.getPairsFromAPI();
		}
	}

	/**
	 * Get pairs from a remote API, specificy for each exchange
	 *
	 * @return
	 * @throws IOException
	 */
	protected abstract List<Pair> getPairsFromAPI() throws IOException;

	/**
	 *
	 * @param pair
	 * @return
	 * @throws IOException
	 */
	protected abstract String getTicker(Pair pair) throws IOException;

	public abstract String parseJSON(JsonNode node, Pair pair) throws IOException;

	/**
	 * @return the timestamp
	 */
	protected Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the experiedPeriod
	 */
	public long getExperiedPeriod() {
		return experiedPeriod;
	}

	/**
	 * @param experiedPeriod the experiedPeriod to set
	 */
	public void setExperiedPeriod(long experiedPeriod) {
		this.experiedPeriod = experiedPeriod;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public static Set<Class<? extends Exchange>> getExchanges() {
		Reflections reflections = new Reflections("com.github.andrefbsantos.libdynticker");
		Set<Class<? extends Exchange>> exchanges = reflections.getSubTypesOf(Exchange.class);
		return exchanges;
	}
}