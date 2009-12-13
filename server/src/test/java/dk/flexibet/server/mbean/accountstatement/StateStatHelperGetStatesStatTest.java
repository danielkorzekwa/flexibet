package dk.flexibet.server.mbean.accountstatement;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dk.flexibet.server.mbean.accountstatement.model.MarketRunnerStatement;
import dk.flexibet.server.mbean.accountstatement.model.StateMachineStat;
import dk.flexibet.server.mbean.accountstatement.model.StatesStat;

public class StateStatHelperGetStatesStatTest {

	@Test
	public void testGetStatesStat() {

		List<MarketRunnerStatement> items = new ArrayList<MarketRunnerStatement>();
	    items.add(getItem("machine1","01_layPlaced",1,1,2));
	    items.add(getItem("machine1","01_layPlaced",1,2,-1));
	    items.add(getItem("machine1","02_backPlaced",1,3,-1));
		
		List<StateMachineStat> statesStat = StateStatHelper.getStatesStat(items);
		
		assertEquals(1, statesStat.size());
		
		assertEquals(2, statesStat.get(0).getStatesStat().size());
		
		StatesStat layPlacedStat = statesStat.get(0).getStatesStat().get("01_layPlaced");
		assertEquals("01_layPlaced",layPlacedStat.getStateName());	
		assertEquals(1,layPlacedStat.getGreenRunners());
		assertEquals(0,layPlacedStat.getWhiteRunners());
		assertEquals(1,layPlacedStat.getRedRunners());
		assertEquals(2,layPlacedStat.getTotalRunners());
		assertEquals(2,layPlacedStat.getProfitLossGreen(),0);
		assertEquals(-1,layPlacedStat.getProfitLossRed(),0);
		assertEquals(1,layPlacedStat.getProfitLossTotal(),0);
		
		StatesStat backPlacedStat = statesStat.get(0).getStatesStat().get("02_backPlaced");
		
		assertEquals("02_backPlaced",backPlacedStat.getStateName());	
		assertEquals(0,backPlacedStat.getGreenRunners());
		assertEquals(0,backPlacedStat.getWhiteRunners());
		assertEquals(1,backPlacedStat.getRedRunners());
		assertEquals(1,backPlacedStat.getTotalRunners());
		assertEquals(0,backPlacedStat.getProfitLossGreen(),0);
		assertEquals(-1,backPlacedStat.getProfitLossRed(),0);
		assertEquals(-1,backPlacedStat.getProfitLossTotal(),0);
		
	}
	
	@Test
	public void testGetStatesStatForTwoStateMachines() {

		List<MarketRunnerStatement> items = new ArrayList<MarketRunnerStatement>();
	    items.add(getItem("machine1","01_layPlaced",1,1,2));
	    items.add(getItem("machine1","01_layPlaced",1,2,-1));
	    items.add(getItem("machine2","02_backPlaced",1,3,-1));
		
		List<StateMachineStat> statesStat = StateStatHelper.getStatesStat(items);
		
		assertEquals(2, statesStat.size());
		assertEquals(1, statesStat.get(0).getStatesStat().size());
		assertEquals(1, statesStat.get(1).getStatesStat().size());
		
		
		StatesStat layPlacedStat = statesStat.get(1).getStatesStat().get("01_layPlaced");
		assertEquals("01_layPlaced",layPlacedStat.getStateName());	
		assertEquals(1,layPlacedStat.getGreenRunners());
		assertEquals(0,layPlacedStat.getWhiteRunners());
		assertEquals(1,layPlacedStat.getRedRunners());
		assertEquals(2,layPlacedStat.getTotalRunners());
		assertEquals(2,layPlacedStat.getProfitLossGreen(),0);
		assertEquals(-1,layPlacedStat.getProfitLossRed(),0);
		assertEquals(1,layPlacedStat.getProfitLossTotal(),0);
		
		StatesStat backPlacedStat = statesStat.get(0).getStatesStat().get("02_backPlaced");
		
		assertEquals("02_backPlaced",backPlacedStat.getStateName());	
		assertEquals(0,backPlacedStat.getGreenRunners());
		assertEquals(0,backPlacedStat.getWhiteRunners());
		assertEquals(1,backPlacedStat.getRedRunners());
		assertEquals(1,backPlacedStat.getTotalRunners());
		assertEquals(0,backPlacedStat.getProfitLossGreen(),0);
		assertEquals(-1,backPlacedStat.getProfitLossRed(),0);
		assertEquals(-1,backPlacedStat.getProfitLossTotal(),0);
		
	}
	
	private static MarketRunnerStatement getItem(String stateMachineId, String stateName, int marketId, int selectionId, double amount) {
		MarketRunnerStatement item = new MarketRunnerStatement(stateMachineId,stateName,marketId, selectionId);
		item.setAmount(amount);
		
		return item;
		
	}
}
