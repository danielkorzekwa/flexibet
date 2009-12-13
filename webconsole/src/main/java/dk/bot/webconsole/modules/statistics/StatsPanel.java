package dk.bot.webconsole.modules.statistics;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;

import dk.bot.birt.BirtLinearParam;
import dk.bot.birt.BirtService;
import dk.bot.webconsole.modules.statistics.birtstatpanel.BirtStatPanel;
import dk.bot.webconsole.modules.statistics.model.BirtReport;
import dk.bot.webconsole.utils.BotHomeHelper;

/**
 * Generates BIRT reports.
 * 
 * @author daniel
 * 
 */
public class StatsPanel extends Panel {

	private List<BirtReport> reportsList = new ArrayList<BirtReport>();
	private final BirtService birtService;

	public StatsPanel(String id,BirtService birtService) {
		super(id);
		this.birtService = birtService;

		reportsList.add(new BirtReport("Sport_Region_Period", "sport_region_period.rptdesign"));
		reportsList.add(new BirtReport("State_Period", "state_period.rptdesign"));
		reportsList.add(new BirtReport("Sport_State_Period", "sport_state_period.rptdesign"));
		reportsList.add(new BirtReport("HR_GB_MarketType_Period", "hr_gb_markettype_period.rptdesign"));

		build();
	}

	private void build() {

		final Form form = new Form("form");
		form.setOutputMarkupId(true);
		add(form);

		/** Add dropdown with available reports. */
		final Model selectedReport = new Model();
		final DropDownChoice reportsDropdownList = new DropDownChoice("reportsList", selectedReport, reportsList);
		form.add(reportsDropdownList);

		/** Add empty label for report data. */
		final Component birtReportLazyPanel = new Label("birtReportLazyPanel", "");
		form.add(birtReportLazyPanel);

		/** Replace lazyLoadingPanel with birt report. */
		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				BirtReport birtReport = (BirtReport) selectedReport.getObject();
				form.remove(birtReportLazyPanel);
				form.add(createBirtReportLazyPanel(BotHomeHelper.getBotHome() + "/birt/reports/"
						+ birtReport.getReportFilePath()));
				target.addComponent(form);
			}
		};
		reportsDropdownList.add(onChangeAjaxBehavior);
	}

	/**
	 * Create a new lazyLoadingPanel with BIRT report.
	 * 
	 * @param reportPath
	 * @return
	 */
	private Panel createBirtReportLazyPanel(final String reportPath) {
		final AjaxLazyLoadPanel reportLazyPanel = new AjaxLazyLoadPanel("birtReportLazyPanel") {
			@Override
			public Component getLazyLoadComponent(String id) {

				try {
					IReportRunnable reportRunnable = readReport(reportPath);

					List<BirtLinearParam> reportParameters = birtService.getReportParameters(reportRunnable);
					if (reportParameters.size() == 0) {
						String reportString = generateReport(reportRunnable);
						Label reportDataLabel = new Label(id, reportString);
						reportDataLabel.setOutputMarkupId(true);
						reportDataLabel.setEscapeModelStrings(false);
						return reportDataLabel;
					}
					else if(reportParameters.size()==1) {
						BirtStatPanel birtStatPanel = new BirtStatPanel(id,reportRunnable,reportParameters.get(0),birtService);
						birtStatPanel.build();
						return birtStatPanel;
					}
					else {
						return new Label(id, "Birt reports with more than 1 parameters are not supported.");
					}

				} catch (Exception e) {
					return new Label(id, e.getLocalizedMessage());
				}
			}
		};

		return reportLazyPanel;
	}

	private IReportRunnable readReport(String reportPath) throws FileNotFoundException, EngineException {
		FileInputStream reportInputStream = new FileInputStream(reportPath);

		IReportRunnable reportRunnable = birtService.parseReport(reportInputStream);
		return reportRunnable;
	}

	/**
	 * Generates a BIRT report.
	 * 
	 * @param ReportPath
	 *            file path to the report document.
	 * @return
	 */
	private String generateReport(IReportRunnable reportRunnable) {
		try {
			ByteArrayOutputStream reportOutputStream = new ByteArrayOutputStream();
			birtService.generateReport(reportRunnable, null,reportOutputStream);
			return reportOutputStream.toString();
		} catch (EngineException e) {
			return e.getLocalizedMessage();
		}

	}

}
