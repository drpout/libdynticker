package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class BanxExchange extends Exchange {

	public BanxExchange(int i) {
		super("Banx Capital", i);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		String url = "https://www.banx.io/SimpleAPI?a=markets";
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		Iterator<JsonNode> elements = node.getElements();
		while(elements.hasNext()){
			String pair = elements.next().asText();
			String[] split = pair.split("/");
			pairs.add(new Pair(split[0], split[1]));
		}

		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://www.banx.io/GetPrices?c=" + pair.getExchange() + "&p=" + pair.getCoin();
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		return node.get("price").asText();
	}
}
