package mobi.boilr.libdynticker.exchanges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Pair;

public final class BTCExchangePHExchange extends BTCTraderExchange {

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "PHP"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public BTCExchangePHExchange(long expiredPeriod) {
		super("BTCExchange", expiredPeriod, "https://btcexchange.ph/");
	}
}