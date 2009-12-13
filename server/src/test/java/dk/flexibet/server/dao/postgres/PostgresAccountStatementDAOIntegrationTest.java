package dk.flexibet.server.dao.postgres;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import dk.flexibet.server.dao.AccountStatementDAO;
import dk.flexibet.server.model.DetailedAccountStatementItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-flexibet-server-dao.xml",
		"file:src/test/resources/spring/ds-test.xml" })
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "txManager")
@Transactional
public class PostgresAccountStatementDAOIntegrationTest {

	@Resource(name = "accountStatementDao")
	private AccountStatementDAO dao;
	
	@Test
	public void testAdd() {

		long now = System.currentTimeMillis();
		List<DetailedAccountStatementItem> items = new ArrayList<DetailedAccountStatementItem>();
		items.add(getAccountStatement(1, 2, 3, new Date(now), 4, false));
		items.add(getAccountStatement(1, 2, 3, new Date(now), 4, false));
		items.add(getAccountStatement(1, 2, 3, new Date(now + 1), 4, true));
		items.add(getAccountStatement(1, 2, 3, new Date(now + 1), 4, true));

		dao.add(items);

		List<DetailedAccountStatementItem> itemsFromDb = dao.getItems(new Date(now), new Date(now + 1),true);
		assertEquals(4, itemsFromDb.size());

	}

	@Test
	public void testGetItems() {

		long now = System.currentTimeMillis();
		List<DetailedAccountStatementItem> items = new ArrayList<DetailedAccountStatementItem>();
		items.add(getAccountStatement(1, 2, 3, new Date(now), 4, false));
		items.add(getAccountStatement(1, 2, 3, new Date(now), 0.2, true));

		dao.add(items);

		List<DetailedAccountStatementItem> itemsFromDb = dao.getItems(new Date(now), new Date(now + 1),true);
		assertEquals(2, itemsFromDb.size());

		/** Check for commission item */
		assertEquals(1, itemsFromDb.get(0).getMarketId());
		assertEquals("market name", itemsFromDb.get(0).getMarketName());
		assertEquals("market type", itemsFromDb.get(0).getMarketType());
		assertEquals("full market name", itemsFromDb.get(0).getFullMarketName());
		assertEquals(2, itemsFromDb.get(0).getSelectionId());
		assertEquals("selection name", itemsFromDb.get(0).getSelectionName());
		assertEquals(3, itemsFromDb.get(0).getBetId());
		assertEquals(7, itemsFromDb.get(0).getEventTypeId());
		assertEquals("C", itemsFromDb.get(0).getBetCategoryType());
		assertEquals("B", itemsFromDb.get(0).getBetType());
		assertEquals(6.5, itemsFromDb.get(0).getBetSize(), 0);
		assertEquals(2.34d, itemsFromDb.get(0).getAvgPrice(), 0);
		assertEquals(new Date(1000), itemsFromDb.get(0).getPlacedDate());
		assertEquals(new Date(2000), itemsFromDb.get(0).getStartDate());
		assertEquals(new Date(now), itemsFromDb.get(0).getSettledDate());
		assertEquals(true, itemsFromDb.get(0).isCommission());
		assertEquals(0.2, itemsFromDb.get(0).getAmount(), 0);
		assertEquals("default", itemsFromDb.get(0).getStateMachineId());
		assertEquals("noBets", itemsFromDb.get(0).getStateName());

		/** Check for non-commission item. */
		assertEquals(1, itemsFromDb.get(1).getMarketId());
		assertEquals("market name", itemsFromDb.get(1).getMarketName());
		assertEquals("market type", itemsFromDb.get(1).getMarketType());
		assertEquals("full market name", itemsFromDb.get(1).getFullMarketName());
		assertEquals(2, itemsFromDb.get(1).getSelectionId());
		assertEquals("selection name", itemsFromDb.get(1).getSelectionName());
		assertEquals(3, itemsFromDb.get(1).getBetId());
		assertEquals(7, itemsFromDb.get(1).getEventTypeId());
		assertEquals("C", itemsFromDb.get(1).getBetCategoryType());
		assertEquals("B", itemsFromDb.get(1).getBetType());
		assertEquals(6.5, itemsFromDb.get(1).getBetSize(), 0);
		assertEquals(2.34d, itemsFromDb.get(1).getAvgPrice(), 0);
		assertEquals(new Date(1000), itemsFromDb.get(1).getPlacedDate());
		assertEquals(new Date(2000), itemsFromDb.get(1).getStartDate());
		assertEquals(new Date(now), itemsFromDb.get(1).getSettledDate());
		assertEquals(false, itemsFromDb.get(1).isCommission());
		assertEquals(4, itemsFromDb.get(1).getAmount(), 0);
		assertEquals("default", itemsFromDb.get(1).getStateMachineId());
		assertEquals("noBets", itemsFromDb.get(1).getStateName());

	}

	@Test
	public void testGetNonBetItems() {

		long now = System.currentTimeMillis();
		List<DetailedAccountStatementItem> items = new ArrayList<DetailedAccountStatementItem>();
		items.add(getAccountStatement(1, 2, 0, new Date(now), 4, false));
		items.add(getAccountStatement(1, 2, 0, new Date(now), 4, false));
		items.add(getAccountStatement(1, 2, 3, new Date(now + 1), 4, false));
		
		dao.add(items);

		List<DetailedAccountStatementItem> itemsFromDb = dao.getItems(new Date(now), new Date(now + 1),false);
		assertEquals(2, itemsFromDb.size());
		assertEquals(0, itemsFromDb.get(0).getBetId());
		assertEquals(0, itemsFromDb.get(1).getBetId());

	}

	@Test
	public void testGetLastestSettledDate() {
		long now = System.currentTimeMillis();
		List<DetailedAccountStatementItem> items = new ArrayList<DetailedAccountStatementItem>();
		items.add(getAccountStatement(1, 2, 3, new Date(now), 4, false));
		items.add(getAccountStatement(1, 2, 3, new Date(now + 1), 4, false));

		dao.add(items);

		assertEquals(new Date(now + 1), dao.getLastestSettledDate());
	}

	@Test
	public void testGetLastestSettledDateDefault() {

		Date latestSettledDate = dao.getLastestSettledDate();

		/** now - 365 days */
		Date now = new Date(System.currentTimeMillis());
		Date oneYearAgo = new Date(System.currentTimeMillis() - (long) 1000 * 3600 * 24 * 30);
		assertTrue(oneYearAgo.getTime() - latestSettledDate.getTime() >= 0
				&& oneYearAgo.getTime() - latestSettledDate.getTime() < 10000);

	}

	@Test
	public void testDeleteItems() {
		long now = System.currentTimeMillis();
		List<DetailedAccountStatementItem> items = new ArrayList<DetailedAccountStatementItem>();
		items.add(getAccountStatement(1, 2, 3, new Date(now), 4, false));
		items.add(getAccountStatement(1, 2, 3, new Date(now + 1), 4, false));

		dao.add(items);
		List<DetailedAccountStatementItem> itemsFromDb = dao.getItems(new Date(now), new Date(now + 1),true);

		dao.deleteItems(new Date(now + 1));
		List<DetailedAccountStatementItem> itemsFromDb2 = dao.getItems(new Date(now), new Date(now + 1),true);

		assertEquals(itemsFromDb2.size() + 1, itemsFromDb.size());

	}

	private DetailedAccountStatementItem getAccountStatement(int marketId, int selectionId, long betId, Date settledDate,
			double amount, boolean commission) {
		DetailedAccountStatementItem item = new DetailedAccountStatementItem();
		item.setMarketId(marketId);
		item.setMarketName("market name");
		item.setMarketType("market type");
		item.setFullMarketName("full market name");

		item.setSelectionId(selectionId);
		item.setSelectionName("selection name");

		item.setBetId(betId);

		item.setEventTypeId(7);
		item.setBetCategoryType("C");
		item.setBetType("B");
		item.setBetSize(6.5);
		item.setAvgPrice(2.34d);

		item.setPlacedDate(new Date(1000));
		item.setStartDate(new Date(2000));
		item.setSettledDate(settledDate);
		item.setAmount(amount);

		item.setCommission(commission);
		
		item.setStateMachineId("default");
		item.setStateName("noBets");

		return item;
	}
}
