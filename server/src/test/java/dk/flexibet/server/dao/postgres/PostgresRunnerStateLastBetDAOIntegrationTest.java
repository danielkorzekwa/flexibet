package dk.flexibet.server.dao.postgres;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.marketobserver.model.MarketRunner;
import dk.bot.marketobserver.model.RunnerPrice;
import dk.flexibet.server.dao.RunnerStateDAO;
import dk.flexibet.server.dao.RunnerStateLastBetDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-flexibet-server-dao.xml","file:src/test/resources/spring/ds-test.xml" })
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "txManager")
@Transactional
public class PostgresRunnerStateLastBetDAOIntegrationTest {

	@Resource(name="runnerStateLastBetDao")
	private RunnerStateLastBetDAO runnerStateLastBetDao; 

	@Resource(name="runnerStateDao")
	private RunnerStateDAO runnerStateDao;
	
	private int runnerStateId;
	
	@Before
	public void setUp() {
		runnerStateDao.saveRunnerState("machine1", 1, 2, "noBets");
		runnerStateId = runnerStateDao.findRunnerState("machine1", 1, 2).getId();
	}
	
	@Test
	public void testAddBet() {
		
		
		assertNull(runnerStateLastBetDao.findLastBet(runnerStateId,"B"));
		runnerStateLastBetDao.addBet(runnerStateId, 3, new Date(1000), "B", 2, 2,1,1,new MarketRunner(1,0,0,0,0,0,new ArrayList<RunnerPrice>()),false,-10,0.1);
		assertNotNull("Bet not found", runnerStateLastBetDao.findLastBet(runnerStateId,"B"));
		
		runnerStateLastBetDao.addBet(runnerStateId, 3, new Date(1000), "B", 10, 5,8,3,new MarketRunner(1,0,0,0,0,0,new ArrayList<RunnerPrice>()),true,10,0.3);
		RunnerBet bet = runnerStateLastBetDao.findLastBet(runnerStateId,"B");
		assertNotNull("Bet not found", bet);
		assertEquals(runnerStateId, bet.getRunnerStateId());
		assertEquals(3, bet.getBetId());
		assertEquals(1000, bet.getPlacedDate().getTime());
		assertEquals("B", bet.getBetType());
		assertEquals(10, bet.getPrice(), 0);
		assertEquals(5, bet.getSize(), 0);

		assertEquals(8, bet.getAvgPriceMatched(), 0);	
		assertEquals(3, bet.getSizeMatched(), 0);
		assertEquals(1000, bet.getMatchedDate().getTime());
		assertEquals(0, bet.getSizeCancelled(), 0);
	}
	
	@Test
	public void testAddBetsBackAndLay() {
		
		assertNull(runnerStateLastBetDao.findLastBet(runnerStateId,"L"));
		assertNull(runnerStateLastBetDao.findLastBet(runnerStateId,"B"));
		runnerStateLastBetDao.addBet(runnerStateId, 3, new Date(1000), "L", 10, 5,8,3,new MarketRunner(1,0,0,0,0,0,new ArrayList<RunnerPrice>()),true,10,0.3);
		runnerStateLastBetDao.addBet(runnerStateId, 4, new Date(1000), "B", 2, 2,1,1,new MarketRunner(1,0,0,0,0,0,new ArrayList<RunnerPrice>()),true,10,0.3);
		
		RunnerBet layBet = runnerStateLastBetDao.findLastBet(runnerStateId,"L");
		RunnerBet backBet = runnerStateLastBetDao.findLastBet(runnerStateId,"B");
		assertNotNull("Bet not found", layBet);
		assertNotNull("Bet not found", backBet);
			
		assertEquals(runnerStateId, layBet.getRunnerStateId());
		assertEquals(3, layBet.getBetId());
		assertEquals(1000, layBet.getPlacedDate().getTime());
		assertEquals("L", layBet.getBetType());
		assertEquals(10, layBet.getPrice(), 0);
		assertEquals(5, layBet.getSize(), 0);

		assertEquals(8, layBet.getAvgPriceMatched(), 0);	
		assertEquals(3, layBet.getSizeMatched(), 0);
		assertEquals(1000, layBet.getMatchedDate().getTime());
		assertEquals(0, layBet.getSizeCancelled(), 0);
		
		assertEquals(runnerStateId, backBet.getRunnerStateId());
		assertEquals(4, backBet.getBetId());
		assertEquals(1000, backBet.getPlacedDate().getTime());
		assertEquals("B", backBet.getBetType());
		assertEquals(2, backBet.getPrice(), 0);
		assertEquals(2, backBet.getSize(), 0);

		assertEquals(1, backBet.getAvgPriceMatched(), 0);	
		assertEquals(1, backBet.getSizeMatched(), 0);
		assertEquals(1000, backBet.getMatchedDate().getTime());
		assertEquals(0, backBet.getSizeCancelled(), 0);
		
	}
	
	@Test
	public void testFindBet() {
		assertNull(runnerStateLastBetDao.findBet(3));
		runnerStateLastBetDao.addBet(runnerStateId, 3, new Date(1000), "B", 2, 2,1,1,new MarketRunner(1,0,0,0,0,0,new ArrayList<RunnerPrice>()),true,10,0.3);
		assertNotNull("Bet not found", runnerStateLastBetDao.findBet(3));
		
		runnerStateLastBetDao.addBet(runnerStateId, 3, new Date(1000), "B", 10, 5,8,3,new MarketRunner(1,0,0,0,0,0,new ArrayList<RunnerPrice>()),true,10,0.3);
		RunnerBet bet = runnerStateLastBetDao.findBet(3);
		assertNotNull("Bet not found", bet);
		assertEquals(3, bet.getBetId());
		assertEquals(1000, bet.getPlacedDate().getTime());
		assertEquals("B", bet.getBetType());
		assertEquals(10, bet.getPrice(), 0);
		assertEquals(5, bet.getSize(), 0);

		assertEquals(8, bet.getAvgPriceMatched(), 0);	
		assertEquals(3, bet.getSizeMatched(), 0);
		assertEquals(1000, bet.getMatchedDate().getTime());
		assertEquals(0, bet.getSizeCancelled(), 0);

	}
	
	@Test
	public void testCancelBet() {
		assertNull(runnerStateLastBetDao.findLastBet(runnerStateId,"B"));
		runnerStateLastBetDao.addBet(runnerStateId, 30, new Date(1000), "B", 10, 5,8,2,new MarketRunner(1,0,0,0,0,0,new ArrayList<RunnerPrice>()),true,10,0.3);
		assertNotNull("Bet not found",runnerStateLastBetDao.findLastBet(runnerStateId,"B"));
		
		runnerStateLastBetDao.cancelBet(30, 3, 2);
		RunnerBet bet = runnerStateLastBetDao.findLastBet(runnerStateId,"B");
		assertNotNull("Bet not found", bet);
		assertEquals(30, bet.getBetId());
		
		assertEquals(2, bet.getSizeMatched(), 0);
		assertEquals(3, bet.getSizeCancelled(), 0);
	
	}
	
	@Test
	public void testUpdateBet2() {
		assertNull(runnerStateLastBetDao.findLastBet(runnerStateId,"B"));
		runnerStateLastBetDao.addBet(runnerStateId, 300, new Date(1000), "B", 10, 5,8,2,new MarketRunner(1,0,0,0,0,0,new ArrayList<RunnerPrice>()),true,10,0.3);
		assertNotNull("Bet not found",runnerStateLastBetDao.findLastBet(runnerStateId,"B"));
		runnerStateLastBetDao.updateBet(300, 9, 4, new Date(200));
		
		RunnerBet bet = runnerStateLastBetDao.findLastBet(runnerStateId,"B");

		assertNotNull("Bet not found", bet);
		assertEquals(300, bet.getBetId());
		assertEquals(1000, bet.getPlacedDate().getTime());
		assertEquals("B", bet.getBetType());
		assertEquals(10, bet.getPrice(), 0);
		assertEquals(5, bet.getSize(), 0);

		assertEquals(9, bet.getAvgPriceMatched(), 0);
		assertEquals(4, bet.getSizeMatched(), 0);
		assertEquals(200, bet.getMatchedDate().getTime());
		assertEquals(0, bet.getSizeCancelled(), 0);
		
	}
	
}
