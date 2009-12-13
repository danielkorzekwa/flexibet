package dk.bot.webconsole.pages.histpricechart.visualization;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.google.visualization.datasource.DataSourceHelper;
import com.google.visualization.datasource.DataSourceRequest;
import com.google.visualization.datasource.base.DataSourceException;
import com.google.visualization.datasource.base.DataSourceParameters;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.DateTimeValue;
import com.google.visualization.datasource.datatable.value.Value;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.query.Query;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Generates time series interactive chart based on Google Visualisation API.
 * 
 * I'm a thread-safe.
 * 
 * @author korzekwad
 * 
 */
public class TimeLineChart {

	private final VelocityEngine velocityEngine;

	public TimeLineChart(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	/**
	 * Generated html report
	 * 
	 * @throws DataSourceException
	 */
	public String generate(TimeChartData chartData) throws VisualizationException {
		try {
			String dataTableJson = generateDataTableJson(chartData);
			String geoMapHtmlPage = generateHtmlGeoPage(dataTableJson);
			return geoMapHtmlPage;
		} catch (Exception e) {
			throw new VisualizationException(e);
		}
	}

	private String generateDataTableJson(TimeChartData chartData) throws DataSourceException {
		DataTable data = new DataTable();
		ArrayList<ColumnDescription> cd = new ArrayList<ColumnDescription>();
		cd.add(new ColumnDescription("date", ValueType.DATETIME, "Date"));

		for (String serie : chartData.getSeries()) {
			cd.add(new ColumnDescription(serie, ValueType.NUMBER, serie));
		}
		data.addColumns(cd);
		try {
		
			for (TimeChartDataValue timeSerie : chartData.getValues()) {

				TableRow row = new TableRow();
				GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
				calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
				calendar.setTimeInMillis(TimeZone.getDefault().getOffset(timeSerie.getTimestamp().getTime()) + timeSerie.getTimestamp().getTime());
				row.addCell(new TableCell(new DateTimeValue(calendar)));
				for (Double value : timeSerie.getValues()) {
					if(value!=null) {
					row.addCell(value);
					}
					else {
						row.addCell(Value.getNullValueFromValueType(ValueType.NUMBER));
					}
				}
				data.addRow(row);
			}

		} catch (TypeMismatchException e) {
			System.out.println("Invalid type!");
		}
		Query query = new Query();
		DataSourceParameters parameters = new DataSourceParameters("");
		DataSourceRequest request = new DataSourceRequest(query, parameters, ULocale.UK);
		String generateResponse = DataSourceHelper.generateResponse(data, request);

		return generateResponse;
	}

	private String generateHtmlGeoPage(String dataTableJson) throws ParseErrorException, MethodInvocationException,
			ResourceNotFoundException, IOException, URISyntaxException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		InputStreamReader templateReader = new InputStreamReader(this.getClass().getResourceAsStream("timelinechart_template.html"));
		VelocityContext ctx = new VelocityContext();
		ctx.put("rawdata", dataTableJson);
		Writer writer = new StringWriter();
		velocityEngine.evaluate(ctx, writer, "logTag", templateReader);
		return writer.toString();
	}
}
