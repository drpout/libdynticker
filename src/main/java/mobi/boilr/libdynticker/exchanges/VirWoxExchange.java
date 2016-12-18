package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import mobi.boilr.libdynticker.core.exception.NoMarketDataException;

public final class VirWoxExchange extends Exchange {

	public VirWoxExchange(long expiredPeriod) {
		super("VirWox", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		JsonNode node = readJsonFromUrl("http://api.virwox.com/api/json.php?method=getInstruments");
		JsonNode result = node.get("result");
		if(result.asText().equals("null"))
			throw new IOException(node.get("error").asText());
		else {
			for(JsonNode inst : result)
				pairs.add(new Pair(inst.get("longCurrency").asText(), inst.get("shortCurrency").asText()));
		}
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("http://api.virwox.com/api/json.php?method=getRawTradeData&instrument=" +
				pair + "&timespan=172800");
		String errorCode = node.get("result").get("errorCode").asText();
		if(errorCode.equals("OK"))
			return parseTicker(node, pair);
		else
			throw new IOException(errorCode);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		JsonNode data = node.get("result").get("data");
		if(data.asText().equals("null")) {
			throw new NoMarketDataException(pair);
		} else {
			Iterator<JsonNode> elements = data.getElements();
			JsonNode last = null;
			while(elements.hasNext())
				last = elements.next();
			return last.get("price").asText();
		}
	}
}