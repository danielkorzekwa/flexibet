package dk.bot.webconsole.modules.dashboard.sportstat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.bot.bwinservice.model.BWinMarket;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.tasks.bwin.BetFairBwinRegionEnum;

/**
 * How many markets are for each sport: betfair and bwin markets
 * 
 * @author daniel
 * 
 */
public class SportsStatHelper {

	public static List<SportStat> getSportsStat(List<Market> compositeMarkets,Collection<BWinMarket> bwinMarkets) {

		/** key sportName*/
		Map<String,SportStat> sportsStatMap = new HashMap<String,SportStat>();
		
		/** Create BetFair markets stats.*/
		for (Market compositeMarket : compositeMarkets) {
			String sportName = BetFairBwinRegionEnum.getSportName(compositeMarket.getRegion(),compositeMarket.getMarketData().getMenuPath());

			SportStat sportStat = sportsStatMap.get(sportName);
			if(sportStat==null) {
				sportStat = new SportStat();
				sportStat.setSportName(sportName);
			}
			sportStat.setBetFairMarkets(sportStat.getBetFairMarkets()+1);
			
			if(compositeMarket.getBwinPrices()!=null) {
				sportStat.setBwinMMatchedMarkets(sportStat.getBwinMMatchedMarkets()+1);
			}
			if(compositeMarket.getHorseWinMarket()!=null) {
				sportStat.setOddsCheckerMatchedMarkets(sportStat.getOddsCheckerMatchedMarkets()+1);
			}
					
			sportsStatMap.put(sportName, sportStat);
		}
		
		/**Create bwin markets stats.*/
			
		if(bwinMarkets!=null) {
			for(BWinMarket bwinMarket: bwinMarkets) {
				
				BetFairBwinRegionEnum betFairRegion = BetFairBwinRegionEnum.getRegion(bwinMarket.getRegion());
				String sportName = BetFairBwinRegionEnum.getSportName(betFairRegion,null);
				
				SportStat sportStat = sportsStatMap.get(sportName);
				if(sportStat==null) {
					sportStat = new SportStat();
					sportStat.setSportName(sportName);
				}
				sportStat.setBwinMarkets(sportStat.getBwinMarkets()+1);	
				sportsStatMap.put(sportName, sportStat);
			}
		}

		/**Create OddsChecker stats*/
		String sportName = BetFairBwinRegionEnum.getSportName(BetFairBwinRegionEnum.HORSERACING_GB,null);
		SportStat sportStat = sportsStatMap.get(sportName);
		if(sportStat==null) {
			sportStat = new SportStat();
			sportStat.setSportName(sportName);
		}
		sportStat.setOddsCheckerMarkets(0);
		sportsStatMap.put(sportName, sportStat);
		
		ArrayList<SportStat> statsList = new ArrayList<SportStat>();
		statsList.addAll(sportsStatMap.values());	
		Collections.sort(statsList);
			
		return statsList;
	}
}
