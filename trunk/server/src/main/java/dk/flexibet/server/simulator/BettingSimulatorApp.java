package dk.flexibet.server.simulator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;
import org.apache.commons.math.util.MathUtils;
import org.joda.time.DateTime;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dk.bot.marketobserver.model.MarketData;

/** Main class for a betting simulator. */
public class BettingSimulatorApp {

	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void main(String[] args) throws IOException {
		System.out.println("Starting betting simulator...");
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {
				"spring-flexibet-simulator.xml", "spring-flexibet-simulator-ds.xml" });
		BettingSimulator bettingSimulator = (BettingSimulator) ctx.getBean("bettingSimulator");

		String marketsSql;
		if (args.length == 0) {
			 marketsSql = "select * from market where market_time>='" + df.format(new DateTime().minusDays(7).toDate()) + "' and market_time<='" + df.format(new DateTime().toDate()) + "'";
			System.out
					.println("Type SQL query to get markets for simulation.\nDefault: " + marketsSql);

			System.out.print("\nQuery: ");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String inputData = reader.readLine();
			if(inputData.length()>0) {
				marketsSql=inputData;
			}
		} else {
			marketsSql = args[0];
		}

		System.out.println("Simulation in progress...");
		
		BettingSimulationReport report = bettingSimulator.simulate(marketsSql, new BettingSimulationListener() {
		
			public void afterMarketAnalysis(int marketNumber,MarketData marketData) {
				System.out.print("#");
				if((marketNumber) % 80 ==0) {
					System.out.print("\n");
				}
		
			}
		
			public void afterGetMarkets(int numOfMarkets) {
				System.out.println("Markets to analyze: " + numOfMarkets);
			}
		
		});

		System.out.println("\n\nBetting report:");
		System.out.println("Number of markets: " + report.getNumOfMarkets());
		System.out.println("Number of analyzed markets: " + report.getNumOfAnalyzedMarkets());
		System.out.println("Number of matched bets: " + report.getNumOfMatchedBets());
		System.out.println("Total liability: " + MathUtils.round(report.getTotalLiability(), 2) + "\n");
		System.out.println(report.getReport());
	}
}
