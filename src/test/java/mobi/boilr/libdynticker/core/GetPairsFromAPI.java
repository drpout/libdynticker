package mobi.boilr.libdynticker.core;

import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import mobi.boilr.libdynticker.exchanges.BitstampExchange;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BitstampExchange.class)
public class GetPairsFromAPI {
	Exchange exchange;

	@Before
	public void setUp() throws Exception {
		exchange = PowerMockito.spy(new BitstampExchange(1000000000));
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testSecondGetPairsFromAPIDoesActive() throws NotFoundException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, CannotCompileException {
		try {

			exchange.setExpiredPeriod(-1);
			exchange.getPairs();
			verify(exchange, Mockito.times(1)).getPairsFromAPI();
			Assert.assertNotNull(exchange.getTimestamp());
			exchange.getPairs();
			verify(exchange, Mockito.times(2)).getPairsFromAPI();
		}
		catch(IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testSecondGetPairsFromAPIDoesntActive() {
		try {
			// Exchange exchange = PowerMockito.spy(testExchange);
			exchange.setExpiredPeriod(Long.MAX_VALUE);
			exchange.getPairs();
			verify(exchange, Mockito.times(1)).getPairsFromAPI();
			Assert.assertNotNull(exchange.getTimestamp());
			exchange.getPairs();
			verify(exchange, Mockito.times(1)).getPairsFromAPI();
		}
		catch(IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
