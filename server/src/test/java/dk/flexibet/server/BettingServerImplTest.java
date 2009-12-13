package dk.flexibet.server;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class BettingServerImplTest {

	private BettingServerImpl bettingServer;
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("BOT_HOME", "./src/test/resources");
		bettingServer = new BettingServerImpl();
		
		/**Wait until scheduler starts.*/
		Thread.sleep(3000);
	}
	
	@Test
	public void testIsSchedulerRunning() {
		assertEquals(true,bettingServer.getMBean().isSchedulerRunning());
		bettingServer.getMBean().stopScheduler();
		assertEquals(false,bettingServer.getMBean().isSchedulerRunning());
		bettingServer.getMBean().stopScheduler();
	}
}
