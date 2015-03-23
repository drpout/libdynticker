package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class DSXExchange extends Exchange {
	private static final List<Pair> PAIRS;
	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC","USD"));
		pairs.add(new Pair("BTC","EUR"));
		pairs.add(new Pair("LTC","BTC"));
		pairs.add(new Pair("LTC","USD"));
		pairs.add(new Pair("LTC","EUR"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public DSXExchange(long expiredPeriod) {
		super("Digital Securities Exchange", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		 return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		HttpsURLConnection uc = (HttpsURLConnection) buildConnection("https://dsx.uk/api/ticker/" +
				pair.getCoin().toLowerCase() + pair.getExchange().toLowerCase() + "?mode=DEMO");
		uc.setRequestProperty("Cookie", "_gat=1; _ym_visorc_27507645=w; i18next=en; _ga=GA1.2.1040986257.1426094211; __zlcmid=TheUiZvEtgPvNe");
		uc.setRequestMethod("POST");
		uc.connect();
		JsonNode node = new ObjectMapper().readTree(uc.getInputStream());
		if(node.has(pair.getCoin().toLowerCase() + pair.getExchange().toLowerCase())) {
			return parseTicker(node, pair);
		} else {
			throw new MalformedURLException();
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get(pair.getCoin().toLowerCase() +
				pair.getExchange().toLowerCase()).get("last").asText();
	}

}
