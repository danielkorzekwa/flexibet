package dk.flexibet.server.dao;

import java.util.Date;

import dk.bot.bettingengine.dao.model.RunnerBet;
import dk.bot.marketobserver.model.MarketRunner;

/**
 * Dao object for last back/lay bets for runner state machine.
 * 
 * @author daniel
 * 
 */
public interface RunnerStateLastBetDAO {
	
	public RunnerBet findBet(long betId);
	
	public RunnerBet findLastBet(int runnerStateId, String betType);
	
	/**
	 * Add new bet if not exist, replace if already exist. Only one lay and one
	 * back bets can exist in db for runner state.
	 * 
	 * @param runnerStateId
	 * @param betId
	 * @param placedDate
	 * @param betType
	 * @param price
	 * @param size
	 * @param avgPriceMatched
	 * @param sizeMatched
	 * @param marketRunner
	 *            State of marketRunner when the bet was placed.
	 */
	public void addBet(int runnerStateId, long betId, Date placedDate, String betType, double price, double size,
			double avgPriceMatched, double sizeMatched, MarketRunner marketRunner, boolean marketInPLay,
			double price_slope, double price_slope_err);

	public void cancelBet(long betId, double sizeCancelled, double sizeMatched);
	
	public void updateBet(long betId, double avgPriceMatched,double sizeMatched, Date matchedDate);
}
