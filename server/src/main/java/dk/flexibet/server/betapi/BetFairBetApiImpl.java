package dk.flexibet.server.betapi;

import dk.bot.betfairservice.BetFairException;
import dk.bot.betfairservice.BetFairService;
import dk.bot.betfairservice.model.BFBetCancelResult;
import dk.bot.betfairservice.model.BFBetPlaceResult;
import dk.bot.betfairservice.model.BFBetType;
import dk.bot.betfairservice.model.BFSPBetPlaceResult;
import dk.bot.bettingengine.betapi.BetApi;
import dk.bot.bettingengine.betapi.BetCancelResult;
import dk.bot.bettingengine.betapi.BetCancelResultEnum;
import dk.bot.bettingengine.betapi.BetPlaceResult;
import dk.bot.bettingengine.betapi.SPBetPlaceResult;
import dk.bot.marketobserver.model.BetType;

public class BetFairBetApiImpl implements BetApi {

	private final BetFairService betFairService;

	public BetFairBetApiImpl(BetFairService betFairService) {
		this.betFairService = betFairService;
	}

	public BetCancelResult cancelBet(long betId) throws BetFairException {
		BFBetCancelResult r = betFairService.cancelBet(betId);

		BetCancelResult result = new BetCancelResult(betId,r.getSizeCancelled(), r.getSizeMatched(), BetCancelResultEnum
				.valueOf(r.getStatus().name()));
		return result;
	}

	public BetPlaceResult placeBet(int marketId, int selectionId, BetType betType, double price, double size,
			boolean checkTxLimit) {
		BFBetPlaceResult r = betFairService.placeBet(marketId, selectionId, BFBetType.fromValue(betType.name()), price, size, checkTxLimit);

		BetPlaceResult result = new BetPlaceResult(r.getBetId(), r.getBetDate(), r.getBetType(), r.getPrice(), r
				.getSize(), r.getAvgPriceMatched(), r.getSizeMatched());
		return result;
	}

	public SPBetPlaceResult placeSPBet(int marketId, int selectionId, BetType betType, double liability, Double limit) {
		BFSPBetPlaceResult r = betFairService.placeSPBet(marketId, selectionId, BFBetType.fromValue(betType.name()), liability, limit);
		SPBetPlaceResult result = new SPBetPlaceResult(r.getBetId(), r.getBetDate(), r.getBetType(), r.getLimit(), r
				.getLiability());
		return result;
	}
}
