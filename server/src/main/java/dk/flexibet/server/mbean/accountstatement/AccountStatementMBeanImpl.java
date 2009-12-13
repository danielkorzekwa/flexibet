package dk.flexibet.server.mbean.accountstatement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import dk.flexibet.server.dao.AccountStatementDAO;
import dk.flexibet.server.mbean.accountstatement.model.AccountStatementStat;
import dk.flexibet.server.mbean.accountstatement.model.MarketRunnerStatement;
import dk.flexibet.server.mbean.accountstatement.model.MarketStat;
import dk.flexibet.server.mbean.accountstatement.model.StateMachineStat;
import dk.flexibet.server.model.DetailedAccountStatementItem;

@ManagedResource(objectName = "dk.flexibet.bettingserver:name=AccountStatement")
public class AccountStatementMBeanImpl implements AccountStatementMBean {
	
	@Resource
	private AccountStatementDAO accountStatementDAO;

	public AccountStatementMBeanImpl() {
	}

	public AccountStatementMBeanImpl(AccountStatementDAO accountStatementDAO) {
		this.accountStatementDAO = accountStatementDAO;
	}

	@ManagedOperation
	@ManagedOperationParameters( { @ManagedOperationParameter(name = "dateFrom", description = ""),
			@ManagedOperationParameter(name = "stateMachineId", description = ""),
			@ManagedOperationParameter(name = "stateName", description = ""),
			@ManagedOperationParameter(name = "betItems", description = "") })
	public List<DetailedAccountStatementItem> getAccountStatement(Date from, String stateMachineId, String stateName,
			boolean betItems) {

		List<DetailedAccountStatementItem> items;

		if (betItems) {
			if (stateMachineId == null) {
				items = accountStatementDAO.getItems(from, new Date(System.currentTimeMillis()), true);

			} else {
				List<DetailedAccountStatementItem> detailedItems = accountStatementDAO.getItems(from, new Date(
						System.currentTimeMillis()), true);
				items = new ArrayList<DetailedAccountStatementItem>();
				for (DetailedAccountStatementItem detailedItem : detailedItems) {
					if (detailedItem.getStateMachineId()!=null && detailedItem.getStateMachineId().equals(stateMachineId)
							&& (stateName == null || detailedItem.getStateName().equals(stateName))) {
						items.add(detailedItem);
					}
				}
			}
		} else {
			items = accountStatementDAO.getItems(from, new Date(System.currentTimeMillis()), false);
		}
		return items;
	}

	@ManagedAttribute
	public AccountStatementStat[] getAccountStatementStat() {
		long now = System.currentTimeMillis();

		List<DetailedAccountStatementItem> items = accountStatementDAO.getItems(new Date(now - 1000 * 3600 * 24
				* 14), new Date(now), true);

		return getAccountStatementStat(items, now);

	}

	private AccountStatementStat[] getAccountStatementStat(List<DetailedAccountStatementItem> lastTwoWeeksItems,
			long now) {
		Calendar startOfWeek = GregorianCalendar.getInstance();
		startOfWeek.setTimeInMillis(now);
		startOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		startOfWeek.set(Calendar.HOUR_OF_DAY, 0);
		startOfWeek.set(Calendar.MINUTE, 0);
		startOfWeek.set(Calendar.SECOND, 0);
		startOfWeek.set(Calendar.MILLISECOND, 0);

		List<DetailedAccountStatementItem> thisWeekItems = filterAccountStatementItems(lastTwoWeeksItems, new Date(
				startOfWeek.getTimeInMillis()), new Date(now));

		List<DetailedAccountStatementItem> lastWeekItems = filterAccountStatementItems(lastTwoWeeksItems, new Date(
				startOfWeek.getTimeInMillis() - 1000 * 3600 * 24 * 7), new Date(startOfWeek.getTimeInMillis()));

		AccountStatementStat[] stat = new AccountStatementStat[2];
		stat[0] = getAccountStatementStat("This week", thisWeekItems);
		stat[1] = getAccountStatementStat("Last week", lastWeekItems);

		return stat;
	}

	private AccountStatementStat getAccountStatementStat(String timePeriodLabel,
			List<DetailedAccountStatementItem> items) {
		/** Prepare market stat */
		MarketStat marketsStat = MarketStatHelper.getAccountProfitLoss(items);
		marketsStat.setTimePeriod(timePeriodLabel);

		/** Prepare states stat */
		List<MarketRunnerStatement> marketRunnerStatements = StateStatHelper.getMarketRunnerStatements(items);
		List<StateMachineStat> statesMachineStats = StateStatHelper.getStatesStat(marketRunnerStatements);

		/** Prepare stat object to return. [0] - this week, [1] - last week */
		AccountStatementStat stat = new AccountStatementStat();
		stat.setMarketStat(marketsStat);
		stat.setProfitPerStat(statesMachineStats);

		return stat;
	}

	private List<DetailedAccountStatementItem> filterAccountStatementItems(
			List<DetailedAccountStatementItem> accountStatementItems, Date startDate, Date endDate) {
		/** Filter to match start/end dates */
		List<DetailedAccountStatementItem> filteredItems = new ArrayList<DetailedAccountStatementItem>();
		for (DetailedAccountStatementItem item : accountStatementItems) {
			long settledDate = item.getSettledDate().getTime();
			if (settledDate >= startDate.getTime() && settledDate < endDate.getTime()) {
				filteredItems.add(item);
			}
		}

		return filteredItems;
	}

}
