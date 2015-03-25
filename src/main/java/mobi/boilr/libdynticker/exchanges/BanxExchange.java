package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public class BanxExchange extends Exchange {

	public BanxExchange(long expiredPeriod) {
		super("Banx Capital", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		Iterator<JsonNode> elements = readJsonFromUrl("https://www.banx.io/SimpleAPI?a=markets").getElements();
		List<Pair> pairs = new ArrayList<Pair>();
		while(elements.hasNext()){
			String pair = elements.next().asText();
			String[] split = pair.split("/");
			pairs.add(new Pair(split[0], split[1]));
		}

		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://www.banx.io/GetPrices?c=" +
				pair.getExchange() + "&p=" + pair.getCoin());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("price").asText();
	}
}
