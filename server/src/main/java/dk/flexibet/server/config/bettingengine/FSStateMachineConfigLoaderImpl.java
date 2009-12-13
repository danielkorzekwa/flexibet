package dk.flexibet.server.config.bettingengine;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.ConfigObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.scxml.model.CustomAction;

import dk.bot.bettingengine.statemachine.StateMachineInfo;
import dk.bot.bettingengine.statemachine.StateMachineServiceConfig;
import dk.bot.marketobserver.model.MarketFilter;

/**
 * Loads state machines from directory. The stateMachineId is a name of state machine file, e.g. default.scxml ->
 * default.
 * 
 * @author daniel
 * 
 */
public class FSStateMachineConfigLoaderImpl implements StateMachineConfigLoader {

	private Binding binding = new Binding();
	private GroovyShell shell = new GroovyShell(binding);

	private final String stateMachinesConfigFile;
	private String baseDir;

	/** File path to statemachines.properties configuration file */
	public FSStateMachineConfigLoaderImpl(String stateMachineConfigFilepath) {
		this.stateMachinesConfigFile = stateMachineConfigFilepath;
		baseDir = FilenameUtils.getFullPath(stateMachineConfigFilepath);
	}

	public StateMachineServiceConfig load() throws StateMachineConfigLoaderException {

		try {
			binding.setVariable("configFile", stateMachinesConfigFile);
			ConfigObject configFileMap = (ConfigObject) shell
					.evaluate("new ConfigSlurper().parse(new File(configFile).toURL())");

			List<CustomAction> customActions = parseCustomActions(configFileMap);
			List<StateMachineInfo> stateMachines = parseStateMachines(configFileMap);
			StateMachineServiceConfig serviceConfig = new StateMachineServiceConfig(stateMachines, customActions);
				
			return serviceConfig;
		} catch (Exception e) {
			throw new StateMachineConfigLoaderException(e);
		}
	}

	private List<CustomAction> parseCustomActions(ConfigObject configFileMap) throws ClassNotFoundException {
		List<CustomAction> customActions = new ArrayList<CustomAction>();

		Map customActionsMap = (Map) configFileMap.get("customaction");
		if (customActionsMap != null) {

			for (Object customActionName : customActionsMap.keySet()) {
				Map customActionMap = (Map) customActionsMap.get(customActionName);
				String namespace = (String) customActionMap.get("namespace");
				String classname = (String) customActionMap.get("classname");
				Class customActionClass = Class.forName(classname);

				customActions.add(new CustomAction(namespace, (String) customActionName, customActionClass));
			}
		}

		return customActions;
	}

	private List<StateMachineInfo> parseStateMachines(ConfigObject configFileMap) throws MalformedURLException, URISyntaxException {
		/** List of state machine */
		List<String> stateMachineNames = (List<String>) configFileMap.get("statemachines");
		
		List<StateMachineInfo> stateMachines = new ArrayList<StateMachineInfo>();
		for (String stateMachineName : stateMachineNames) {
			File stateMachineFile = new File(baseDir + "/statemachine/" + stateMachineName + ".scxml");
			String stateMachineInfoFile = baseDir + "/statemachine/" + stateMachineName + ".info";

			List<MarketFilter> marketFilters = getMarketFilters(stateMachineInfoFile);
			StateMachineInfo stateMachineInfo = new StateMachineInfo(stateMachineName,
					stateMachineFile.toURI().toURL(), marketFilters);
			stateMachines.add(stateMachineInfo);
		}

		return stateMachines;
	}

	private List<MarketFilter> getMarketFilters(String stateMachineInfoFile) {
		List<MarketFilter> marketFilters = new ArrayList<MarketFilter>();

		binding.setVariable("configFile", stateMachineInfoFile);

		ConfigObject value = (ConfigObject) shell.evaluate("new ConfigSlurper().parse(new File(configFile).toURL())");

		Map marketFilersMap = (Map) value.get("marketfilter");
		if (marketFilersMap != null) {

			for (Object filterName : marketFilersMap.keySet()) {
				Map marketFilterMap = (Map) marketFilersMap.get(filterName);
				String eventPath = (String) marketFilterMap.get("eventpath");
				String marketName = (String) marketFilterMap.get("marketname");
				
				String bwinMatchedString = (String)marketFilterMap.get("bwinmatched");
				if(bwinMatchedString==null) {
					bwinMatchedString="false";
				}
				boolean bwinMatched = new Boolean(bwinMatchedString);
				
				String inPlayString = (String)marketFilterMap.get("inplay");
				if(inPlayString==null) {
					inPlayString="false";
				}
				boolean inPlay = new Boolean(inPlayString);
				
				MarketFilter marketFilter = new MarketFilter((String) filterName, eventPath,
						marketName,bwinMatched,inPlay);
				marketFilters.add(marketFilter);
			}
		}
		return marketFilters;
	}

}
