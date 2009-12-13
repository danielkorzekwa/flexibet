package dk.flexibet.server.model.factory;

import java.util.ArrayList;
import java.util.List;

import dk.bot.betfairservice.model.BFAccountStatementItem;
import dk.bot.bettingengine.dao.model.RunnerState;
import dk.flexibet.server.dao.RunnerStateDAO;
import dk.flexibet.server.model.DetailedAccountStatementItem;

/**Creates AccountStatementItems from BFAccountStatementItems
 * 
 * @author daniel
 *
 */
public class AccountStatementItemFactory {

	public static List<DetailedAccountStatementItem> create(List<BFAccountStatementItem> bfItems, RunnerStateDAO runnerStateDao) {
		
		List<DetailedAccountStatementItem> items = new ArrayList<DetailedAccountStatementItem>();
		
		for(BFAccountStatementItem bfItem: bfItems) {
			DetailedAccountStatementItem item = new DetailedAccountStatementItem();
			item.setMarketId(bfItem.getMarketId());
			item.setMarketName(bfItem.getMarketName());
			item.setMarketType(bfItem.getMarketType());
			item.setFullMarketName(bfItem.getFullMarketName());
			item.setSelectionId(bfItem.getSelectionId());
			item.setSelectionName(bfItem.getSelectionName());
			item.setBetId(bfItem.getBetId());
			item.setEventTypeId(bfItem.getEventTypeId());
			item.setBetCategoryType(bfItem.getBetCategoryType());
			item.setBetType(bfItem.getBetType());
			item.setBetSize(bfItem.getBetSize());
			item.setAvgPrice(bfItem.getAvgPrice());
			item.setPlacedDate(bfItem.getPlacedDate());
			item.setStartDate(bfItem.getStartDate());
			item.setSettledDate(bfItem.getSettledDate());
			item.setCommission(bfItem.isCommission());
			item.setAmount(bfItem.getAmount());
			
			RunnerState runnerState = runnerStateDao.findRunnerState(item.getBetId());
			if(runnerState!=null) {
				item.setStateMachineId(runnerState.getStateMachineId());
				item.setStateName(runnerState.getStateName());
			}
			
			items.add(item);
		}
		
		return items;
	}
}
