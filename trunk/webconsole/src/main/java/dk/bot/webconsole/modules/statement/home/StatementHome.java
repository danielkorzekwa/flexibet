package dk.bot.webconsole.modules.statement.home;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dk.bot.webconsole.modules.statement.home.accountstatementpanel.AccountStatementPanel;
import dk.bot.webconsole.modules.statement.home.navigation.NavigationPanel;
import dk.bot.webconsole.modules.statement.home.navigation.NavigationPanelListener;
import dk.bot.webconsole.modules.statement.home.stathelper.AccountStatementOrderEnum;
import dk.bot.webconsole.modules.statement.home.stathelper.TimePeriod;
import dk.flexibet.server.mbean.accountstatement.AccountStatementMBean;
import dk.flexibet.server.model.DetailedAccountStatementItem;

public class StatementHome extends BreadCrumbPanel {

	@SpringBean(name = "accountStatementMBean")
	private AccountStatementMBean accountStatementMBean;

	public StatementHome(String id, final IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		final Form form = new Form("form");
		form.setOutputMarkupId(true);
		add(form);

		NavigationPanelListener listener = new NavigationPanelListener() {
			public void onUpdate(TimePeriod period, String stateMachineId, String stateName,
					AccountStatementOrderEnum orderBy, boolean betItems) {
				form.remove("accountStatementStat");
				form.add(getAccountStatementStat(period.getDateFrom(), stateMachineId, stateName, orderBy, betItems));
			}
		};

		NavigationPanel navigationPanel = new NavigationPanel("navigationPanel", form, listener);
		form.add(navigationPanel);

		final Panel accountStatementPanel = getAccountStatementStat(navigationPanel.getDefaultPeriod().getDateFrom(),
				null, null, AccountStatementOrderEnum.SETTLED_DATE, true);
		form.add(accountStatementPanel);

	}

	/**
	 * Gets account statements from given date.
	 * 
	 * @param dateFrom
	 * @return
	 */
	private Panel getAccountStatementStat(final Date dateFrom, final String stateMachineId, final String stateName,
			final AccountStatementOrderEnum orderBy, final boolean betItems) {
		AjaxLazyLoadPanel ajaxLazyLoadPanel = new AjaxLazyLoadPanel("accountStatementStat") {
			@Override
			public Component getLazyLoadComponent(String markupId) {
				List<DetailedAccountStatementItem> items = accountStatementMBean.getAccountStatement(dateFrom,
						stateMachineId, stateName, betItems);
				sort(items, orderBy);
				return new AccountStatementPanel(markupId, StatementHome.this, items);
			}
		};

		return ajaxLazyLoadPanel;

	}

	private void sort(List<DetailedAccountStatementItem> items, AccountStatementOrderEnum order) {
		if (order == AccountStatementOrderEnum.SETTLED_DATE) {
			Collections.sort(items, new Comparator<DetailedAccountStatementItem>() {

				public int compare(DetailedAccountStatementItem o1, DetailedAccountStatementItem o2) {
					int i = o2.getSettledDate().compareTo(o1.getSettledDate());
					if (i != 0) return i;
					
					i = new Long(o2.getMarketId()).compareTo(new Long(o1.getMarketId()));
					if (i != 0) return i;
					
					i = new Long(o2.getSelectionId()).compareTo(new Long(o1.getSelectionId()));
					if (i != 0) return i;
					
					return o2.getPlacedDate().compareTo(o1.getPlacedDate());
					
				}

			});

		} else if (order == AccountStatementOrderEnum.STAKE) {
			Collections.sort(items, new Comparator<DetailedAccountStatementItem>() {

				public int compare(DetailedAccountStatementItem o1, DetailedAccountStatementItem o2) {
					return Double.compare(o2.getBetSize(), o1.getBetSize());
				}
			});
		}
	}

	public String getTitle() {
		return "Home";
	}
}
