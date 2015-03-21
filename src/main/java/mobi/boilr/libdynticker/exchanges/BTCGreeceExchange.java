package mobi.boilr.libdynticker.exchanges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Pair;

public class BTCGreeceExchange extends BTCTraderExchange {

	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "EUR"));
		PAIRS = Collections.unmodifiableList(pairs);
	}

	public BTCGreeceExchange(long expiredPeriod) {
		super("BTCGreece", expiredPeriod, "https://www.btcgreece.com/");
	}


}