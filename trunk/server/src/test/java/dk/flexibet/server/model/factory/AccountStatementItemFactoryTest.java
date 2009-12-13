package dk.flexibet.server.model.factory;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dk.bot.betfairservice.model.BFAccountStatementItem;
import dk.bot.bettingengine.dao.model.RunnerState;
import dk.flexibet.server.dao.RunnerStateDAO;
import dk.flexibet.server.model.DetailedAccountStatementItem;

@RunWith(JMock.class)
public class AccountStatementItemFactoryTest {

	private Mockery context = new JUnit4Mockery();
	private RunnerStateDAO runnerStateDAO = context.mock(RunnerStateDAO.class);
	
	private List<BFAccountStatementItem> bfItems;
	final RunnerState runnerState = new RunnerState();
	
	@Before
	public void setUp() {
		bfItems = new ArrayList<BFAccountStatementItem>();
		bfItems.add(creaate(500));
		bfItems.add(creaate(501));
	
		
		runnerState.setStateMachineId("default");
		runnerState.setStateName("noBets");
		context.checking(new Expectations() {
			{
				one(runnerStateDAO).findRunnerState(500l);
				will(returnValue(runnerState));
				
				one(runnerStateDAO).findRunnerState(501);
				will(returnValue(null));

			}
		});
	}
	
	@Test
	public void testCreate() {
		
		List<DetailedAccountStatementItem> items = AccountStatementItemFactory.create(bfItems,runnerStateDAO);
		
		assertEquals(bfItems.size(), items.size());
		
		for(int i=0;i<bfItems.size();i++) {
			BFAccountStatementItem bfItem = bfItems.get(i);
			DetailedAccountStatementItem item = items.get(i);
			
			assertEquals(bfItem.getMarketId(), item.getMarketId());
			assertEquals(bfItem.getMarketName(), item.getMarketName());
			assertEquals(bfItem.getMarketType(), item.getMarketType());
			assertEquals(bfItem.getFullMarketName(), item.getFullMarketName());
			assertEquals(bfItem.getSelectionId(), item.getSelectionId());
			assertEquals(bfItem.getSelectionName(), item.getSelectionName());
			assertEquals(bfItem.getBetId(), item.getBetId());
			assertEquals(bfItem.getEventTypeId(), item.getEventTypeId());
			assertEquals(bfItem.getBetCategoryType(), item.getBetCategoryType());
			assertEquals(bfItem.getBetType(), item.getBetType());
			assertEquals(bfItem.getBetSize(), item.getBetSize(),0);
			assertEquals(bfItem.getAvgPrice(), item.getAvgPrice(),0);
			assertEquals(bfItem.getPlacedDate(), item.getPlacedDate());
			assertEquals(bfItem.getStartDate(), item.getStartDate());
			assertEquals(bfItem.getSettledDate(), item.getSettledDate());
			assertEquals(bfItem.isCommission(), item.isCommission());
			assertEquals(bfItem.getAmount(), item.getAmount(),0);
			
			if(item.getBetId()==500) {
			assertEquals(runnerState.getStateMachineId(),item.getStateMachineId());
			assertEquals(runnerState.getStateName(),item.getStateName());
			}
			else if(item.getBetId()==501) {
				assertEquals(null,item.getStateMachineId());
				assertEquals(null,item.getStateName());
			}
		}
	}
	
	private BFAccountStatementItem creaate(long betId) {
		BFAccountStatementItem item = new BFAccountStatementItem();
		item.setMarketId(5);
		item.setMarketName("Man vs Arsenal");
		item.setMarketType("Winner");
		item.setFullMarketName("Man vs Arsenal 12/05/09");
		item.setSelectionId(11);
		item.setSelectionName("Man");
		item.setBetId(betId);
		item.setEventTypeId(556);
		item.setBetCategoryType("E");
		item.setBetType("L");
		item.setBetSize(6);
		item.setAvgPrice(3.67);
		item.setPlacedDate(new Date(3400));
		item.setStartDate(new Date(3500));
		item.setSettledDate(new Date(3600));
		item.setCommission(true);
		item.setAmount(12);
		
		return item;
		
	}

}
