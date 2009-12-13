package dk.flexibet.server.dao.postgres;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import dk.bot.bettingengine.dao.BettingEngineDAO;
import dk.bot.bettingengine.dao.model.RunnerState;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-flexibet-server-dao.xml","file:src/test/resources/spring/ds-test.xml" })
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "txManager")
@Transactional
public class PostgresBettingEngineDAOIntegrationTest {

	@Resource
	BettingEngineDAO bettingEngineDao;
		
	@Test
	public void testFindRunnerStateNotExistYet() {
		String stateMachineId = "default" + System.currentTimeMillis();
		int marketId = new Random(System.currentTimeMillis()).nextInt();
		int selectionId = new Random(System.currentTimeMillis()).nextInt();

		RunnerState runnerState = bettingEngineDao.findRunnerState(stateMachineId, marketId, selectionId,"noBets");

		assertEquals(true, runnerState.getId() > 0);
		assertEquals(stateMachineId, runnerState.getStateMachineId());
		assertEquals(marketId, runnerState.getMarketId());
		assertEquals(selectionId, runnerState.getSelectionId());
		assertEquals("noBets", runnerState.getStateName());
	}
	
	@Test
	public void testFindRunnerStateExist() {
		String stateMachineId = "default" + System.currentTimeMillis();
		int marketId = new Random(System.currentTimeMillis()).nextInt();
		int selectionId = new Random(System.currentTimeMillis()).nextInt();
		
		/**Runner state is created in db because it doesn't exist yet.*/
		bettingEngineDao.findRunnerState(stateMachineId, marketId, selectionId,"noBets");
		
		/**Runner state is returned from db, so defaultStateName parameter is ignored*/
		RunnerState runnerState = bettingEngineDao.findRunnerState(stateMachineId, marketId, selectionId,"layPlaced");
		assertEquals(true, runnerState.getId() > 0);
		assertEquals(stateMachineId, runnerState.getStateMachineId());
		assertEquals(marketId, runnerState.getMarketId());
		assertEquals(selectionId, runnerState.getSelectionId());
		assertEquals("noBets", runnerState.getStateName());
	}
	

}
