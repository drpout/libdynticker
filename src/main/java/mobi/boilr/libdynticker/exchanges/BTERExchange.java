package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public final class BTERExchange extends Exchange {

	public BTERExchange(long expiredPeriod) {
		super("BTER", expiredPeriod);
	}

	protected String getTickerURL(Pair pair) {
		return "http://data.bter.com/api/1/ticker/" + pair.getCoin() + "_" + pair.getExchange();
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.get("result").getTextValue().equals("true")) {
			JsonNode lastValueNode = node.get("last");
			if(lastValueNode.isTextual())
				return lastValueNode.getTextValue();
			else
				return lastValueNode.toString();
		} else {
			throw new IOException(node.get("message").getTextValue());
		}
	}

	@Override
	public double getLastValue(Pair pair) throws IOException {
		URL url = new URL(this.getTickerURL(pair));
		URLConnection uc = url.openConnection();
		// BTER doesn't awnser calls from java, this property masks it as a browser call
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		JsonNode node = (new ObjectMapper()).readTree(uc.getInputStream());
		return Double.parseDouble(this.parseJSON(node, pair));
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		URL url = new URL(this.getTickerURL(pair));
		URLConnection uc = url.openConnection();
		// BTER doesn't awnser calls from java, this property masks it as a browser call
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		return parseJSON(new ObjectMapper().readTree(uc.getInputStream()), pair);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		URL url = new URL("http://data.bter.com/api/1/pairs");
		URLConnection uc = url.openConnection();
		// BTER doesn't answer calls from java, this property masks it as a browser call
		uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		uc.connect();
		Iterator<JsonNode> elements = (new ObjectMapper()).readTree(uc.getInputStream()).getElements();
		for(String[] split; elements.hasNext();) {
			split = elements.next().getTextValue().toUpperCase().split("_");
			pairs.add(new Pair(split[0], split[1]));
		}
		return pairs;
	}
}
