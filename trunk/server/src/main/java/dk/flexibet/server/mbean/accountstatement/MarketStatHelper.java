package dk.flexibet.server.mbean.accountstatement;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.flexibet.server.mbean.accountstatement.model.MarketStat;
import dk.flexibet.server.model.DetailedAccountStatementItem;

/**
 *  Amount of green/white/red/total markets and profit&loss for a given period of time. 
 * @author daniel
 *
 */
public class MarketStatHelper {

	public static MarketStat getAccountProfitLoss(List<DetailedAccountStatementItem> items) {

		/** Group items by markets*/
		/** key - marketId, value - P&L*/
		Map<Integer, Double> marketsProfitLoss = new HashMap<Integer, Double>();
		for(DetailedAccountStatementItem item: items) {
			
			Double marketPL = marketsProfitLoss.get(item.getMarketId());
			if(marketPL==null) {
				marketPL = 0d;
				for(DetailedAccountStatementItem itemForEvent: items) {
					if(itemForEvent.getMarketId()==item.getMarketId()) {
						marketPL = marketPL + itemForEvent.getAmount();
					}
				}
				marketsProfitLoss.put(item.getMarketId(), marketPL);
			}			
		}
		
		/** calculate accout statement summary*/
		MarketStat accountStatement = new MarketStat();
		for(Double marketPL : marketsProfitLoss.values()) {
			if(marketPL>0) {
				accountStatement.setGreenMarkets(accountStatement.getGreenMarkets()+1);
				accountStatement.setProfitLossGreen(accountStatement.getProfitLossGreen() + marketPL);
			}
			else if(marketPL==0) {
				accountStatement.setWhiteMarkets(accountStatement.getWhiteMarkets() + 1);
			}
			else if(marketPL<0) {
				accountStatement.setRedMarkets(accountStatement.getRedMarkets()+1);
				accountStatement.setProfitLossRed(accountStatement.getProfitLossRed() + marketPL);
			}
			
			accountStatement.setProfitLossTotal(accountStatement.getProfitLossTotal() + marketPL);
		}
		
		return accountStatement;
	}
}
