package mobi.boilr.libdynticker.core.exception;

import java.io.IOException;

import mobi.boilr.libdynticker.core.Pair;

public class NoMarketDataException extends IOException {

	private static final long serialVersionUID = -5012861240224300772L;

	private final Pair pair;
	
	public NoMarketDataException(Pair pair) {
		super("No market data for " + pair);
		this.pair = pair;
	}

	public Pair getPair() {
		return pair;
	}
}
