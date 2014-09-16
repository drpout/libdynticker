package mobi.boilr.libdynticker.core;

import java.util.Set;

import org.junit.After;
import org.junit.Before;

public class GetAllExchanges {

	private Set<Class<? extends Exchange>> exchanges;

	@Before
	public void setUp() throws Exception {
		exchanges = Exchange.getExchanges();
	}

	@After
	public void tearDown() throws Exception {
	}
}
