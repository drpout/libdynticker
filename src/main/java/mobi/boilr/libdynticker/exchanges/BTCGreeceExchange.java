package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Pair;

public class BTCGreeceExchange extends BTCTraderExchange {

	private static final List<Pair> PAIRS;

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "EUR"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public BTCGreeceExchange(long expiredPeriod) {
		super("BTCGreece", expiredPeriod, "https://www.btcgreece.com/");
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}
}