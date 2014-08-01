package com.github.andrefbsantos.libdynticker.core;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.codehaus.jackson.JsonNode;

/**
 * Abstract template for Exchange
 *
 * @author andre
 *
 */
public abstract class Exchange {

	private long experiedPeriod;
	private List<Pair> pairs;
	protected Timestamp timestamp = null;

	public Exchange(long experiedPeriod) {
		this.experiedPeriod = experiedPeriod;
	}

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
	 * @throws IOException
	 */
	public double getLastValue(Pair pair) throws IOException {
		double lastValue = Double.parseDouble(this.getTicker(pair));
		// System.out.println(this.getClass().getSimpleName() + "\t" + pair.toString() + "\t" +
		// lastValue);
		return lastValue;
	}

	final public List<Pair> getPairs() throws IOException {
		long currentTime = System.currentTimeMillis();
		if (timestamp != null && (currentTime - timestamp.getTime()) < experiedPeriod) {
			return this.pairs;
		} else {
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

	public abstract String parseJSON(JsonNode node, Pair pair);
}