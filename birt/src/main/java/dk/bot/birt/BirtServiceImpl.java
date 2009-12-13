package dk.bot.birt;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;

/**
 * 
 * @author daniel
 * 
 */
public class BirtServiceImpl implements BirtService {

	private String birtHome;

	private IReportEngine engine = null;

	public void setBirtHome(String birtHome) {
		this.birtHome = birtHome;
	}

	public void init() throws BirtException {
		EngineConfig config = new EngineConfig();
		config.setEngineHome(birtHome);
		config.setLogConfig(null, Level.WARNING);

		Platform.startup(config); // If using RE API in Eclipse/RCP
		// application this is not needed.
		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		engine = factory.createReportEngine(config);
		engine.changeLogLevel(Level.WARNING);
	}

	public IReportRunnable parseReport(InputStream reportFileInput) throws EngineException {
		return engine.openReportDesign(reportFileInput);
	}

	public void generateReport(IReportRunnable report, List<BirtLinearParam> reportParameters, OutputStream reportOutput)
			throws EngineException {
		IRunAndRenderTask task = engine.createRunAndRenderTask(report);

		// Set parent classloader for engine
		task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, BirtServiceImpl.class.getClassLoader());

		// Set parameter values and validate
		if (reportParameters != null) {
			for (BirtLinearParam param : reportParameters) {
				if (param.getSelectedValue() == null) {
					throw new IllegalArgumentException("Birt param value not set: " + param.getParamName());
				} else {
					task.setParameterValue(param.getParamName(), param.getSelectedValue());
				}
			}
		}
		task.validateParameters();

		// Setup rendering to HTML
		HTMLRenderOption options = new HTMLRenderOption();
		options.setOutputStream(reportOutput);

		options.setOutputFormat("html");
		// Setting this to true removes html and body tags
		options.setEmbeddable(true);

		task.setRenderOption(options);
		// run and render report
		task.run();
		task.close();
	}

	public List<BirtLinearParam> getReportParameters(IReportRunnable report) {

		List<BirtLinearParam> birtParams = new ArrayList<BirtLinearParam>();

		IGetParameterDefinitionTask task = engine.createGetParameterDefinitionTask(report);
		Collection params = task.getParameterDefns(true);
		Iterator iter = params.iterator();
		// Iterate over all parameters
		while (iter.hasNext()) {
			IParameterDefnBase param = (IParameterDefnBase) iter.next();
			// Group section found
			if (param instanceof IParameterGroupDefn) {
				throw new UnsupportedOperationException("Group parameters are not supported.");
			} else {
				// Parameters are not in a group
				IScalarParameterDefn scalar = (IScalarParameterDefn) param;

				// Parameter is a List Box
				if (scalar.getControlType() == IScalarParameterDefn.LIST_BOX) {

					BirtLinearParam birtParam = new BirtLinearParam();
					birtParam.setParamName(scalar.getName());

					Collection selectionList = task.getSelectionList(param.getName());
					// Selection contains data
					if (selectionList != null) {
						List<String> paramValues = new ArrayList<String>();
						for (Iterator sliter = selectionList.iterator(); sliter.hasNext();) {
							// Print out the selection choices
							IParameterSelectionChoice selectionItem = (IParameterSelectionChoice) sliter.next();
							String value = (String) selectionItem.getValue();
							paramValues.add(value);
						}
						birtParam.setValues(paramValues);
					}

					birtParams.add(birtParam);
				} else {
					throw new UnsupportedOperationException("Param type not supported: " + scalar.getControlType());
				}
			}
		}

		return birtParams;
	}

}
