package dk.bot.birt;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;

/**
 * Allows generating birt reports.
 * 
 * @author daniel
 * 
 */
public interface BirtService {

	/** Parse Birt report
	 * 
	 * @param reportFileInput Input stream to the report *.rptdesign file
	 * @return parsed report object
	 */
	public IReportRunnable parseReport(InputStream reportFileInput)throws EngineException;
	
	/** Generates betfair report in a html format (only body tag)
	 * 
	 * @param report Parsed Birt report. 
	 * @param report parameters to set.
	 * @param reportOutput Generated report in a html format (only body tag)
	 */
	public void generateReport(IReportRunnable report, List<BirtLinearParam> reportParameters, OutputStream reportOutput) throws EngineException;
	
	public List<BirtLinearParam> getReportParameters(IReportRunnable report);
}
