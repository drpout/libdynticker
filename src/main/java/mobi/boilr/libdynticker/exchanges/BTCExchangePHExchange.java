package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Pair;

public class BTCExchangePHExchange extends BTCTraderExchange {

	private static final List<Pair> PAIRS;

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "PHP"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public BTCExchangePHExchange(long expiredPeriod) {
		super("BTCExchange", expiredPeriod, "https://btcexchange.ph/");
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}
}