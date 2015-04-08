package mobi.boilr.libdynticker.core.exception;

import java.io.IOException;

public class APICallLimitReachedException extends IOException {

	private static final long serialVersionUID = -5038672925279796187L;

	public APICallLimitReachedException() {
		super("API call limit reached.");
	}
}
