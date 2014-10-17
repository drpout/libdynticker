package mobi.boilr.libdynticker.core;

import java.io.Serializable;

public final class Pair implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7350917343984132966L;
	private String coin;
	private String exchange;
	private String market;

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
	 * 
	 * @param coin
	 * @param exchange
	 * @param market
	 */
	public Pair(String coin, String exchange, String market) {
		this.coin = coin;
		this.exchange = exchange;
		this.market = market;
	}
	
	@Override
	public boolean equals(Object o){
		return ((Pair) o).getCoin().equals(this.coin) && ((Pair) o).getExchange().equals(this.exchange); 
	}

	@Override
	public String toString() {
		return this.coin + "/" + this.exchange;
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

	/**
	 * @return the market
	 */
	public String getMarket() {
		return market;
	}

	/**
	 * @param market
	 *            the market to set
	 */
	public void setMarket(String market) {
		this.market = market;
	}

}
