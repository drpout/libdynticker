package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public final class CoinswapExchange extends Exchange {
	private SSLSocketFactory defaultFactory, trustAllCertsFactory;
	private HostnameVerifier defaultVerifier, allHostsValid;

	public CoinswapExchange(long expiredPeriod) {
		super("Coin-Swap", expiredPeriod);
		/*
		 * Fix for self-signed certificate of Coin-Swap API.
		 * From http://www.rgagnon.com/javadetails/java-fix-certificate-problem-in-HTTPS.html
		 */
		TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					@Override
					public void checkClientTrusted(X509Certificate[] arg0, String arg1)
							throws CertificateException {
					}

					@Override
					public void checkServerTrusted(X509Certificate[] arg0, String arg1)
							throws CertificateException {
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
				}
		};

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, null);
			trustAllCertsFactory = sc.getSocketFactory();
		} catch(Exception e) {
			e.printStackTrace();
		}

		allHostsValid = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		defaultFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
		defaultVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
	IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		noCheckCertificate();
		Iterator<JsonNode> elements = (new ObjectMapper()).readTree(new URL("https://api.coin-swap.net/market/summary")).getElements();
		doCheckCertificate();
		for(JsonNode element; elements.hasNext();) {
			element = elements.next();
			pairs.add(new Pair(element.get("symbol").getTextValue(), element.get("exchange").getTextValue()));
		}
		return pairs;
	}

	private void doCheckCertificate() {
		HttpsURLConnection.setDefaultSSLSocketFactory(defaultFactory);
		HttpsURLConnection.setDefaultHostnameVerifier(defaultVerifier);
	}

	private void noCheckCertificate() {
		HttpsURLConnection.setDefaultSSLSocketFactory(trustAllCertsFactory);
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
	IOException {
		// https://api.coin-swap.net/market/stats/DOGE/BTC
		String url = "https://api.coin-swap.net/market/stats/" + pair.getCoin() + "/" + pair.getExchange();
		noCheckCertificate();
		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		doCheckCertificate();
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("lastprice").getTextValue();
	}

}
