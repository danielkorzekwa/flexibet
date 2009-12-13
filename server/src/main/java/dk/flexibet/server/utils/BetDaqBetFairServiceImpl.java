package dk.flexibet.server.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.betfair.publicapi.types.exchange.v5.GetAccountFundsResp;
import com.betfair.publicapi.types.global.v3.EventType;

import dk.bot.betdaq.BetDaq;
import dk.bot.betdaq.BetDaqFactoryBean;
import dk.bot.betdaq.model.BDAccountBalance;
import dk.bot.betdaq.model.BDAccountPostingItem;
import dk.bot.betdaq.model.BDBootstrapOrders;
import dk.bot.betdaq.model.BDEvent;
import dk.bot.betdaq.model.BDMarket;
import dk.bot.betdaq.model.BDMarketHelper;
import dk.bot.betdaq.model.BDMarketWithPrices;
import dk.bot.betdaq.model.BDOrder;
import dk.bot.betdaq.model.BDMarket.BDSelection;
import dk.bot.betdaq.model.BDMarketWithPrices.BDSelectionWithPrices;
import dk.bot.betdaq.model.BDMarketWithPrices.BDSelectionWithPrices.BDPrice;
import dk.bot.betdaq.model.BDMarketWithPrices.BDSelectionWithPrices.BDPrice.BDPRiceTypeEnum;
import dk.bot.betfairservice.BetFairException;
import dk.bot.betfairservice.BetFairService;
import dk.bot.betfairservice.BetFairServiceInfo;
import dk.bot.betfairservice.counters.DataRequestCounter;
import dk.bot.betfairservice.counters.TransactionCounter;
import dk.bot.betfairservice.model.BFAccountStatementItem;
import dk.bot.betfairservice.model.BFBetCancelResult;
import dk.bot.betfairservice.model.BFBetCancelResultEnum;
import dk.bot.betfairservice.model.BFBetCategoryType;
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
import dk.bot.betfairservice.model.BFRunnerPrice;
import dk.bot.betfairservice.model.BFSPBetPlaceResult;
import dk.bot.betfairservice.model.LoginResponse;

/**
 * Emulator of BetFair API service.
 * 
 * @author daniel
 * 
 */
public class BetDaqBetFairServiceImpl implements BetFairService {

	private BetDaq betDaq;

	private TransactionCounter txCounter;
	private DataRequestCounter dataRequestCounter;

	private List<Double> oddsLadder;

	public BetDaqBetFairServiceImpl() {
		txCounter = new TransactionCounter();
		dataRequestCounter = new DataRequestCounter();

		BetDaqFactoryBean betDaqFactoryBean = new BetDaqFactoryBean();
		betDaqFactoryBean.setVersion("2.0");
		betDaqFactoryBean.setLanguage("en");
		betDaqFactoryBean.setUserName("xxx");
		betDaqFactoryBean.setPassword("xxx");
		betDaqFactoryBean.init();

		try {
			betDaq = (BetDaq) betDaqFactoryBean.getObject();
			oddsLadder = betDaq.getOddsLadder();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BFBetCancelResult cancelBet(long betId) throws BetFairException {
		dataRequestCounter.waitForPermission(System.currentTimeMillis(), 1);

		betDaq.cancelOrders(Arrays.asList(betId));

		/** 9999 - to indicate that something(>0) was canceled. */
		BFBetCancelResult betCancelResult = new BFBetCancelResult(9999, -1, BFBetCancelResultEnum.OK);
		return betCancelResult;
	}

	public GetAccountFundsResp getAccountFunds() {
		dataRequestCounter.waitForPermission(System.currentTimeMillis(), 1);

		BDAccountBalance accountBalances = betDaq.getAccountBalances();

		GetAccountFundsResp accountFunds = new GetAccountFundsResp();
		accountFunds.setBalance(accountBalances.getBalance().doubleValue());
		accountFunds.setAvailBalance(accountBalances.getAvailableFunds().doubleValue());
		accountFunds.setExposure(accountBalances.getExposure().doubleValue());

		return accountFunds;

	}

	public List<BFAccountStatementItem> getAccountStatement(Date startDate, Date endDate, int recordCount) {
		dataRequestCounter.waitForPermission(System.currentTimeMillis(), 1);

		List<BDAccountPostingItem> bdItems = betDaq.listAccountPostings(startDate, endDate);
		ArrayList<BFAccountStatementItem> bfItems = new ArrayList<BFAccountStatementItem>();

		for (int i = 0; i < bdItems.size(); i++) {
			BDAccountPostingItem bdItem = bdItems.get(i);

			BFAccountStatementItem bfItem = new BFAccountStatementItem();
			if (bdItem.getMarketId() != null) {
				if (bdItem.getMarketId() > Integer.MAX_VALUE) {
					throw new UnsupportedOperationException("Market id is bigger than Integer.MAX_VALUE");
				}
				bfItem.setMarketId(bdItem.getMarketId().intValue());
			} else {
				bfItem.setMarketId(-1);
			}
			bfItem.setMarketName("n/a");
			bfItem.setMarketType("n/a");
			bfItem.setFullMarketName("n/a");

			bfItem.setSelectionId(-1);
			bfItem.setSelectionName("n/a");

			if (bdItem.getOrderId() != null) {
				bfItem.setBetId(bdItem.getOrderId());
			}
			/** Set order id for commission item (assume that it is the next bdItem in the list. */
			else if (bdItem.getMarketId() != null && bdItems.size() > i + 1 && bdItems.get(i + 1).getOrderId() != null) {
				bfItem.setBetId(bdItems.get(i + 1).getOrderId());
			} else {
				/** Non bet items have bet id=0. */
				bfItem.setBetId(0);
			}

			bfItem.setEventTypeId(-1);
			bfItem.setBetCategoryType("n/a");
			bfItem.setBetType("n/a");
			bfItem.setBetSize(-1);
			bfItem.setAvgPrice(-1);

			bfItem.setPlacedDate(new Date(0));
			bfItem.setSettledDate(bdItem.getPostedAt());
			bfItem.setStartDate(new Date(0));

			if (bdItem.getOrderId() == null) {
				bfItem.setCommission(true);
			}

			bfItem.setAmount(bdItem.getAmount().doubleValue());

			bfItems.add(bfItem);
		}
		return bfItems;

	}

	public BetFairServiceInfo getBetFairServiceInfo() {
		BetFairServiceInfo info = new BetFairServiceInfo();
		info.setTxCounterState(txCounter.getState());

		info.setMaxDataRequestPerSecond(dataRequestCounter.getMaxRequestsPerSecond());
		info.setLastSecDataRequest(dataRequestCounter.getLastSecRequestsNum());

		return info;
	}

	public List<EventType> getEventTypes() {
		dataRequestCounter.waitForPermission(System.currentTimeMillis(), 1);
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	public List<BFMUBet> getMUBets(BFBetStatus betStatus) {
		dataRequestCounter.waitForPermission(System.currentTimeMillis(), 1);
		if (betStatus != BFBetStatus.MU) {
			throw new UnsupportedOperationException("Only " + BFBetStatus.MU + " status is supported by getMUBets().");
		}

		/** Get all orders from betdaq. */
		List<BDOrder> allOrders = new ArrayList<BDOrder>();
		BDBootstrapOrders listBootstrapOrders = betDaq.listBootstrapOrders(-1);

		if (listBootstrapOrders.getOrders().size() > 0) {
			allOrders.addAll(listBootstrapOrders.getOrders());
			long lastReceivedSeqNumber = listBootstrapOrders.getOrders()
					.get(listBootstrapOrders.getOrders().size() - 1).getSequenceNumber();

			while (lastReceivedSeqNumber < listBootstrapOrders.getMaxSequenceNumber()) {
				BDBootstrapOrders nextOrders = betDaq.listBootstrapOrders(lastReceivedSeqNumber);

				if (nextOrders.getOrders().size() > 0) {
					allOrders.addAll(nextOrders.getOrders());
					lastReceivedSeqNumber = nextOrders.getOrders().get(nextOrders.getOrders().size() - 1)
							.getSequenceNumber();
				} else {
					break;
				}
			}
		}

		/** Convert BDOrders to BFBets. */
		List<BFMUBet> bets = new ArrayList<BFMUBet>();
		for (BDOrder bdOrder : allOrders) {

			/** Only unmatched and matched bets are returned. */
			if (bdOrder.getStatus() == 1) {

				if (bdOrder.getUnmatchedStake() > 0) {
					BFMUBet bfBet = convertBet(bdOrder);
					bfBet.setBetStatus(BFBetStatus.U);
					bfBet.setPrice(bdOrder.getRequestedPrice());
					bfBet.setSize(bdOrder.getUnmatchedStake());

					bets.add(bfBet);
				}
				if (bdOrder.getMatchedStake() > 0) {
					BFMUBet bfBet = convertBet(bdOrder);
					bfBet.setBetStatus(BFBetStatus.M);
					bfBet.setSize(bdOrder.getMatchedStake());
					bfBet.setPrice(bdOrder.getMatchedPrice());
					bfBet.setMatchedDate(new Date(0));

					bets.add(bfBet);
				}

			} else if (bdOrder.getStatus() == 2) {

				BFMUBet bfBet = convertBet(bdOrder);
				bfBet.setBetStatus(BFBetStatus.M);
				bfBet.setSize(bdOrder.getMatchedStake());
				bfBet.setPrice(bdOrder.getMatchedPrice());
				bfBet.setMatchedDate(new Date(0));

				bets.add(bfBet);
			}

		}

		return bets;
	}

	/** bet status, size and price are not set */
	private BFMUBet convertBet(BDOrder bdOrder) {
		BFMUBet bfBet = new BFMUBet();
		bfBet.setBetId(bdOrder.getId());

		if (bdOrder.getPolarity() == 1) {
			bfBet.setBetType(BFBetType.B);
		} else if (bdOrder.getPolarity() == 2) {
			bfBet.setBetType(BFBetType.L);
		} else {
			throw new IllegalArgumentException("Polarity " + bdOrder.getPolarity() + " is not supported.");
		}
		bfBet.setBetCategoryType(BFBetCategoryType.E);

		if (bdOrder.getMarketId() > Integer.MAX_VALUE) {
			throw new UnsupportedOperationException("Market id is bigger than Integer.MAX_VALUE");
		}
		bfBet.setMarketId((int) bdOrder.getMarketId());

		if (bdOrder.getSelectionId() > Integer.MAX_VALUE) {
			throw new UnsupportedOperationException("Selection id is bigger than Integer.MAX_VALUE");
		}
		bfBet.setSelectionId((int) bdOrder.getSelectionId());
		bfBet.setPlacedDate(bdOrder.getIssuedAt());

		return bfBet;
	}

	public BFMarketDetails getMarketDetails(int marketId) throws BetFairException {
		dataRequestCounter.waitForPermission(System.currentTimeMillis(), 1);

		List<BDMarket> marketInformation = betDaq.getMarketInformation(Arrays.asList((long) marketId));
		if (marketInformation.size() == 0) {
			throw new IllegalArgumentException("Market not found for marketId");
		}
		BDMarket bdMarket = marketInformation.get(0);

		List<BFMarketDetailsRunner> runners = new ArrayList<BFMarketDetailsRunner>();
		for (BDSelection selection : bdMarket.getSelections()) {
			if (selection.getId() > Integer.MAX_VALUE) {
				throw new UnsupportedOperationException("Market id is bigger than Integer.MAX_VALUE");
			}
			runners.add(new BFMarketDetailsRunner((int) selection.getId(), selection.getName()));
		}

		BFMarketDetails marketDetails = new BFMarketDetails(marketId, "Not available", bdMarket.getStartTime(),
				bdMarket.getStartTime(), runners);
		return marketDetails;
	}

	public BFMarketRunners getMarketRunners(int marketId) {
		dataRequestCounter.waitForPermission(System.currentTimeMillis(), 1);

		List<Long> marketIds = Arrays.asList((long) marketId);
		List<BDMarketWithPrices> marketsWithPrices = betDaq.getMarketsWithPrices(marketIds, 0, 3, 3, false);

		if (marketsWithPrices.size() == 0) {
			return null;
		} else {
			BDMarketWithPrices bdMarket = marketsWithPrices.get(0);
			if (bdMarket.getId() > Integer.MAX_VALUE) {
				throw new UnsupportedOperationException("Market id is bigger than Integer.MAX_VALUE:"
						+ bdMarket.getId());
			}

			List<BFMarketRunner> marketRunnersList = new ArrayList<BFMarketRunner>();
			for (BDSelectionWithPrices bdSelection : bdMarket.getSelections()) {
				if (bdSelection.getId() > Integer.MAX_VALUE) {
					throw new UnsupportedOperationException("Selection id is bigger than Integer.MAX_VALUE:"
							+ bdSelection.getId());
				}
				int selectionId = (int) bdSelection.getId();
				List<BFRunnerPrice> prices = new ArrayList<BFRunnerPrice>();
				for (BDPrice bdPrice : bdSelection.getPrices()) {

					double totalToBack = 0;
					double totalToLay = 0;
					if (bdPrice.getType() == BDPRiceTypeEnum.ForSidePrices) {
						totalToBack = bdPrice.getStake();
					} else if (bdPrice.getType() == BDPRiceTypeEnum.AgainstSidePrices) {
						totalToLay = bdPrice.getStake();
					}
					BFRunnerPrice bfRunnerPrice = new BFRunnerPrice(bdPrice.getPrice(), totalToBack, totalToLay);
					prices.add(bfRunnerPrice);
				}

				BFMarketRunner bfMarketRunner = new BFMarketRunner(selectionId, 0, 0, 0, 0, 0, prices);
				marketRunnersList.add(bfMarketRunner);
			}

			int inPlayDelay = 0;
			if (bdMarket.isCurrentlyInRunning()) {
				inPlayDelay = bdMarket.getInRunningDelaySeconds();
			}
			BFMarketRunners marketRunners = new BFMarketRunners((int) bdMarket.getId(), marketRunnersList, inPlayDelay,
					new Date(System.currentTimeMillis()));
			return marketRunners;
		}

	}

	public List<BFMarketData> getMarkets(Date fromDate, Date toDate, Set<Integer> eventTypeIds) {
		dataRequestCounter.waitForPermission(System.currentTimeMillis(), 1);

		List<Long> eventTypeLongIds = new ArrayList<Long>();
		for (Integer eventId : eventTypeIds) {
			eventTypeLongIds.add(eventId.longValue());
		}
		List<BDEvent> bdEvents = betDaq.getEventSubTreeNoSelections(eventTypeLongIds, false);

		List<BDMarket> bdMarkets = new ArrayList<BDMarket>();
		for (BDEvent bdEvent : bdEvents) {
			List<BDMarket> eventMarkets = BDMarketHelper.getAllMarkets(bdEvent);
			bdMarkets.addAll(eventMarkets);
		}

		List<BFMarketData> bfMarkets = new ArrayList<BFMarketData>();
		for (BDMarket bdMarket : bdMarkets) {
			if ((bdMarket.getType() == 1 || bdMarket.getType() == 2 || bdMarket.getType() == 3 || bdMarket.getType()==14)
					&& bdMarket.getStatus() == 2 && bdMarket.getStartTime().getTime() >= fromDate.getTime()
					&& bdMarket.getStartTime().getTime() <= toDate.getTime()) {

				BFMarketData bfMarket = new BFMarketData();
				if (bdMarket.getId() > Integer.MAX_VALUE) {
					throw new UnsupportedOperationException("Market id is bigger than Integer.MAX_VALUE:"
							+ bdMarket.getId());
				} else {
					bfMarket.setMarketId((int) bdMarket.getId());
				}
				bfMarket.setMarketName(bdMarket.getName());
				bfMarket.setMarketType("O");
				bfMarket.setMarketStatus("ACTIVE");
				bfMarket.setEventDate(bdMarket.getStartTime());
				bfMarket.setMenuPath(bdMarket.getEventNamesPath());
				bfMarket.setEventHierarchy(bdMarket.getEventPath());
				if (bdMarket.isCurrentlyInRunning()) {
					bfMarket.setBetDelay("" + bdMarket.getInRunningDelaySeconds());
				} else {
					bfMarket.setBetDelay("" + 0);
				}
				bfMarket.setExchangeId(1);
				bfMarket.setCountryCode("Not available");
				bfMarket.setLastRefresh(null);
				bfMarket.setNumberOfRunners(-1);
				bfMarket.setNumberOfWinners(bdMarket.getNumberOfWinningSelections());
				bfMarket.setTotalAmountMatched(-1);
				bfMarket.setBsbMarket(false);
				bfMarket.setTurningInPlay(bdMarket.isInRunningAllowed());

				bfMarkets.add(bfMarket);
			}
		}

		return bfMarkets;
	}

	public LoginResponse login() {
		dataRequestCounter.waitForPermission(System.currentTimeMillis(), 1);

		throw new UnsupportedOperationException("Not implemented yet.");

	}

	public BFBetPlaceResult placeBet(int marketId, int selectionId, BFBetType betType, double price, double size,
			boolean checkTxLimit) throws BetFairException {
		dataRequestCounter.waitForPermission(System.currentTimeMillis(), 1);
		if (checkTxLimit) {
			txCounter.waitForPermission(System.currentTimeMillis(), false);
		}
		
		double validatedPrice = price;
		short polarity;
		if (betType == BFBetType.B) {
			polarity = 1;
			for (int i = oddsLadder.size() - 1; i >= 0; i--) {
				double odds = oddsLadder.get(i);
				if (odds <= price) {
					validatedPrice = odds;
					break;
				}
			}

		} else if (betType == BFBetType.L) {
			polarity = 2;
			for (int i = 0; i < oddsLadder.size(); i++) {
				double odds = oddsLadder.get(i);
				if (odds >= price) {
					validatedPrice = odds;
					break;
				}
			}
		} else {
			throw new IllegalArgumentException("Unsupported bet type: " + betType);
		}

		BDMarket marketInformation = betDaq.getMarketInformation(Arrays.asList((long) marketId)).get(0);
		long betId = betDaq.placeOrdersNoReceipt(selectionId, size, validatedPrice, polarity, marketInformation
				.getWithdrawalSequenceNumber());

		BFBetPlaceResult betPlaceResult = new BFBetPlaceResult(betId, new Date(System.currentTimeMillis()), betType
				.value(), validatedPrice, size, 0, 0);

		return betPlaceResult;
	}

	public BFSPBetPlaceResult placeSPBet(int marketId, int selectionId, BFBetType betType, double liability,
			Double limit) throws BetFairException {
		dataRequestCounter.waitForPermission(System.currentTimeMillis(), 1);

		txCounter.waitForPermission(System.currentTimeMillis(), false);

		throw new UnsupportedOperationException("Not implemented yet.");
	}

	public List<BFMUBet> getMUBets(BFBetStatus betStatus, int marketId) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	public BFMarketTradedVolume getMarketTradedVolume(int marketId) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

}
