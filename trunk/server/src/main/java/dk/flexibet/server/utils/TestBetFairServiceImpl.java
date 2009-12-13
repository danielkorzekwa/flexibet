package dk.flexibet.server.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.betfair.publicapi.types.exchange.v5.GetAccountFundsResp;
import com.betfair.publicapi.types.global.v3.EventType;

import dk.bot.betfairservice.BetFairException;
import dk.bot.betfairservice.BetFairService;
import dk.bot.betfairservice.BetFairServiceInfo;
import dk.bot.betfairservice.counters.TransactionCounterState;
import dk.bot.betfairservice.model.BFAccountStatementItem;
import dk.bot.betfairservice.model.BFBetCancelResult;
import dk.bot.betfairservice.model.BFBetCancelResultEnum;
import dk.bot.betfairservice.model.BFBetPlaceResult;
import dk.bot.betfairservice.model.BFBetStatus;
import dk.bot.betfairservice.model.BFBetType;
import dk.bot.betfairservice.model.BFMUBet;
import dk.bot.betfairservice.model.BFMarketData;
import dk.bot.betfairservice.model.BFMarketDetails;
import dk.bot.betfairservice.model.BFMarketDetailsRunner;
import dk.bot.betfairservice.model.BFMarketRunner;
import dk.bot.betfairservice.model.BFMarketRunners;
import dk.bot.betfairservice.model.BFMarketTradedVolume;
import dk.bot.betfairservice.model.BFSPBetPlaceResult;
import dk.bot.betfairservice.model.LoginResponse;

/**Emulator of BetFair API service.
 * 
 * @author daniel
 *
 */
public class TestBetFairServiceImpl  implements BetFairService{

	public BFBetCancelResult cancelBet(long betId) throws BetFairException {
		return new BFBetCancelResult(0,0, BFBetCancelResultEnum.TAKEN_OR_LAPSED);
	}

	public GetAccountFundsResp getAccountFunds() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		return new GetAccountFundsResp();
	}

	public List<BFAccountStatementItem> getAccountStatement(Date startDate, Date endDate, int recordCount) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		return new ArrayList<BFAccountStatementItem>();
	}

	public BetFairServiceInfo getBetFairServiceInfo() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		
		BetFairServiceInfo betFairServiceInfo = new BetFairServiceInfo();
		betFairServiceInfo.setTxCounterState(new TransactionCounterState());
		
		return betFairServiceInfo;
	}

	public List<EventType> getEventTypes() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		return new ArrayList<EventType>();
	}

	public List<BFMUBet> getMUBets(BFBetStatus betStatus) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		return new ArrayList<BFMUBet>();
	}
	
	public List<BFMUBet> getMUBets(BFBetStatus betStatus, int marketId) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		return new ArrayList<BFMUBet>();
	}

	public BFMarketDetails getMarketDetails(int marketId) throws BetFairException {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		return new BFMarketDetails(1,"", new Date(0),new Date(0),new ArrayList<BFMarketDetailsRunner>());
	}

	public BFMarketRunners getMarketRunners(int marketId) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		return new BFMarketRunners(1, new ArrayList<BFMarketRunner>(),0, new Date());
	}

	public List<BFMarketData> getMarkets(Date fromDate, Date toDate, Set<Integer> eventTypeIds) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		return new ArrayList<BFMarketData>();
	}

	public LoginResponse login() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		return new LoginResponse(false,"","","");
	}

	public BFBetPlaceResult placeBet(int marketId, int selectionId, BFBetType betType, double price, double size,boolean checkTxLimit)
			throws BetFairException {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		return new BFBetPlaceResult(1, new Date(),betType.value(),price,size,price,size);
	}

	public BFSPBetPlaceResult placeSPBet(int marketId, int selectionId, BFBetType betType, double liability,
			Double limit) throws BetFairException {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		return new BFSPBetPlaceResult(1, new Date(),betType.value(),limit,liability);
	}

	public BFMarketTradedVolume getMarketTradedVolume(int marketId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
