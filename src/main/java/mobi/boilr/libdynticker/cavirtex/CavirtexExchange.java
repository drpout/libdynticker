/**
 *
 */
package mobi.boilr.libdynticker.cavirtex;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

/**
 * @author andre
 *
 */
public class CavirtexExchange extends Exchange {

	/**
	 *
	 */
	private static final long serialVersionUID = 8776748943580993440L;

	/**
	 * @param experiedPeriod
	 */
	public CavirtexExchange(long experiedPeriod) {
		super("Cavirtex", experiedPeriod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mobi.boilr.libdynticker.core.Exchange#getPairsFromAPI()
	 */
	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
	IOException {
		String url = "https://www.cavirtex.com/api2/ticker.json";
		List<Pair> pairs = new ArrayList<Pair>();
		URLConnection uc = (new URL(url)).openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream());
		if (node.get("status").getTextValue().equals("ok")) {
			Iterator<String> pairCode = node.get("ticker").getFieldNames();
			while (pairCode.hasNext()) {
				String next = pairCode.next().toString();
				String coin = next.substring(0, 3);
				String exchange = next.substring(3, 6);
				Pair pair = new Pair(coin, exchange);
				pairs.add(pair);
			}
			return pairs;
		} else {
			throw new IOException(node.get("message").getTextValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mobi.boilr.libdynticker.core.Exchange#getTicker(mobi.boilr.
	 * libdynticker.core.Pair)
	 */
	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
	IOException {
		String url = "https://www.cavirtex.com/api2/ticker.json?currencypair=" + pair.getCoin() + pair.getExchange();
		URLConnection uc = (new URL(url)).openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream());
		if (node.get("status").getTextValue().equals("ok")) {
			return this.parseJSON(node, pair);
		} else {
			throw new IOException(node.get("message").getTextValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mobi.boilr.libdynticker.core.Exchange#parseJSON(org.codehaus.jackson.JsonNode,
	 * mobi.boilr.libdynticker.core.Pair)
	 */
	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("ticker").get(pair.getCoin() + pair.getExchange()).get("last").toString();
	}
}