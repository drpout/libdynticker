package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

public final class NXTAssetExchange extends Exchange {
	private static final String peer = "http://humanoide.thican.net:7876/nxt?requestType=";
	
	public NXTAssetExchange(long expiredPeriod) {
		super("NXT Asset Exchange", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		for(JsonNode asset : readJsonFromUrl(peer + "getAllAssets").get("assets")) {
			pairs.add(new Pair(asset.get("name").asText(), "NXT", asset.get("asset").asText()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl(peer + "getTrades&lastIndex=0&asset=" + pair.getMarket());
		if(node.has("errorCode"))
			throw new IOException(node.get("errorDescription").asText());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		Iterator<JsonNode> elements = node.get("trades").getElements();
		String price;
		if(elements.hasNext()) {
			String priceNQT = elements.next().get("priceNQT").asText();
			// 1 NXT = 10^8 NQT
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
		return price;
	}
}
