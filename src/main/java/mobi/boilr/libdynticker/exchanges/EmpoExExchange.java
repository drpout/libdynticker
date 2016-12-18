package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class EmpoExExchange extends Exchange {

	public EmpoExExchange(long expiredPeriod) {
		super("EmpoEX", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		
		for(JsonNode node : readJsonFromUrl("https://api.empoex.com/marketinfo")){
			String[] split = node.get("pairname").asText().split("-");
			pairs.add(new Pair(split[0], split[1]));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException,
			NoMarketDataException {
		JsonNode node = readJsonFromUrl("https://api.empoex.com/marketinfo/"+pair.getCoin()+"-"+pair.getExchange());
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException,
			NoMarketDataException {
		for(JsonNode n : node){
			if(n.get("pairname").asText().equals(pair.getCoin()+"-"+pair.getExchange()))
				return n.get("last").asText().replace(",", "");
		}
		throw new NoMarketDataException(pair);
	}

}
