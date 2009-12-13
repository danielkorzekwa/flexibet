package dk.bot.webconsole.modules.statement.home.navigation;

import java.io.Serializable;

import dk.bot.webconsole.modules.statement.home.stathelper.AccountStatementOrderEnum;
import dk.bot.webconsole.modules.statement.home.stathelper.TimePeriod;

/**
 * 
 * @author daniel
 *
 */
 public interface NavigationPanelListener extends Serializable{

	 /**
	  * 
	  * @param period
	  * @param stateMachineId If null then items for all state machines are returned.
	  * @param stateName If null then items for all states (for state machine Id) are returned.
	  * @param orderBy
	  * @betItems If true then items related to bets are returned (betId>0). If false then other items are returned (betId=0).	
	  */
	void onUpdate(TimePeriod period,String stateMachineId,String stateName,AccountStatementOrderEnum orderBy, boolean betItems);
}
