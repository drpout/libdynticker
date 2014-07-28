package com.github.andrefbsantos.libdynticker.core;

public class Pair {
	private String coin;
	private String exchange;

	/**
	 * 
	 * @param coin
	 * @param exchange
	 */
	public Pair(String coin, String exchange) {
		this.coin = coin;
		this.exchange = exchange;
	}

	/**
	 * @return the exchange
	 */
	public String getExchange() {
		return exchange;
	}

	/**
	 * @param exchange
	 *            the exchange to set
	 */
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	/**
	 * @return the coin
	 */
	public String getCoin() {
		return coin;
	}

	/**
	 * @param coin
	 *            the coin to set
	 */
	public void setCoin(String coin) {
		this.coin = coin;
	}

	public String toString() {
		return this.coin + "/" + this.exchange;
	}

}
