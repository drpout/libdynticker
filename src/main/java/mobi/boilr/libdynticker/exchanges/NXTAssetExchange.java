package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public final class NXTAssetExchange extends Exchange {
	private List<String> peers = null;
	private Iterator<String> peersIt = null;
	private long peersTimestamp;
	private static final long MILLIS_DAY = 86400000;

	public NXTAssetExchange(long expiredPeriod) {
		super("NXT Asset Exchange", expiredPeriod);
	}

	private String getPeer() throws JsonProcessingException, MalformedURLException, IOException {
		if(peersIt == null || System.currentTimeMillis() - peersTimestamp > MILLIS_DAY) {
			List<String> newPeers = new ArrayList<String>(30);
			Iterator<JsonNode> elements = (new ObjectMapper()).readTree(new URL("http://nxtpeers.com/api/hallmark.php")).getElements();
			for(JsonNode node; elements.hasNext();) {
				node = elements.next();
				newPeers.add("http://" + node.get("ip").getTextValue() + ":7876/nxt?requestType=");
			}
			if(newPeers.isEmpty()) {
				throw new IOException("Empty NXT peers list.");
			} else {
				peers = newPeers;
				peersIt = peers.iterator();
				peersTimestamp = System.currentTimeMillis();
				System.out.println("Peers fetched: " + peers.toString());
			}
		}
		if(!peersIt.hasNext())
			peersIt = peers.iterator();
		String peer = peersIt.next();
		System.out.println("Using peer: " + peer);
		return peer;
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<JsonNode> elements = (new ObjectMapper()).readTree(new URL(
				getPeer() + "getAllAssets")).get("assets").getElements();
		for(JsonNode node; elements.hasNext();) {
			node = elements.next();
			pairs.add(new Pair(node.get("name").getTextValue(), "NXT", node.get("asset").getTextValue()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = (new ObjectMapper()).readTree(new URL(
				getPeer() + "getTrades&lastIndex=0&asset=" + pair.getMarket()));
		if(node.has("errorCode"))
			throw new MalformedURLException(node.get("errorDescription").getTextValue());
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		// 1 NXT = 10^8 NQT
		Iterator<JsonNode> elements = node.get("trades").getElements();
		String price;
		if(elements.hasNext()) {
			String priceNQT = elements.next().get("priceNQT").getTextValue();
			while(priceNQT.length() < 8) {
				priceNQT = "0" + priceNQT;
			}
			int length = priceNQT.length();
			if(length < 9)
				price = "." + priceNQT;
			else
				price = priceNQT.substring(0, length - 8) + "." + priceNQT.substring(length - 8, length);
		} else {
			price = Double.toString(Double.NaN);
		}
		System.out.println(pair + " " + pair.getMarket() + " " + price);
		return price;
	}
}
