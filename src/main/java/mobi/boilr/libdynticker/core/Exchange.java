package mobi.boilr.libdynticker.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

/**
 * Abstract template for Exchange
 */
public abstract class Exchange {

	private long expiredPeriod;
	private List<Pair> pairs;
	private Timestamp timestamp = null;
	private String name;
	private static final int CONN_TIMEOUT = 8000;
	private ObjectMapper mapper = new ObjectMapper();

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
	 * @throws NoMarketDataException
	 */
	public double getLastValue(Pair pair) throws NumberFormatException, IOException, NoMarketDataException {
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
	 * @throws NoMarketDataException
	 */
	protected abstract String getTicker(Pair pair) throws IOException, NoMarketDataException;

	protected JsonNode readJsonFromUrl(String url) throws IOException {
		URLConnection urlConnection = buildConnection(url);
		urlConnection.connect();
		return mapper.readTree(urlConnection.getInputStream());
	}

	protected JsonNode readJsonFromUrl(String url, String postData) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) buildConnection(url);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", "" + Integer.toString(postData.getBytes().length));
		conn.setUseCaches(false);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(postData);
		wr.flush();
		wr.close();
		return mapper.readTree(conn.getInputStream());
	}

	protected URLConnection buildConnection(String url) throws IOException, MalformedURLException {
		URLConnection urlConnection = (new URL(url)).openConnection();
		/*
		 * Some exchanges return an HTTP 403 error (Forbidden) when you try to
		 * access the API with an undefined User-Agent.
		 */
		urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		// Others return HTTP 406 (Not Acceptable) when Accept is undefined.
		urlConnection.addRequestProperty("Accept", "*/*");
		urlConnection.setConnectTimeout(CONN_TIMEOUT);
		urlConnection.setReadTimeout(CONN_TIMEOUT);
		return urlConnection;
	}

	public abstract String parseTicker(JsonNode node, Pair pair) throws IOException, NoMarketDataException;

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