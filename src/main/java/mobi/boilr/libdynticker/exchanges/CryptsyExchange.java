package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

public final class CryptsyExchange extends Exchange {

	public CryptsyExchange(long expiredPeriod) {
		super("Cryptsy", expiredPeriod);
	}

	protected String getTickerURL(Pair pair) {
		return "http://pubapi.cryptsy.com/api.php?method=singlemarketdata&marketid=" + pair.getMarket();
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.get("success").toString().equals("1")) {
			String lastValue = node.get("return").get("markets").getElements().next().get("lasttradeprice").getTextValue();
			if(lastValue == null) {
				throw new IOException("No last value for " + pair + ".");
			} else {
				return lastValue;
			}
		} else {
			throw new IOException(node.get("error").getTextValue());
		}
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		return parseJSON((new ObjectMapper()).readTree(new URL(getTickerURL(pair))), pair);
	}

	// http://stackoverflow.com/questions/11507231/android-parsing-json-file-with-a-large-property-with-low-memory-usage

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {

		JsonFactory factory = new MappingJsonFactory();
		JsonParser parser = factory
				.createJsonParser(new URL("http://pubapi.cryptsy.com/api.php?method=marketdatav2"));

		List<Pair> pairs = new ArrayList<Pair>();

		parser.nextToken();// Start
		parser.nextToken();// Success
		parser.nextToken();// 1
		parser.nextToken();// return
		parser.nextToken();// markets
		parser.nextToken();// markets' Start
		parser.nextToken();

		for(String id, coin, exchange; parser.nextToken() != JsonToken.END_OBJECT;) {
			parser.nextToken(); // value

			parser.nextToken(); // marketId
			parser.nextToken(); // marketId value
			id = parser.getText();

			parser.nextToken(); // label
			parser.nextToken(); // label's value

			parser.nextToken(); // lasttradeprice
			parser.nextToken(); // lasttradeprice's value

			parser.nextToken(); // volume
			parser.nextToken(); // volume's value

			parser.nextToken(); // lasttradetime
			parser.nextToken(); // lasttradetime's value

			parser.nextToken(); // primaryname
			parser.nextToken();

			parser.nextToken(); // primarycode
			parser.nextToken();
			coin = parser.getText();

			parser.nextToken(); // secondaryname
			parser.nextToken();

			parser.nextToken(); // secondarycode
			parser.nextToken();
			exchange = parser.getText();

			parser.nextToken(); // recentrades
			parser.nextToken();
			parser.skipChildren();

			parser.nextToken(); // sellorders
			parser.nextToken();
			parser.skipChildren();

			parser.nextToken(); // buyorders
			parser.nextToken();
			parser.skipChildren();
			parser.nextToken();
			pairs.add(new Pair(coin, exchange, id));
		}

		return pairs;
	}
}