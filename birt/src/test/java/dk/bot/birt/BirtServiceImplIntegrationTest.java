package dk.bot.birt;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BirtServiceImplIntegrationTest {

	private BirtServiceImpl birtService = new BirtServiceImpl();
	
	@Before
	public void setUp() throws BirtException {
		birtService.setBirtHome("c:/birt-runtime-2_3_1/ReportEngine");
		birtService.init();
	}
	
	/** Test only against exceptions*/
	@Test
	public void testGenerateReport() throws EngineException, FileNotFoundException {
		
		FileInputStream reportInputStream = new FileInputStream("./src/test/resources/sport_region_period.rptdesign");
		ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
		IReportRunnable report = birtService.parseReport(reportInputStream);
		birtService.generateReport(report, new ArrayList<BirtLinearParam>(),reportOutput );
	}

	@Test
	public void testGenerateReportWithParameters() throws EngineException, FileNotFoundException {
		
		FileInputStream reportInputStream = new FileInputStream("./src/test/resources/sport_state_period.rptdesign");
		ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
		IReportRunnable report = birtService.parseReport(reportInputStream);
		
		ArrayList<BirtLinearParam> parameters = new ArrayList<BirtLinearParam>();
		parameters.add(new BirtLinearParam("sport_name","SOCCER"));
		
		birtService.generateReport(report, parameters,reportOutput );
	}
	
	@Test(expected=Exception.class)
	public void testGenerateReportParameterNotSet() throws EngineException, FileNotFoundException {
		
		FileInputStream reportInputStream = new FileInputStream("./src/test/resources/sport_state_period.rptdesign");
		ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
		IReportRunnable report = birtService.parseReport(reportInputStream);
		birtService.generateReport(report, new ArrayList<BirtLinearParam>(),reportOutput );
	}
	
	@Test(expected=Exception.class)
	public void testGenerateReportWrongParameterName() throws EngineException, FileNotFoundException {
		
		FileInputStream reportInputStream = new FileInputStream("./src/test/resources/sport_state_period.rptdesign");
		ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
		IReportRunnable report = birtService.parseReport(reportInputStream);
		
		ArrayList<BirtLinearParam> parameters = new ArrayList<BirtLinearParam>();
		parameters.add(new BirtLinearParam("wrongName","value"));
		
		birtService.generateReport(report, parameters,reportOutput );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGenerateReportNoParamValue() throws EngineException, FileNotFoundException {
		
		FileInputStream reportInputStream = new FileInputStream("./src/test/resources/sport_state_period.rptdesign");
		ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
		IReportRunnable report = birtService.parseReport(reportInputStream);
		
		ArrayList<BirtLinearParam> parameters = new ArrayList<BirtLinearParam>();
		parameters.add(new BirtLinearParam("wrongName",null));
	
		birtService.generateReport(report, parameters,reportOutput );
	}
	
	/** Test getting report parameters*/
	@Test
	public void testGetReportParameters() throws EngineException, FileNotFoundException {
		
		FileInputStream reportInputStream = new FileInputStream("./src/test/resources/sport_state_period.rptdesign");
		
		IReportRunnable report = birtService.parseReport(reportInputStream);
		List<BirtLinearParam> params = birtService.getReportParameters(report);
		
		assertEquals(1, params.size());
		assertEquals("sport_name", params.get(0).getParamName());
		assertEquals(true, params.get(0).getValues().size()>0);	
	}
	
}
