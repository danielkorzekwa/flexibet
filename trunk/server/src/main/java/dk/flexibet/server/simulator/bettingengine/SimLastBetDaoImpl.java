package dk.flexibet.server.simulator.bettingengine;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.marketobserver.model.BetType;

public class SimLastBetDaoImpl implements SimLastBetDao {

	/** key - runnerStateId */
	private Map<Integer, RunnerBet> lastBackBets = new HashMap<Integer, RunnerBet>();

	/** key - runnerStateId */
	private Map<Integer, RunnerBet> lastLayBets = new HashMap<Integer, RunnerBet>();

	/** key - betId */
	private Map<Long, RunnerBet> allBets = new HashMap<Long, RunnerBet>();

	public RunnerBet findLastBet(int runnerStateId, BetType betType) {
		if (betType == BetType.B) {
			return lastBackBets.get(runnerStateId);
		} else if (betType == BetType.L) {
			return lastLayBets.get(runnerStateId);
		} else {
			throw new IllegalArgumentException("Bet type is not supported: " + betType);
		}
	}

	public void saveLastBet(RunnerBet runnerBet) {
		allBets.put(runnerBet.getBetId(), runnerBet);

		if (runnerBet.getBetType().equals(BetType.B.value())) {
			lastBackBets.put(runnerBet.getRunnerStateId(), runnerBet);
		} else if (runnerBet.getBetType().equals(BetType.L.value())) {
			lastLayBets.put(runnerBet.getRunnerStateId(), runnerBet);
		} else {
			throw new IllegalArgumentException("Bet type is not supported: " + runnerBet.getBetType());
		}
	}

	public void updateLastBet(long betId, double avgPriceMatched, double sizeMatched, Date matchedDate) {
		RunnerBet runnerBet = allBets.get(betId);
		runnerBet.setAvgPriceMatched(avgPriceMatched);
		runnerBet.setSizeMatched(sizeMatched);
		runnerBet.setMatchedDate(matchedDate);

		RunnerBet lastBackBet = lastBackBets.get(betId);
		if (lastBackBet != null) {
			lastBackBet.setAvgPriceMatched(avgPriceMatched);
			lastBackBet.setSizeMatched(sizeMatched);
			lastBackBet.setMatchedDate(matchedDate);
		}

		RunnerBet lastLayBet = lastLayBets.get(betId);
		if (lastLayBet != null) {
			lastLayBet.setAvgPriceMatched(avgPriceMatched);
			lastLayBet.setSizeMatched(sizeMatched);
			lastLayBet.setMatchedDate(matchedDate);
		}

	}
	
	public void cancelBet(long betId, double sizeCancelled, double sizeMatched) {
		RunnerBet runnerBet = allBets.get(betId);
		runnerBet.setSizeCancelled(sizeCancelled);
		runnerBet.setSizeMatched(sizeMatched);

		RunnerBet lastBackBet = lastBackBets.get(betId);
		if (lastBackBet != null) {
			lastBackBet.setSizeCancelled(sizeCancelled);
			lastBackBet.setSizeMatched(sizeMatched);
		}

		RunnerBet lastLayBet = lastLayBets.get(betId);
		if (lastLayBet != null) {
			lastLayBet.setSizeCancelled(sizeCancelled);
			lastLayBet.setSizeMatched(sizeMatched);
		}

	}

	public RunnerBet findBet(long betId) {
		return allBets.get(betId);
	}

}
