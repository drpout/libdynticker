package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public final class CoinMktExchange extends Exchange {
	public static final long COINMKT_DELAY = 15000;
	private Random random = new Random();

	public CoinMktExchange(long expiredPeriod) {
		super("CoinMkt", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws JsonProcessingException, MalformedURLException,
	IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = (new ObjectMapper()).readTree(new URL("https://api.coinmkt.com/v1/currency/pairs/" + random.nextInt()));
		if(node.get("Code").getIntValue() < 0) {
			throw new IOException(node.get("Err").getTextValue());
		}
		Iterator<JsonNode> elements = node.get("Pairs").getElements();
		for (String[] pairSplit; elements.hasNext();) {
			pairSplit = elements.next().get("Pair").getTextValue().split("_");
			pairs.add(new Pair(pairSplit[0], pairSplit[1]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws JsonProcessingException, MalformedURLException,
			IOException {
		// https://api.coinmkt.com/v1/ticker/RANDOMKEY/DOGE_BTC/1
		JsonNode node = (new ObjectMapper()).readTree(new URL("https://api.coinmkt.com/v1/ticker/"
				+ random.nextInt() + "/" + pair.getCoin() + "_" + pair.getExchange() + "/1"));
		if(node.get("Code").getIntValue() < 0) {
			throw new IOException(node.get("Err").getTextValue());
		}
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("Last").asText();
	}

}
