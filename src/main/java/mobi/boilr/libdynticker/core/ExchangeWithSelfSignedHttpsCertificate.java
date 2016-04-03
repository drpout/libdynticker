package mobi.boilr.libdynticker.core;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mobi.boilr.libdynticker.core.exception.NoMarketDataException;


public abstract class ExchangeWithSelfSignedHttpsCertificate extends Exchange {
	private SSLSocketFactory trustAllCertsFactory;
	private final HostnameVerifier allHostsValid = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};
	
	public ExchangeWithSelfSignedHttpsCertificate(String name, long expiredPeriod) {
		super(name, expiredPeriod);
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
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		noCheckCertificate();
		List<Pair> pairs = doGetPairsFromAPI();
		doCheckCertificate();
		return pairs;
	}

	protected abstract List<Pair> doGetPairsFromAPI();

	@Override
	protected String getTicker(Pair pair) throws IOException,
			NoMarketDataException {
		noCheckCertificate();
		String ticker = doGetTicker(pair);
		doCheckCertificate();
		return ticker;
	}
	
	protected abstract String doGetTicker(Pair pair);

	private void doCheckCertificate() {
		HttpsURLConnection.setDefaultSSLSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
	}

	private void noCheckCertificate() {
		HttpsURLConnection.setDefaultSSLSocketFactory(trustAllCertsFactory);
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

}
