package dk.flexibet.server.simulator.betex;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import com.betfair.publicapi.types.exchange.v5.BetTypeEnum;

import dk.bot.betfairservice.BetFairUtil;
import dk.bot.betfairservice.PriceRange;
import dk.bot.bettingengine.betapi.BetCancelResult;
import dk.bot.bettingengine.betapi.BetCancelResultEnum;
import dk.bot.bettingengine.betapi.BetPlaceResult;
import dk.bot.marketobserver.model.BetCategoryType;
import dk.bot.marketobserver.model.BetStatus;
import dk.bot.marketobserver.model.BetType;
import dk.bot.marketobserver.model.MUBet;
import dk.bot.marketobserver.model.MarketRunners;

/**
 * Represents Betting Exchange. It's a very simple implementation used by a betting simulator. 
 * 
 * Bets are matched when are in front of queue.
 * automatically matched.
 * 
 * @author daniel
 * 
 */
public class BettingExchangeInFrontOfQueueImpl implements BettingExchange {

	private List<PriceRange> priceRanges = BetFairUtil.getPriceRanges();
	
	private int lastBetId = 0;

	private List<MUBet> muBets = new ArrayList<MUBet>();

	public BetPlaceResult placeBet(int marketId, int selectionId, BetType betType, double price, double size,
			boolean checkTxLimit) {
		int betId = getNextBetId();

		double validatedPrice;
		if (betType == BetType.B) {
			validatedPrice = BetFairUtil.validatePrice(priceRanges, price, BetFairUtil.ROUND_DOWN);
		} else if (betType == BetType.L) {
			validatedPrice = BetFairUtil.validatePrice(priceRanges, price, BetFairUtil.ROUND_UP);
		} else {
			throw new IllegalArgumentException("Bet type not recognized: " + betType.value() + ".");
		}
		
		MUBet bet = new MUBet();
		bet.setBetId(betId);
		bet.setBetStatus(BetStatus.U);
		bet.setBetType(betType);
		bet.setBetCategoryType(BetCategoryType.E);
		bet.setMarketId(marketId);
		bet.setSelectionId(selectionId);
		bet.setPlacedDate(new DateTime().toDate());
		bet.setPrice(validatedPrice);
		bet.setSize(size);

		muBets.add(bet);

		BetPlaceResult betPlaceResult = new BetPlaceResult(betId, bet.getPlacedDate(), betType.name(), price, size,
				0, 0);
		return betPlaceResult;
	}
	
	public BetCancelResult cancelBet(long betId) {
		for (int i=0;i<muBets.size();i++) {
			MUBet bet = muBets.get(i);
			if(bet.getBetId()==betId && bet.getBetStatus()==BetStatus.U) {
				muBets.remove(i);
				BetCancelResult betCancelResult = new BetCancelResult(betId,bet.getSize(),0,BetCancelResultEnum.OK);
				return betCancelResult;
			}
		}
		throw new IllegalArgumentException("Bet doesn't exist for betId: " + betId);
	}

	public List<MUBet> getMUBets(int marketId) {

		List<MUBet> marketMUBets = new ArrayList<MUBet>();
		for (MUBet bet : muBets) {
			if (bet.getMarketId() == marketId) {
				marketMUBets.add(bet);
			}
		}

		return marketMUBets;
	}
	public List<MUBet> getMUBets() {
		return muBets;
	}
	
	public int getNumOfMatchedBets() {
		int numOfMatchedBets=0;
		for (MUBet bet : muBets) {
			if (bet.getBetStatus()==BetStatus.M) {
				numOfMatchedBets++;
			}
		}
		return numOfMatchedBets;
	}
	
	public void matchBets(MarketRunners marketRunners) {
		
		for (MUBet bet : muBets) {
			if (bet.getMarketId() == marketRunners.getMarketId() && bet.getBetStatus()==BetStatus.U) {
				if(bet.getBetType()==BetType.B) {
					if(bet.getPrice()<marketRunners.getMarketRunner(bet.getSelectionId()).getPriceToLay()) {
						bet.setBetStatus(BetStatus.M);
						bet.setMatchedDate(new DateTime().toDate());
					}
				}
				else if(bet.getBetType()==BetType.L && bet.getBetStatus()==BetStatus.U) {
					if(bet.getPrice()>marketRunners.getMarketRunner(bet.getSelectionId()).getPriceToBack()) {
						bet.setBetStatus(BetStatus.M);
						bet.setMatchedDate(new DateTime().toDate());
					}
				}
				else {
					throw new UnsupportedOperationException("Unsupported bet type: " + bet.getBetType());
				}
			}
		}
	}

	private int getNextBetId() {
		lastBetId++;
		return lastBetId;
	}

}
