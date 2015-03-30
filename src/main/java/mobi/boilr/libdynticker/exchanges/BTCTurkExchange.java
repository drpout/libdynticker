package mobi.boilr.libdynticker.exchanges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Pair;

public final class BTCTurkExchange extends BTCTraderExchange {

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "TL"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public BTCTurkExchange(long expiredPeriod) {
		super("BTCTurk", expiredPeriod, "https://www.btcturk.com/");
	}
}
