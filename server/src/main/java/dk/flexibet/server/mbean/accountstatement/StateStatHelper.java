package dk.flexibet.server.mbean.accountstatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.flexibet.server.mbean.accountstatement.model.MarketRunnerStatement;
import dk.flexibet.server.mbean.accountstatement.model.StateMachineStat;
import dk.flexibet.server.mbean.accountstatement.model.StatesStat;
import dk.flexibet.server.model.DetailedAccountStatementItem;

/**
 * Statistic about runner states v profit&loss
 * 
 * @author daniel
 * 
 */
public class StateStatHelper {

	private final static Log log = LogFactory.getLog(StateStatHelper.class.getSimpleName());

	/**
	 * Statistics for state machines. Profit per state.
	 * 
	 * @param marketRunnerItems
	 * @return key - stateName
	 */
	public static List<StateMachineStat> getStatesStat(List<MarketRunnerStatement> marketRunnerItems) {

		/**Prepare map with statements per stateMachine. Key - stateMachineId, Value - list of statements for state machine.*/
		Map<String,List<MarketRunnerStatement>> runnerStatementMap = new HashMap<String, List<MarketRunnerStatement>>();
		
		
		for (MarketRunnerStatement marketRunnerStatement : marketRunnerItems) {
			List<MarketRunnerStatement> statementList = runnerStatementMap.get(marketRunnerStatement.getStateMachineId());
			if(statementList==null) {
				statementList = new ArrayList<MarketRunnerStatement>();
			}
			statementList.add(marketRunnerStatement);
			runnerStatementMap.put(marketRunnerStatement.getStateMachineId(), statementList);
		}
		
		/**Calculate states stats for state machines*/
		ArrayList<StateMachineStat> stateMachinesStats = new ArrayList<StateMachineStat>();
		for(String stateMachineId : runnerStatementMap.keySet()) {
			
			/** key - stateName */
			Map<String, StatesStat> stateStats = new HashMap<String, StatesStat>();

			List<MarketRunnerStatement> stateMachineItems = runnerStatementMap.get(stateMachineId);
			for (MarketRunnerStatement item : stateMachineItems) {
				
					StatesStat stateStat = stateStats.get(item.getStateName());
					if (stateStat == null) {
						stateStat = new StatesStat(stateMachineId,item.getStateName());
					}
					if (item.getAmount() > 0) {
						stateStat.setProfitLossGreen(stateStat.getProfitLossGreen() + item.getAmount());
						stateStat.setGreenRunners(stateStat.getGreenRunners() + 1);
					} else if (item.getAmount() == 0) {
						stateStat.setWhiteRunners(stateStat.getWhiteRunners() + 1);
					} else if (item.getAmount() < 0) {
						stateStat.setProfitLossRed(stateStat.getProfitLossRed() + item.getAmount());
						stateStat.setRedRunners(stateStat.getRedRunners() + 1);
					}

					stateStats.put(item.getStateName(), stateStat);	
			}

			StateMachineStat stateMachineStat = new StateMachineStat(stateMachineId, stateStats);
			stateMachinesStats.add(stateMachineStat);
		}
		
		return stateMachinesStats;

	}

	/** Calculate account statement for market runners */
	public static List<MarketRunnerStatement> getMarketRunnerStatements(List<DetailedAccountStatementItem> items) {

		/** key - marketId:selectionId */
		Map<String, MarketRunnerStatement> statements = new HashMap<String, MarketRunnerStatement>();

		for (DetailedAccountStatementItem item : items) {

			String marketRunnerKey = item.getStateMachineId() + ":" + item.getStateName() + ":" + item.getMarketId() + ":" + item.getSelectionId();

			MarketRunnerStatement statement = statements.get(marketRunnerKey);
			if (statement == null) {
				statement = new MarketRunnerStatement(item.getStateMachineId(), item.getStateName(), item.getMarketId(), item.getSelectionId());
			}
			statement.setAmount(statement.getAmount() + item.getAmount());
			statements.put(marketRunnerKey, statement);
		}

		ArrayList<MarketRunnerStatement> statementsList = new ArrayList<MarketRunnerStatement>();
		statementsList.addAll(statements.values());

		return statementsList;
	}
}
