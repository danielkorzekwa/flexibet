package dk.flexibet.server.config.bettingengine;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.scxml.model.CustomAction;
import org.junit.Test;

import dk.bot.bettingengine.statemachine.StateMachineInfo;
import dk.bot.bettingengine.statemachine.StateMachineServiceConfig;
import dk.bot.marketobserver.model.MarketFilter;

public class FSStateMachineConfigLoaderImplTest {

	@Test
	public void testLoad() {
		StateMachineServiceConfig stateMachines = new FSStateMachineConfigLoaderImpl("src/test/resources/conf/bettingengine.properties")
				.load();

		assertCustomActions(stateMachines.getCustomActions());
		assertStateMachines(stateMachines.getStateMachines());
	}

	private void assertCustomActions(List<CustomAction> customActions) {
		assertEquals(3, customActions.size());

		assertEquals("placeBet", customActions.get(0).getLocalName());
		assertEquals("http://my.custom-actions/CUSTOM", customActions.get(0).getNamespaceURI());
		assertEquals("dk.bot.bettingengine.statemachine.customaction.PlaceBet", customActions.get(0).getActionClass().getName());

		assertEquals("cancelBet", customActions.get(2).getLocalName());
		assertEquals("http://my.custom-actions/CUSTOM", customActions.get(2).getNamespaceURI());
		assertEquals("dk.bot.bettingengine.statemachine.customaction.CancelBet", customActions.get(2).getActionClass().getName());

	}

	private void assertStateMachines(List<StateMachineInfo> stateMachines) {
		

		/** Check for testmachine.info */
		StateMachineInfo stateMachineInfo = stateMachines.get(0);
		assertEquals("testmachine", stateMachineInfo.getStateMachineId());
		assertEquals(
				"file:/C:/Perforce/default/flexibet/server/src/test/resources/conf/statemachine/testmachine.scxml",
				stateMachineInfo.getStateMachineUrl().toString());

		List<MarketFilter> marketFilters = stateMachineInfo.getMarketFilters();
		assertEquals(1, marketFilters.size());

		assertEquals("HR", marketFilters.get(0).getFilterName());
		assertEquals("/7/", marketFilters.get(0).getMarketPath());
		assertEquals(false, marketFilters.get(0).isBwinMatched());
		assertEquals(null, marketFilters.get(0).getMarketName());
		
		/** Check for default.info */
		stateMachineInfo = stateMachines.get(1);
		assertEquals("default", stateMachineInfo.getStateMachineId());
		assertEquals("file:/C:/Perforce/default/flexibet/server/src/test/resources/conf/statemachine/default.scxml",
				stateMachineInfo.getStateMachineUrl().toString());

		marketFilters = stateMachineInfo.getMarketFilters();
		assertEquals(2, marketFilters.size());

		assertEquals("soccer", marketFilters.get(0).getFilterName());
		assertEquals("/1/", marketFilters.get(0).getMarketPath());
		assertEquals("Match Odds", marketFilters.get(0).getMarketName());
		assertEquals(true, marketFilters.get(0).isBwinMatched());
		assertEquals(true, marketFilters.get(0).isInPlay());

		assertEquals("horseracing_GB", marketFilters.get(1).getFilterName());
		assertEquals("/7/298251/", marketFilters.get(1).getMarketPath());
		assertEquals(null, marketFilters.get(1).getMarketName());
		assertEquals(false, marketFilters.get(1).isBwinMatched());
		assertEquals(false, marketFilters.get(1).isInPlay());

	}

}
