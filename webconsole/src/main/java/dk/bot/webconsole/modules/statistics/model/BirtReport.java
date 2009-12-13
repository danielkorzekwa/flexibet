package dk.bot.webconsole.modules.statistics.model;

import java.io.Serializable;

/** Birt report model.
 * 
 * @author daniel
 *
 */
public class BirtReport implements Serializable{

	private final String reportName;
	
	/**Birt .rptdesign file.*/
	private final String reportFilePath;
	
	public BirtReport(String reportName,String reportFilePath) {
		this.reportName=reportName;
		this.reportFilePath=reportFilePath;
	}

	public String getReportName() {
		return reportName;
	}

	public String getReportFilePath() {
		return reportFilePath;
	}
	
	@Override
	public String toString() {
		return reportName;
	}
}
