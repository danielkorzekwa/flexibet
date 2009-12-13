package dk.flexibet.server.dao.postgres;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;
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

import dk.bot.bettingengine.dao.model.RunnerState;
import dk.flexibet.server.dao.RunnerStateDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-flexibet-server-dao.xml",
		"file:src/test/resources/spring/ds-test.xml" })
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "txManager")
@Transactional
public class PostgresRunnerStateDAOIntegrationTest {

	@Resource(name = "runnerStateDao")
	private RunnerStateDAO runnerStateDao;
	
	@Resource
	PostgresBetsDAO betsDao;

	@Test
	public void testFindSaveRunnerStateNotYetExist() {
		String stateMachineId = "default" + System.currentTimeMillis();
		int marketId = new Random(System.currentTimeMillis()).nextInt();
		int selectionId = new Random(System.currentTimeMillis()).nextInt();

		assertNull(runnerStateDao.findRunnerState(stateMachineId, marketId, selectionId));

		String stateName = "name" + System.currentTimeMillis();

		runnerStateDao.saveRunnerState(stateMachineId, marketId, selectionId, stateName);
		assertEquals(true, runnerStateDao.findRunnerState(stateMachineId, marketId, selectionId).getId() > 0);
		assertEquals(stateMachineId, runnerStateDao.findRunnerState(stateMachineId, marketId, selectionId).getStateMachineId());
		assertEquals(marketId, runnerStateDao.findRunnerState(stateMachineId, marketId, selectionId).getMarketId());
		assertEquals(selectionId, runnerStateDao.findRunnerState(stateMachineId, marketId, selectionId).getSelectionId());
		assertEquals(stateName, runnerStateDao.findRunnerState(stateMachineId, marketId, selectionId).getStateName());

	}
	
	@Test
	public void testSaveMarketDetailsAlreadyExist() {

		String stateName = "name" + System.currentTimeMillis();
		String stateName2 = "name2" + System.currentTimeMillis();

		runnerStateDao.saveRunnerState("machine1", 123, 234, stateName);
		assertEquals(stateName, runnerStateDao.findRunnerState("machine1", 123, 234).getStateName());

		runnerStateDao.saveRunnerState("machine2", 123, 234, stateName2);
		assertEquals(stateName2, runnerStateDao.findRunnerState("machine2", 123, 234).getStateName());

	}
	
	@Test
	public void testFindRunnerStateForBetId() {
		String stateMachineId = "default" + System.currentTimeMillis();
		String stateName = "name" + System.currentTimeMillis();
		int marketId = new Random(System.currentTimeMillis()).nextInt();
		int selectionId = new Random(System.currentTimeMillis()).nextInt();
		runnerStateDao.saveRunnerState(stateMachineId, marketId, selectionId, stateName);
		RunnerState runnerState = runnerStateDao.findRunnerState(stateMachineId, marketId, selectionId);
		long betId=123;
		
		assertNull(runnerStateDao.findRunnerState(betId));
		betsDao.addRunnerStateBet(runnerState.getId(), betId, new Date(100), 2.34);
		
		assertEquals(true, runnerStateDao.findRunnerState(betId).getId() > 0);
		assertEquals(stateMachineId, runnerStateDao.findRunnerState(stateMachineId, marketId, selectionId).getStateMachineId());
		assertEquals(marketId, runnerStateDao.findRunnerState(stateMachineId, marketId, selectionId).getMarketId());
		assertEquals(selectionId, runnerStateDao.findRunnerState(stateMachineId, marketId, selectionId).getSelectionId());
		assertEquals(stateName, runnerStateDao.findRunnerState(stateMachineId, marketId, selectionId).getStateName());
	}
}
