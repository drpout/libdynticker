package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public final class CEXIOExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "USD"));
		tempPairs.add(new Pair("GHS", "USD"));
		tempPairs.add(new Pair("LTC", "USD"));
		tempPairs.add(new Pair("DOGE", "USD"));
		tempPairs.add(new Pair("DRK", "USD"));
		tempPairs.add(new Pair("GHS", "BTC"));
		tempPairs.add(new Pair("LTC", "BTC"));
		tempPairs.add(new Pair("DOGE", "BTC"));
		tempPairs.add(new Pair("DRK", "BTC"));
		tempPairs.add(new Pair("NMC", "BTC"));
		tempPairs.add(new Pair("IXC", "BTC"));
		tempPairs.add(new Pair("POT", "BTC"));
		tempPairs.add(new Pair("ANC", "BTC"));
		tempPairs.add(new Pair("MEC", "BTC"));
		tempPairs.add(new Pair("WDC", "BTC"));
		tempPairs.add(new Pair("FTC", "BTC"));
		tempPairs.add(new Pair("DGB", "BTC"));
		tempPairs.add(new Pair("USDE", "BTC"));
		tempPairs.add(new Pair("MYR", "BTC"));
		tempPairs.add(new Pair("AUR", "BTC"));
		tempPairs.add(new Pair("GHS", "LTC"));
		tempPairs.add(new Pair("DOGE", "LTC"));
		tempPairs.add(new Pair("DRK", "LTC"));
		tempPairs.add(new Pair("MEC", "LTC"));
		tempPairs.add(new Pair("WDC", "LTC"));
		tempPairs.add(new Pair("ANC", "LTC"));
		tempPairs.add(new Pair("FTC", "LTC"));
		tempPairs.add(new Pair("BTC", "EUR"));
		tempPairs.add(new Pair("LTC", "EUR"));
		tempPairs.add(new Pair("DOGE", "EUR"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public CEXIOExchange(long expiredPeriod) {
		super("CEX.IO", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		// Waiting for
		// https://support.cex.io/hc/communities/public/questions/203516116--API-Add-method-to-retrieve-list-of-traded-pairs
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		URLConnection urlConnection = (new URL("https://cex.io/api/last_price/"
				+ pair.getCoin() + "/" + pair.getExchange())).openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		urlConnection.connect();
		JsonNode node = (new ObjectMapper()).readTree(urlConnection.getInputStream());
		if(node.has("error"))
			throw new MalformedURLException(node.get("error").getTextValue());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		return node.get("lprice").getTextValue();
	}

}
