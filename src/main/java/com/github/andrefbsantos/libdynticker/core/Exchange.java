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
	private Timestamp timestamp = null;

	public Exchange(long experiedPeriod) {
		this.setExperiedPeriod(experiedPeriod);
	}

	/**
	 * Initialize with a period of one week(7*24*60*60*1000)
	 *
	 */
	public Exchange() {
		this.setExperiedPeriod(604800000);
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
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public double getLastValue(Pair pair) throws NumberFormatException, IOException {
		double lastValue = Double.parseDouble(this.getTicker(pair));
		return lastValue;
	}

	final public List<Pair> getPairs() throws IOException {
		long currentTime = System.currentTimeMillis();
		if (timestamp == null) {
			this.pairs = this.getPairsFromAPI();
			timestamp = new Timestamp(currentTime);
			return this.pairs;
		} else if ((currentTime - getTimestamp().getTime()) < getExperiedPeriod()) {
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

	/**
	 * @return the timestamp
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
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
}