package dk.flexibet.server.dao.postgres;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import dk.bot.marketobserver.dao.MarketDetailsDAO;
import dk.bot.marketobserver.model.MarketData;
import dk.bot.marketobserver.model.MarketDetailsRunner;
import dk.flexibet.server.dao.RunnerStateDAO;
import dk.flexibet.server.model.BetsStat;
import dk.flexibet.server.model.RunnerStateBet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-flexibet-server-dao.xml","file:src/test/resources/spring/ds-test.xml" })
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "txManager")
@Transactional
public class PostgresBetsDAOIntegrationTest {

	@Resource
	PostgresBetsDAO betsDao;
	
	@Resource(name="runnerStateDao")
	private RunnerStateDAO runnerStateDao;

	@Resource
	private MarketDetailsDAO marketDetailsDao;
	
	int runnerStateId;
	
	@Test
	public void testGetBetsStat() {
		
		DateTime hourStart = new DateTime();
		DateTime hourEnd = hourStart.plusHours(1);
		
		addRunnerBets(hourStart.plusMinutes(30).toDate());
		List<BetsStat> betsStat = betsDao.getBetsStat(hourStart.toDate(),hourEnd.toDate());
		
		assertEquals(1, betsStat.size());
		assertEquals("machineA", betsStat.get(0).getStateMachineId());
		assertEquals(runnerStateId, betsStat.get(0).getRunnerStateId());
		assertEquals("\\GB\\Ascot 10th Jul", betsStat.get(0).getMenuPath());
		assertEquals("Frozen Power", betsStat.get(0).getSelectionName());
		assertEquals(2, betsStat.get(0).getBetsAmount());
	
	}
	
	@Test
	public void testAddRunnerStateBet() {
		runnerStateDao.saveRunnerState("machineA", 10, 11, "noBets");
		int runnerStateId = runnerStateDao.findRunnerState("machineA", 10, 11).getId();
		betsDao.addRunnerStateBet(runnerStateId, 2, new Date(100), 2.34);
		betsDao.addRunnerStateBet(runnerStateId, 3, new Date(200), 3.1);

		List<RunnerStateBet> runnerStateBets = betsDao.getRunnerStateBets(runnerStateId);

		Collections.sort(runnerStateBets, new Comparator<RunnerStateBet>() {
			public int compare(RunnerStateBet o1, RunnerStateBet o2) {
				return new Long(o2.getBetId()).compareTo(new Long(o1.getBetId()));
			};
		});

		assertEquals(2, runnerStateBets.size());

		assertEquals(2, runnerStateBets.get(1).getBetId());
		assertEquals(runnerStateId, runnerStateBets.get(1).getRunnerStateId());
		assertEquals(new Date(100), runnerStateBets.get(1).getPlacedDate());
		assertEquals(2.34, runnerStateBets.get(1).getPrice(), 0);

		assertEquals(3, runnerStateBets.get(0).getBetId());
		assertEquals(runnerStateId, runnerStateBets.get(0).getRunnerStateId());
		assertEquals(new Date(200), runnerStateBets.get(0).getPlacedDate());
		assertEquals(3.1, runnerStateBets.get(0).getPrice(), 0);

	}
	
	private void addRunnerBets(Date time) {
	runnerStateDao.saveRunnerState("machineA", 10, 11, "noBets");
	runnerStateId = runnerStateDao.findRunnerState("machineA", 10, 11).getId();
	betsDao.addRunnerStateBet(runnerStateId, 2, time,2.34);
	betsDao.addRunnerStateBet(runnerStateId, 3,time,2.36);
	
	List<MarketDetailsRunner> runners = new ArrayList<MarketDetailsRunner>();
	runners.add(new MarketDetailsRunner(11,"Frozen Power"));
	
	MarketData marketDetails = new MarketData();
	marketDetails.setMarketId(10);
	marketDetails.setMenuPath("\\GB\\Ascot 10th Jul");
	marketDetails.setEventDate(new Date(0));
	marketDetails.setSuspendTime(new Date());
	marketDetails.setRunners(runners);	
	marketDetailsDao.saveMarketDetails(marketDetails );
	}

}
