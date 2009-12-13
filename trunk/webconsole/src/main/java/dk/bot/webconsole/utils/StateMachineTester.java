package dk.bot.webconsole.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.scxml.model.CustomAction;
import org.apache.commons.scxml.model.ModelException;

import dk.bot.bettingengine.statemachine.context.MarketRunnerCtxFactoryImpl;
import dk.bot.bettingengine.statemachine.executor.StateMachineExecutor;

/**
 * Run a random test of a state machine.
 * 
 * @author daniel
 * 
 */
public class StateMachineTester {

	public static void main(String[] args) throws MalformedURLException, ModelException {

		Random random = new Random(System.currentTimeMillis());

		String baseDir = BotHomeHelper.getBotHome();

		URL url = new File(baseDir + "/conf/testing/testmachinetester.scxml").toURI().toURL();

		StateMachineExecutor machine = new StateMachineExecutor(url, new ArrayList<CustomAction>());

		SortedSet<String> finalStates = new TreeSet<String>();

		for (int i = 0; i < 20; i++) {

			for (int j = 0; j < 100; j++) {
				machine.fireEvent("event", new MarketRunnerCtxFactoryImpl().createRandomContext(random));
				if (machine.isFinalState()) {
					break;
				}
			}
			finalStates.add(machine.getCurrentStateId());
			machine.reset();
		}

		String reportString = VisitedStatesReport.buildReport(machine.getVisitedStates());
		System.out.println(reportString);
	}

}
