package mobi.boilr.libdynticker.exchanges;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;

public final class HuobiExchange extends Exchange {
	private static final List<Pair> pairs;
	static {
		List<Pair> tempPairs = new ArrayList<Pair>();
		tempPairs.add(new Pair("BTC", "CNY"));
		pairs = Collections.unmodifiableList(tempPairs);
	}

	public HuobiExchange(long expiredPeriod) {
		super("Huobi", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		if(!pairs.contains(pair))
			throw new IOException("Invalid pair: " + pair);
		String url = "https://api.huobi.com/staticmarket/detail_btc.js";
		String data = "";
		URLConnection urlConnection = buildConnection(url);
		urlConnection.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String inputLine;
		while((inputLine = in.readLine()) != null)
			data += inputLine;
		in.close();
		return getLast(data, pair);
	}

	protected String getLast(String data, Pair pair) throws IOException {
		Pattern pattern = Pattern.compile("\"p_new\":([0-9.]+),");
		Matcher matcher = pattern.matcher(data);
		if(matcher.find()) {
			return matcher.group(1);
		}
		throw new IOException("No last value for " + pair);
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) {
		return null;
	}
}
