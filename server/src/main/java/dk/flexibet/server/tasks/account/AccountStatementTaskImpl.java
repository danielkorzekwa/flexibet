package dk.flexibet.server.tasks.account;

import java.util.Date;
import java.util.List;

import dk.bot.betfairservice.BetFairService;
import dk.bot.betfairservice.model.BFAccountStatementItem;
import dk.bot.marketobserver.util.AbstractTask;
import dk.flexibet.server.dao.AccountStatementDAO;
import dk.flexibet.server.dao.RunnerStateDAO;
import dk.flexibet.server.model.DetailedAccountStatementItem;
import dk.flexibet.server.model.factory.AccountStatementItemFactory;

/**
 * Updates account statement in db.
 * 
 * @author daniel
 * 
 */
public class AccountStatementTaskImpl extends AbstractTask {

	private final AccountStatementDAO accountStatementDao;
	private final BetFairService betFairService;
	private final RunnerStateDAO runnerStateDao;

	public AccountStatementTaskImpl(String taskName, AccountStatementDAO accountStatementDao,RunnerStateDAO runnerStateDao,BetFairService betFairService) {
		super(taskName);
		this.accountStatementDao = accountStatementDao;
		this.runnerStateDao = runnerStateDao;
		this.betFairService = betFairService;
	}

	@Override
	public void doExecute() {
		Date latestSettledDate = accountStatementDao.getLastestSettledDate();
		latestSettledDate = new Date(latestSettledDate.getTime() - (1000 * 60));

		List<BFAccountStatementItem> bfItems = betFairService.getAccountStatement(latestSettledDate, new Date(System
				.currentTimeMillis()), Integer.MAX_VALUE);
		List<DetailedAccountStatementItem> items = AccountStatementItemFactory.create(bfItems,runnerStateDao);

		accountStatementDao.deleteItems(latestSettledDate);

		accountStatementDao.add(items);
	}
}
