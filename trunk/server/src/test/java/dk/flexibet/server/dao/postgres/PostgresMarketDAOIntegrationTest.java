package dk.flexibet.server.dao.postgres;

import static org.junit.Assert.*;

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

import dk.bot.marketobserver.model.MarketData;
import dk.bot.marketobserver.model.MarketDetailsRunner;
import dk.flexibet.server.dao.MarketDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-flexibet-server-dao.xml","classpath:spring/ds-test.xml" })
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "txManager")
@Transactional
public class PostgresMarketDAOIntegrationTest {

	@Resource
	private MarketDAO marketDao;
	
	@Test
	public void testFindMarketNotExist() {
		assertNull(marketDao.findMarket(1));
	}
	
	@Test
	public void testFindSaveMarket() {
		
		MarketData market = createMarket(1);
		marketDao.saveMarket(market);
		
		MarketData marketFromDb = marketDao.findMarket(market.getMarketId());
		
		assertEquals(market.getMarketId(), marketFromDb.getMarketId());
		assertEquals(market.getMarketName(), marketFromDb.getMarketName());
		assertEquals(market.getMarketType(), marketFromDb.getMarketType());
		assertEquals(market.getMarketStatus(), marketFromDb.getMarketStatus());
		assertEquals(market.getEventDate(), marketFromDb.getEventDate());
		assertEquals(market.getSuspendTime(), marketFromDb.getSuspendTime());
		assertEquals(market.getMenuPath(), marketFromDb.getMenuPath());
		assertEquals(market.getEventHierarchy(), marketFromDb.getEventHierarchy());
		assertEquals(market.getCountryCode(), marketFromDb.getCountryCode());
		assertEquals(market.getNumberOfRunners(), marketFromDb.getNumberOfRunners());
		assertEquals(market.getNumberOfWinners(), marketFromDb.getNumberOfWinners());
		assertEquals(market.isBsbMarket(), marketFromDb.isBsbMarket());
		assertEquals(market.isTurningInPlay(), marketFromDb.isTurningInPlay());
		
		assertEquals(market.getRunners().size(), marketFromDb.getRunners().size());
		assertEquals(market.getRunners().get(0).getSelectionId(), marketFromDb.getRunners().get(0).getSelectionId());
		assertEquals(market.getRunners().get(0).getSelectionName(), marketFromDb.getRunners().get(0).getSelectionName());
		assertEquals(market.getRunners().get(1).getSelectionId(), marketFromDb.getRunners().get(1).getSelectionId());
		assertEquals(market.getRunners().get(1).getSelectionName(), marketFromDb.getRunners().get(1).getSelectionName());
	
	}

	@Test
	public void testMarketExistsTrue() {
		MarketData market = createMarket(1);
		marketDao.saveMarket(market);
		
		assertTrue(marketDao.marketExists(market.getMarketId()));
	}
	
	@Test
	public void testMarketExistsFalse() {
		assertFalse(marketDao.marketExists(1));
	}
	
	@Test 
	public void testFindMarkets() {
		List<MarketData> markets = marketDao.findMarkets("select * from market");
		assertEquals(0, markets.size());
		for(int i=0;i<10;i++) {
			MarketData market = createMarket(i);
			marketDao.saveMarket(market);
		}
		markets = marketDao.findMarkets("select * from market");
		assertEquals(10, markets.size());
	}

	private MarketData createMarket(int marketId) {
		MarketData marketData = new MarketData();
		
		marketData.setMarketId(marketId);
		marketData.setMarketName("market 1");
		marketData.setMarketType("O");
		marketData.setMarketStatus("ACTIVE");
		marketData.setEventDate(new Date(System.currentTimeMillis()));
		marketData.setSuspendTime(new Date(System.currentTimeMillis()));
		marketData.setMenuPath("Soccer/UK/Premiership");
		marketData.setEventHierarchy("/24/34565/67677/666");
		marketData.setCountryCode("uk");
		marketData.setNumberOfRunners(13);
		marketData.setNumberOfWinners(2);
		marketData.setBsbMarket(true);
		marketData.setTurningInPlay(true);
		
		List<MarketDetailsRunner> runners = new ArrayList<MarketDetailsRunner>();
		runners.add(new MarketDetailsRunner(2,"selection 2"));
		runners.add(new MarketDetailsRunner(3,"selection 3"));
		
		marketData.setRunners(runners);
		
		return marketData;
	}

}
