package dk.flexibet.server.mbean.accountstatement.model;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class StateMachineStatTest {

	private StateMachineStat stateMachineStat;
	
	@Before
	public void setUp() {
		Map<String, StatesStat> statesStatMap = new HashMap<String, StatesStat>();
		
		StatesStat statesStat = new StatesStat("machine1","state1");
		statesStat.setGreenRunners(1);
		statesStat.setWhiteRunners(2);
		statesStat.setRedRunners(3);
		statesStat.setProfitLossGreen(4);
		statesStat.setProfitLossRed(5);
		statesStatMap.put(statesStat.getStateName(),statesStat);
		
		StatesStat statesStat2 = new StatesStat("machine1","state2");
		statesStat2.setGreenRunners(10);
		statesStat2.setWhiteRunners(20);
		statesStat2.setRedRunners(30);
		statesStat2.setProfitLossGreen(40);
		statesStat2.setProfitLossRed(50);
		statesStatMap.put(statesStat2.getStateName(),statesStat2);
		
		stateMachineStat = new StateMachineStat("machine1",statesStatMap);
	}
	
	@Test
	public void testGetStateMachineId() {
		assertEquals("machine1", stateMachineStat.getStateMachineId());
	}

	@Test
	public void testGetStatesStat() {
		assertEquals(2, stateMachineStat.getStatesStat().size());
	}

	@Test
	public void testGetProfitLossGreen() {
		assertEquals(44, stateMachineStat.getProfitLossGreen(),0);
	}

	@Test
	public void testGetProfitLossRed() {
		assertEquals(55, stateMachineStat.getProfitLossRed(),0);
	}

	@Test
	public void testGetGreenRunners() {
		assertEquals(11, stateMachineStat.getGreenRunners());
	}

	@Test
	public void testGetWhiteRunners() {
		assertEquals(22, stateMachineStat.getWhiteRunners());
	}

	@Test
	public void testGetRedRunners() {
		assertEquals(33, stateMachineStat.getRedRunners());
	}

	@Test
	public void testGetTotalRunners() {
		assertEquals(66, stateMachineStat.getTotalRunners());
	}

	@Test
	public void testGetProfitLossTotal() {
		assertEquals(99, stateMachineStat.getProfitLossTotal(),0);
	}

}
