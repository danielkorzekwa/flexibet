package dk.flexibet.server.simulator;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.math.util.MathUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import dk.bot.marketobserver.model.MarketData;
import dk.bot.marketobserver.model.MarketDetailsRunner;
import dk.flexibet.server.dao.HistRunnerPriceDAO;
import dk.flexibet.server.dao.MarketDAO;
import dk.flexibet.server.model.HistRunnerPrice;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-flexibet-simulator.xml", "classpath:spring/ds-test.xml" })
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "txManager")
@Transactional
public class BettingSimulatorIml2IntegrationTest {

	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private Date marketDate;
	
	@Resource
	private MarketDAO marketDao;
	@Resource
	private HistRunnerPriceDAO histRunnerPriceDao;
	@Resource
	private BettingSimulator simulator;

	private static String bettingConfigFile = "src/test/resources/simulator_test2";

	@BeforeClass
	public static void setUpBeforeClass() {
		System.setProperty("BOT_HOME", bettingConfigFile);
	}

	@Before
	public void setUp() throws ParseException {
		 marketDate = df.parse("2009-09-25");
		
		/** Create market */
		MarketData market = createMarket(1);
		marketDao.saveMarket(market);

		/** Create marker historical prices */
		List<HistRunnerPrice> runnerPrices = createHistRunnerPrice(market);
		histRunnerPriceDao.saveRunnerPrices(runnerPrices);
	}

	@Test
	public void testSimulate() {

		BettingSimulationReport report = simulator.simulate("select * from market where market_time>='2009-09-25' and market_time<='2009-09-25'",null);

		assertNotNull("Betting report is null", report);
		System.out.println("Number of markets:" + report.getNumOfMarkets());
		System.out.println("Number of analyzed markets:" + report.getNumOfAnalyzedMarkets());
		System.out.println("Number of matched bets:" + report.getNumOfMatchedBets());
		System.out.println("Total liability: " + MathUtils.round(report.getTotalLiability(), 2));
		System.out.println(report.getReport());
		
		assertEquals(1,report.getNumOfMarkets());
		assertEquals(1,report.getNumOfAnalyzedMarkets());
		assertEquals(7,report.getNumOfMatchedBets());
		assertEquals(1.6546,report.getTotalLiability(),0.0001);
	}

	private MarketData createMarket(int marketId) {
		MarketData marketData = new MarketData();

		marketData.setMarketId(marketId);
		marketData.setMarketName("Match Odds");
		marketData.setMarketType("O");
		marketData.setMarketStatus("ACTIVE");
		marketData.setEventDate(marketDate);
		marketData.setSuspendTime(marketDate);
		marketData.setMenuPath("Soccer/UK/Premiership");
		marketData.setEventHierarchy("/1/54543/67677/666");
		marketData.setCountryCode("uk");
		marketData.setNumberOfRunners(13);
		marketData.setNumberOfWinners(2);
		marketData.setBsbMarket(true);
		marketData.setTurningInPlay(true);

		List<MarketDetailsRunner> runners = new ArrayList<MarketDetailsRunner>();
		runners.add(new MarketDetailsRunner(2, "The Draw"));
		runners.add(new MarketDetailsRunner(3, "Man Utd"));

		marketData.setRunners(runners);

		return marketData;
	}
	
	private List<HistRunnerPrice> createHistRunnerPrice(MarketData market) {

		List<HistRunnerPrice> histRunnerPrices = new ArrayList<HistRunnerPrice>();

			for (int recordTime = 0; recordTime < 20; recordTime++) {

				for (MarketDetailsRunner runnerDetails: market.getRunners()) {
					HistRunnerPrice histRunnerPrice = new HistRunnerPrice();
					histRunnerPrice.setMarketId(market.getMarketId());
					histRunnerPrice.setSelectionId(runnerDetails.getSelectionId());
					histRunnerPrice.setRecordTime(new Date(marketDate.getTime() + (recordTime*1000*60)));
					histRunnerPrice.setInPlayDelay(5);
					histRunnerPrice.setLastPriceMatched(2 + (recordTime*0.1));
					histRunnerPrice.setPriceToBack(3.2 - (recordTime*0.1) - 0.02);
					histRunnerPrice.setPriceToLay(3.2 - (recordTime*0.1) + 0.02);
					histRunnerPrices.add(histRunnerPrice);
				}
			}
		
		return histRunnerPrices;
	}


}
