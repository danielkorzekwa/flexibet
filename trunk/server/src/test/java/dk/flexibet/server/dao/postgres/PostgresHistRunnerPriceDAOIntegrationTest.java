package dk.flexibet.server.dao.postgres;

import static org.junit.Assert.assertEquals;

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

import dk.flexibet.server.dao.HistRunnerPriceDAO;
import dk.flexibet.server.model.HistRunnerPrice;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-flexibet-server-dao.xml", "classpath:spring/ds-test.xml" })
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "txManager")
@Transactional
public class PostgresHistRunnerPriceDAOIntegrationTest {

	@Resource
	private HistRunnerPriceDAO histRunnerPriceDao;


	@Test
	public void testFindSaveRunnerPricesForMarket() {
		List<HistRunnerPrice> histRunnerPrices = createHistRunnerPrice();

		histRunnerPriceDao.saveRunnerPrices(histRunnerPrices);

		List<HistRunnerPrice> runnnerPricesFromDb = histRunnerPriceDao.findRunnerPrices(1,1);

		assertEquals(20, runnnerPricesFromDb.size());

		assertEquals(1, runnnerPricesFromDb.get(0).getMarketId());
		assertEquals(1, runnnerPricesFromDb.get(0).getSelectionId());
		assertEquals(0, runnnerPricesFromDb.get(0).getRecordTime().getTime());
		assertEquals(20, runnnerPricesFromDb.get(0).getLastPriceMatched(),0);
		assertEquals(2, runnnerPricesFromDb.get(0).getPriceToBack(),0);
		assertEquals(4, runnnerPricesFromDb.get(0).getPriceToLay(),0);
			
		assertEquals(1, runnnerPricesFromDb.get(1).getMarketId());
		assertEquals(1, runnnerPricesFromDb.get(1).getSelectionId());
		assertEquals(1, runnnerPricesFromDb.get(1).getRecordTime().getTime());
		assertEquals(21, runnnerPricesFromDb.get(1).getLastPriceMatched(),0);
		assertEquals(3, runnnerPricesFromDb.get(1).getPriceToBack(),0);
		assertEquals(5, runnnerPricesFromDb.get(1).getPriceToLay(),0);
		
		assertEquals(1, runnnerPricesFromDb.get(19).getMarketId());
		assertEquals(1, runnnerPricesFromDb.get(19).getSelectionId());
		assertEquals(19, runnnerPricesFromDb.get(19).getRecordTime().getTime());
		assertEquals(39, runnnerPricesFromDb.get(19).getLastPriceMatched(),0);
		assertEquals(21, runnnerPricesFromDb.get(19).getPriceToBack(),0);
		assertEquals(23, runnnerPricesFromDb.get(19).getPriceToLay(),0);
	}
	
	@Test
	public void testFindSaveRunnerPricesForMarketAndSelection() {
		List<HistRunnerPrice> histRunnerPrices = createHistRunnerPrice();

		histRunnerPriceDao.saveRunnerPrices(histRunnerPrices);

		List<HistRunnerPrice> runnnerPricesFromDb = histRunnerPriceDao.findRunnerPrices(1);

		assertEquals(40, runnnerPricesFromDb.size());

		assertEquals(1, runnnerPricesFromDb.get(0).getMarketId());
		assertEquals(0, runnnerPricesFromDb.get(0).getSelectionId());
		assertEquals(0, runnnerPricesFromDb.get(0).getRecordTime().getTime());
		assertEquals(20, runnnerPricesFromDb.get(0).getLastPriceMatched(),0);
		assertEquals(2, runnnerPricesFromDb.get(0).getPriceToBack(),0);
		assertEquals(4, runnnerPricesFromDb.get(0).getPriceToLay(),0);

		assertEquals(1, runnnerPricesFromDb.get(1).getMarketId());
		assertEquals(1, runnnerPricesFromDb.get(1).getSelectionId());
		assertEquals(0, runnnerPricesFromDb.get(1).getRecordTime().getTime());
		assertEquals(20, runnnerPricesFromDb.get(1).getLastPriceMatched(),0);
		assertEquals(2, runnnerPricesFromDb.get(1).getPriceToBack(),0);
		assertEquals(4, runnnerPricesFromDb.get(1).getPriceToLay(),0);
		
		assertEquals(1, runnnerPricesFromDb.get(2).getMarketId());
		assertEquals(0, runnnerPricesFromDb.get(2).getSelectionId());
		assertEquals(1, runnnerPricesFromDb.get(2).getRecordTime().getTime());
		assertEquals(21, runnnerPricesFromDb.get(2).getLastPriceMatched(),0);
		assertEquals(3, runnnerPricesFromDb.get(2).getPriceToBack(),0);
		assertEquals(5, runnnerPricesFromDb.get(2).getPriceToLay(),0);
		
		assertEquals(1, runnnerPricesFromDb.get(3).getMarketId());
		assertEquals(1, runnnerPricesFromDb.get(3).getSelectionId());
		assertEquals(1, runnnerPricesFromDb.get(3).getRecordTime().getTime());
		assertEquals(21, runnnerPricesFromDb.get(3).getLastPriceMatched(),0);
		assertEquals(3, runnnerPricesFromDb.get(3).getPriceToBack(),0);
		assertEquals(5, runnnerPricesFromDb.get(3).getPriceToLay(),0);
		
		assertEquals(1, runnnerPricesFromDb.get(38).getMarketId());
		assertEquals(0, runnnerPricesFromDb.get(38).getSelectionId());
		assertEquals(19, runnnerPricesFromDb.get(38).getRecordTime().getTime());
		assertEquals(39, runnnerPricesFromDb.get(38).getLastPriceMatched(),0);
		assertEquals(21, runnnerPricesFromDb.get(38).getPriceToBack(),0);
		assertEquals(23, runnnerPricesFromDb.get(38).getPriceToLay(),0);
		
		assertEquals(1, runnnerPricesFromDb.get(39).getMarketId());
		assertEquals(1, runnnerPricesFromDb.get(39).getSelectionId());
		assertEquals(19, runnnerPricesFromDb.get(39).getRecordTime().getTime());
		assertEquals(39, runnnerPricesFromDb.get(39).getLastPriceMatched(),0);
		assertEquals(21, runnnerPricesFromDb.get(39).getPriceToBack(),0);
		assertEquals(23, runnnerPricesFromDb.get(39).getPriceToLay(),0);
	}
	
	@Test
	public void testFindSaveRunnerPricesForMarketAndTimePeriod() {
		List<HistRunnerPrice> histRunnerPrices = createHistRunnerPrice();

		histRunnerPriceDao.saveRunnerPrices(histRunnerPrices);

		List<HistRunnerPrice> runnnerPricesFromDb = histRunnerPriceDao.findRunnerPrices(1, new Date(8), new Date(12));

		assertEquals(10, runnnerPricesFromDb.size());

		assertEquals(1, runnnerPricesFromDb.get(0).getMarketId());
		assertEquals(1, runnnerPricesFromDb.get(0).getSelectionId());
		assertEquals(8, runnnerPricesFromDb.get(0).getRecordTime().getTime());
		assertEquals(28, runnnerPricesFromDb.get(0).getLastPriceMatched(),0);
		assertEquals(10, runnnerPricesFromDb.get(0).getPriceToBack(),0);
		assertEquals(12, runnnerPricesFromDb.get(0).getPriceToLay(),0);

		assertEquals(1, runnnerPricesFromDb.get(1).getMarketId());
		assertEquals(0, runnnerPricesFromDb.get(1).getSelectionId());
		assertEquals(8, runnnerPricesFromDb.get(1).getRecordTime().getTime());
		assertEquals(28, runnnerPricesFromDb.get(1).getLastPriceMatched(),0);
		assertEquals(10, runnnerPricesFromDb.get(1).getPriceToBack(),0);
		assertEquals(12, runnnerPricesFromDb.get(1).getPriceToLay(),0);
		
		assertEquals(1, runnnerPricesFromDb.get(2).getMarketId());
		assertEquals(1, runnnerPricesFromDb.get(2).getSelectionId());
		assertEquals(9, runnnerPricesFromDb.get(2).getRecordTime().getTime());
		assertEquals(29, runnnerPricesFromDb.get(2).getLastPriceMatched(),0);
		assertEquals(11, runnnerPricesFromDb.get(2).getPriceToBack(),0);
		assertEquals(13, runnnerPricesFromDb.get(2).getPriceToLay(),0);
		
		assertEquals(1, runnnerPricesFromDb.get(3).getMarketId());
		assertEquals(0, runnnerPricesFromDb.get(3).getSelectionId());
		assertEquals(9, runnnerPricesFromDb.get(3).getRecordTime().getTime());
		assertEquals(29, runnnerPricesFromDb.get(3).getLastPriceMatched(),0);
		assertEquals(11, runnnerPricesFromDb.get(3).getPriceToBack(),0);
		assertEquals(13, runnnerPricesFromDb.get(3).getPriceToLay(),0);
		
		assertEquals(1, runnnerPricesFromDb.get(8).getMarketId());
		assertEquals(1, runnnerPricesFromDb.get(8).getSelectionId());
		assertEquals(12, runnnerPricesFromDb.get(8).getRecordTime().getTime());
		assertEquals(32, runnnerPricesFromDb.get(8).getLastPriceMatched(),0);
		assertEquals(14, runnnerPricesFromDb.get(8).getPriceToBack(),0);
		assertEquals(16, runnnerPricesFromDb.get(8).getPriceToLay(),0);
		
		assertEquals(1, runnnerPricesFromDb.get(9).getMarketId());
		assertEquals(0, runnnerPricesFromDb.get(9).getSelectionId());
		assertEquals(12, runnnerPricesFromDb.get(9).getRecordTime().getTime());
		assertEquals(32, runnnerPricesFromDb.get(9).getLastPriceMatched(),0);
		assertEquals(14, runnnerPricesFromDb.get(9).getPriceToBack(),0);
		assertEquals(16, runnnerPricesFromDb.get(9).getPriceToLay(),0);
	}
	
	private List<HistRunnerPrice> createHistRunnerPrice() {

		List<HistRunnerPrice> histRunnerPrices = new ArrayList<HistRunnerPrice>();

		for (int marketId = 0; marketId < 2; marketId++) {

			for (int recordTime = 0; recordTime < 20; recordTime++) {

				for (int selectionId = 0; selectionId < 2; selectionId++) {
					HistRunnerPrice histRunnerPrice = new HistRunnerPrice();
					histRunnerPrice.setMarketId(marketId);
					histRunnerPrice.setSelectionId(selectionId);
					histRunnerPrice.setRecordTime(new Date(recordTime));

					if (recordTime > 10) {
						histRunnerPrice.setInPlayDelay(5);
					} else {
						histRunnerPrice.setInPlayDelay(0);
					}

					histRunnerPrice.setLastPriceMatched(20 + recordTime);
					histRunnerPrice.setPriceToBack(2 + recordTime);
					histRunnerPrice.setPriceToLay(4 + recordTime);

					histRunnerPrices.add(histRunnerPrice);
				}
			}
		}
		return histRunnerPrices;
	}

}
