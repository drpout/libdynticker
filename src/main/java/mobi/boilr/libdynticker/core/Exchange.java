package mobi.boilr.libdynticker.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Abstract template for Exchange
 */
public abstract class Exchange {

	private long expiredPeriod;
	private List<Pair> pairs;
	private Timestamp timestamp = null;
	private String name;
	private static final int CONN_TIMEOUT = 8000;

	public Exchange(String name, long expiredPeriod) {
		setExpiredPeriod(expiredPeriod);
		this.name = name;
	}

	/**
	 * @param pair of exchange/coin
	 *
	 * @return Returns the last value of the exchange for a given pair
	 * coin/exchange. We have to use a double because some exchanges measure
	 * values in satoshis (10^-8). A float has just 24 bits of precision which
	 * is not enough to represent 8 decimal places.
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public double getLastValue(Pair pair) throws NumberFormatException, IOException {
		double lastValue = Double.parseDouble(getTicker(pair));
		return lastValue;
	}

	final public List<Pair> getPairs() throws IOException {
		long currentTime = System.currentTimeMillis();
		if(timestamp == null) {
			pairs = getPairsFromAPI();
			timestamp = new Timestamp(currentTime);
			return pairs;
		}
		else if((currentTime - getTimestamp().getTime()) < getExpiredPeriod()) {
			return pairs;
		}
		else {
			/*
			 * TODO throw a custom exception where there is no internet
			 * connection. The exception includes the previous list of pairs and
			 * the timestamp.
			 */
			return pairs = getPairsFromAPI();
		}
	}

	/**
	 * Get pairs from a remote API, specific for each exchange
	 *
	 * @return List of traded pairs
	 * @throws IOException
	 */
	protected abstract List<Pair> getPairsFromAPI() throws IOException;

	/**
	 *
	 * @param pair
	 * @return Json with ticker information
	 * @throws IOException
	 */
	protected abstract String getTicker(Pair pair) throws IOException;

	protected JsonNode readJsonFromUrl(String url) throws IOException {
		URLConnection urlConnection = buildConnection(url);
		urlConnection.connect();
		return (new ObjectMapper()).readTree(urlConnection.getInputStream());
	}

	protected URLConnection buildConnection(String url) throws IOException, MalformedURLException {
		URLConnection urlConnection = (new URL(url)).openConnection();
		/*
		 * Some exchanges return an HTTP 403 error (Forbidden) when you try to
		 * access the API with an undefined User-Agent.
		 */
		urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		urlConnection.setConnectTimeout(CONN_TIMEOUT);
		urlConnection.setReadTimeout(CONN_TIMEOUT);
		return urlConnection;
	}

	public abstract String parseTicker(JsonNode node, Pair pair) throws IOException;

	protected Timestamp getTimestamp() {
		return timestamp;
	}

	public long getExpiredPeriod() {
		return expiredPeriod;
	}

	public void setExpiredPeriod(long expiredPeriod) {
		this.expiredPeriod = expiredPeriod;
	}

	public String getName() {
		return name;
	}
}