package dk.flexibet.server.mbean.accountstatement;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.util.MathUtils;
import org.junit.Test;

import dk.flexibet.server.mbean.accountstatement.model.MarketRunnerStatement;
import dk.flexibet.server.model.DetailedAccountStatementItem;

public class StateStatHelperGetMarketRunnersStatementTest {

	@Test
	public void testGetLastTwoWeeksStatesStat() {

		List<DetailedAccountStatementItem> items = new ArrayList<DetailedAccountStatementItem>();
		items.add(getItem("machine0","noBets",1, 1,100, 2));
		items.add(getItem("machine0","noBets",1, 1,101, -5));
		
		/** commission must be substracted*/
		items.add(getItem("machine1","backPlaced",1, 2,102, -0.1));
		items.add(getItem("machine1","backPlaced",1, 2,102, 4));
		items.add(getItem("machine1","backPlaced",1, 2,103, -3));
		
		List<MarketRunnerStatement> statements = StateStatHelper.getMarketRunnerStatements(items);
		
		assertEquals("machine1", statements.get(1).getStateMachineId());
		assertEquals("backPlaced", statements.get(1).getStateName());
		assertEquals(1, statements.get(1).getMarketId());
		assertEquals(2, statements.get(1).getSelectionId());
		assertEquals(0.9, statements.get(1).getAmount(),0.00001);
		
		assertEquals("machine0", statements.get(0).getStateMachineId());
		assertEquals("noBets", statements.get(0).getStateName());
		assertEquals(1, statements.get(0).getMarketId());
		assertEquals(1, statements.get(0).getSelectionId());
		assertEquals(-3, MathUtils.round(statements.get(0).getAmount(),2),0);
	
	}
	
	private static DetailedAccountStatementItem getItem(String stateMachineId,String stateName,int marketId, int selectionId, int betId, double amount) {
		DetailedAccountStatementItem item = new DetailedAccountStatementItem();
		item.setStateMachineId(stateMachineId);
		item.setStateName(stateName);
		item.setMarketId(marketId);
		item.setSelectionId(selectionId);
		item.setBetId(betId);
		item.setAmount(amount);
		
		return item;
		
	}
	
	
}
