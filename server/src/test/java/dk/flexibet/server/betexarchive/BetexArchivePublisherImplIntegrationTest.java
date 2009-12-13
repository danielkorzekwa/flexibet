package dk.flexibet.server.betexarchive;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.*;

import org.apache.commons.jexl.junit.Asserter;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dk.bot.marketobserver.model.BetType;
import dk.flexibet.server.dao.HistRunnerPriceDAO;
import dk.flexibet.server.model.HistRunnerPrice;

@RunWith(JMock.class)
public class BetexArchivePublisherImplIntegrationTest {
	
	private Mockery mockery = new JUnit4Mockery();

	private HistRunnerPriceDAO histRunnerPriceDao = mockery.mock(HistRunnerPriceDAO.class);
	private BetexArchivePublisherImpl betexArchivePublisher = new BetexArchivePublisherImpl(histRunnerPriceDao);
	
	private List<HistRunnerPrice> histRunnerPrices = createHistRunnerPrices();	
	
	@Before
	public void setUp() {
		mockery.checking(new Expectations() {
			{
				one(histRunnerPriceDao).saveRunnerPrices(histRunnerPrices);
			}
		});
	}
	
	@Test
	public void testPublish() throws ParseException, InterruptedException, IOException {
		
		
		betexArchivePublisher.publish(histRunnerPrices);

		/**Give a time for publisher to process hist runner prices.*/
		while (betexArchivePublisher.getPriceQueueSize() > 0) {
			Thread.sleep(100);
		}
		Thread.sleep(200);
		assertEquals(0, betexArchivePublisher.getPriceQueueSize());
	}

	private List<HistRunnerPrice> createHistRunnerPrices() {

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
