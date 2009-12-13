package dk.bot.webconsole.modules.statistics.birtstatpanel;

import java.io.ByteArrayOutputStream;
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

public class BirtStatPanel extends Panel {

	private BirtService birtService;
	/** exclude from Wicket serialization*/
	private transient IReportRunnable reportRunnable;
	private BirtLinearParam reportParameter;
	
	public BirtStatPanel(String id) {
		super(id);
	}
	
	public BirtStatPanel(String id, IReportRunnable reportRunnable,final BirtLinearParam reportParameter,
			BirtService birtService) {
		super(id);
		this.reportRunnable = reportRunnable;
		this.reportParameter = reportParameter;
		this.birtService = birtService;
	}
	
	public void build() {
		final Form form = new Form("form");
		form.setOutputMarkupId(true);
		add(form);
		form.add(new Label("paramName", reportParameter.getParamName()));

		/** Add dropdown with available reports. */
		final Model selectedParam = new Model();
		final DropDownChoice reportsDropdownList = new DropDownChoice("reportParam", selectedParam, reportParameter
				.getValues());
		form.add(reportsDropdownList);

		/** Add empty label for report data. */
		final Component birtReportLazyPanel = new Label("birtReportLazyPanel", "");
		form.add(birtReportLazyPanel);

		/** Replace lazyLoadingPanel with birt report. */
		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				String paramValue = (String) selectedParam.getObject();
				reportParameter.setSelectedValue(paramValue);

				form.remove(birtReportLazyPanel);
				form.add(createBirtReportLazyPanel(reportParameter));
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
	private Panel createBirtReportLazyPanel(final BirtLinearParam reportParameter) {
		final AjaxLazyLoadPanel reportLazyPanel = new AjaxLazyLoadPanel("birtReportLazyPanel") {
			@Override
			public Component getLazyLoadComponent(String id) {

				String reportString = generateReport(reportRunnable, reportParameter);
				Label reportDataLabel = new Label(id, reportString);
				reportDataLabel.setOutputMarkupId(true);
				reportDataLabel.setEscapeModelStrings(false);
				return reportDataLabel;
			}
		};

		return reportLazyPanel;
	}

	/**
	 * Generates a BIRT report.
	 * 
	 * @param ReportPath
	 *            file path to the report document.
	 * @return
	 */
	private String generateReport(IReportRunnable reportRunnable, BirtLinearParam reportParameter) {
		try {
			List<BirtLinearParam> reportParameters = new ArrayList<BirtLinearParam>(0);
			reportParameters.add(reportParameter);

			ByteArrayOutputStream reportOutputStream = new ByteArrayOutputStream();
			birtService.generateReport(reportRunnable, reportParameters, reportOutputStream);
			return reportOutputStream.toString();
		} catch (EngineException e) {
			return e.getLocalizedMessage();
		}

	}
}
