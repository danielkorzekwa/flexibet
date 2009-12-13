package dk.bot.webconsole.modules.dashboard.sportstat;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import dk.bot.bwinservice.model.BWinMarket;
import dk.bot.bwinservice.model.BWinTeam;
import dk.bot.bwinservice.model.BwinRegionEnum;
import dk.bot.bwinservice.model.BwinSportEnum;
import dk.bot.marketobserver.model.Market;
import dk.bot.marketobserver.model.MarketData;
import dk.bot.marketobserver.tasks.bwin.BetFairBwinRegionEnum;
import dk.bot.marketobserver.tasks.bwin.BwinMarketPrices;
import dk.bot.oddschecker.model.HorseWinMarket;

public class SportsStatHelperTest {

	@Test
	public void testGetSportsStat() {

		List<Market> compositeMarkets = new ArrayList<Market>();
		compositeMarkets.add(getCompositeMarkets(BetFairBwinRegionEnum.SOCCER_BRAZIL, true));
		compositeMarkets.add(getCompositeMarkets(BetFairBwinRegionEnum.SOCCER_POLAND, true));
		compositeMarkets.add(getCompositeMarkets(BetFairBwinRegionEnum.SOCCER_CZECH, false));

		compositeMarkets.add(getCompositeMarkets(BetFairBwinRegionEnum.BASKETBALL_ITALY, false));
		compositeMarkets.add(getCompositeMarkets(BetFairBwinRegionEnum.BASKETBALL_SPAIN, true));
		
		compositeMarkets.add(getCompositeMarkets(null, false));
		compositeMarkets.add(getCompositeMarkets(null, false));
		
		Collection<BWinMarket> bwinMarkets = new ArrayList<BWinMarket>();
		bwinMarkets.addAll(getBwinMarkets(BwinSportEnum.SOCCER, BwinRegionEnum.SOCCER_BELGIUM, 9));
		bwinMarkets.addAll(getBwinMarkets(BwinSportEnum.BASKETBALL,
				BwinRegionEnum.BASKETBALL_POLAND, 4));
						
		List<SportStat> sportsStat = SportsStatHelper.getSportsStat(compositeMarkets, bwinMarkets);

		assertEquals("BASKETBALL", sportsStat.get(0).getSportName());
		assertEquals(2, sportsStat.get(0).getBetFairMarkets());
		assertEquals(4, sportsStat.get(0).getBwinMarkets());
		assertEquals(1, sportsStat.get(0).getBwinMMatchedMarkets());

		assertEquals("HORSERACING", sportsStat.get(1).getSportName());
		assertEquals(0, sportsStat.get(1).getBetFairMarkets());
		assertEquals(0, sportsStat.get(1).getBwinMarkets());
		assertEquals(0, sportsStat.get(1).getBwinMMatchedMarkets());
	
		assertEquals("SOCCER", sportsStat.get(2).getSportName());
		assertEquals(3, sportsStat.get(2).getBetFairMarkets());
		assertEquals(9, sportsStat.get(2).getBwinMarkets());
		assertEquals(2, sportsStat.get(2).getBwinMMatchedMarkets());

		assertEquals("UNKNOWN", sportsStat.get(3).getSportName());
		assertEquals(2, sportsStat.get(3).getBetFairMarkets());
		assertEquals(0, sportsStat.get(3).getBwinMarkets());
		assertEquals(0, sportsStat.get(3).getBwinMMatchedMarkets());

	}

	private Market getCompositeMarkets(BetFairBwinRegionEnum region, boolean bWinMatched) {

		BwinMarketPrices bwinMarketPrices = null;
		if (bWinMatched) {
			bwinMarketPrices = new BwinMarketPrices(1,new HashMap<Integer, Double>());
		}

		Market compositeMarket = new Market(new MarketData(),region, bwinMarketPrices, new HorseWinMarket());

		return compositeMarket;
	}

	private Collection<BWinMarket> getBwinMarkets(BwinSportEnum bwinSportEnum, BwinRegionEnum bwinRegion, int markets) {

		List<BWinMarket> bwinMarkets = new ArrayList<BWinMarket>();
		for (int i = 0; i < markets; i++) {
			BWinMarket bwinMarket = new BWinMarket(bwinSportEnum,bwinRegion,new ArrayList<BWinTeam>(),new Date(0));
			bwinMarkets.add(bwinMarket);
		}
		return bwinMarkets;

	}

}
