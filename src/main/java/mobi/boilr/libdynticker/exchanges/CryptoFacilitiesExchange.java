package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class CryptoFacilitiesExchange extends Exchange {

	public CryptoFacilitiesExchange(long expiredPeriod) {
		super("Crypto Facilities", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
//		SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
//		SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
//		Calendar c = Calendar.getInstance();
//		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//		c.add(Calendar.DATE, 7);
//		String thisWeek = "F-XBT:USD-" + monthFormat.format(c.getTime()).substring(0, 3) + dayFormat.format(c.getTime());
//		c.add(Calendar.MONTH, 3);
//		String threeMonths = "F-XBT:USD-" + monthFormat.format(c.getTime()).substring(0, 3) + dayFormat.format(c.getTime());
//		c.add(Calendar.MONTH, 3);
//		String sixMonths = "F-XBT:USD-" + monthFormat.format(c.getTime()).substring(0, 3) + dayFormat.format(c.getTime());
		// pairs.add(new Pair(thisWeek, "USD"));
		// pairs.add(new Pair(threeMonths, "USD"));
		// pairs.add(new Pair(sixMonths, "USD"));
		pairs.add(new Pair("F-XBT:USD-Mar15", "USD"));
		pairs.add(new Pair("F-XBT:USD-Jun15", "USD"));
		pairs.add(new Pair("F-XBT:USD-Sep15", "USD"));
		return pairs;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "https://www.cryptofacilities.com/derivatives/api/ticker?tradeable=" + pair.getCoin() + "&unit=" + pair.getExchange();
		JsonNode node = new ObjectMapper().readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if(node.get("result").asText().equals("error")) {
			throw new IOException();
		} else {
			return node.get("last").asText();
		}
	}

}
