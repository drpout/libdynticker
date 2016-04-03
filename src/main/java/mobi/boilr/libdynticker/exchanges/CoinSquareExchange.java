package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class CoinSquareExchange extends Exchange {

	public CoinSquareExchange( long expiredPeriod) {
		super("CoinSquare", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();				
		for(JsonNode node : readJsonFromUrl("https://coinsquare.io/?method=quotes").get("quotes")){
			pairs.add(new Pair(node.get("ticker").asText(),node.get("base").asText()));
		}
		return pairs ;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException,
			NoMarketDataException {
		JsonNode node = readJsonFromUrl("https://coinsquare.io/?method=quotes");
		return parseTicker(node, pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException,
			NoMarketDataException {
		for(JsonNode n : node.get("quotes")){
			if(n.get("ticker").asText().toUpperCase().equals(pair.getCoin()) && 
					n.get("base").asText().toUpperCase().equals(pair.getExchange())){
				if(pair.getExchange().equals("USD") || pair.getExchange().equals("CAD"))
					return Double.toString(n.get("last").asDouble()/100);
				else
					return Double.toString(n.get("last").asDouble()/1000000);
			}
		}
		throw new NoMarketDataException(pair);
	}

}
