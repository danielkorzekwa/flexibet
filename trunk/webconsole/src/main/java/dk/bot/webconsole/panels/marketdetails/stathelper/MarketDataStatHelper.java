package dk.bot.webconsole.panels.marketdetails.stathelper;

import java.text.SimpleDateFormat;

import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketData;
import dk.bot.webconsole.utils.MarketDeepLink;

/**
 * Creates marketDataStat object
 * 
 * @author daniel
 * 
 */
public class MarketDataStatHelper {

	public static MarketDataStat createMarketDataStat(Market completeMarket) {
		MarketDataStat marketDataStat = new MarketDataStat();

		if (completeMarket != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat();
			
			MarketData marketData = completeMarket.getMarketData();

			marketDataStat.setMarketId("" + marketData.getMarketId());
			marketDataStat.setBetFairDeepLink(MarketDeepLink.generateMarketDeepLink(marketData.getMarketId()));
			marketDataStat.setMarketName(marketData.getMarketName());
			marketDataStat.setMenuPath(marketData.getMenuPath());
			marketDataStat.setInPlayDelay("" + completeMarket.getMarketRunners().getInPlayDelay());
			marketDataStat.setNumberOfRunners("" + marketData.getNumberOfRunners());
			marketDataStat.setNumberOfWinners("" + marketData.getNumberOfWinners());
			marketDataStat.setMarketTime(dateFormat.format(completeMarket.getMarketData().getEventDate()));
			marketDataStat.setMarketRunnersTimestamp(dateFormat.format(completeMarket.getMarketRunners().getTimestamp()));
		}

		return marketDataStat;
	}
}
