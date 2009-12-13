package dk.flexibet.server.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;

import dk.bot.bettingengine.statemachine.StateMachineInfo;
import dk.bot.bettingengine.statemachine.StateMachineServiceConfig;
import dk.bot.marketobserver.marketservice.MarketServiceConfig;
import dk.bot.marketobserver.model.MarketFilter;

/** Creates market observer config object.
 * 
 * @author daniel
 *
 */
public class MarketObserverConfigFactoryBean implements FactoryBean{

	private final StateMachineServiceConfig stateMachineServiceConfig;

	/**Global markets filter*/
	private int startInHoursFrom;
	private int startInHoursTo;
	private int exchangeId;
	private String marketStatus;
	private String marketType;
	
	public MarketObserverConfigFactoryBean(StateMachineServiceConfig stateMachineServiceConfig) {
		this.stateMachineServiceConfig = stateMachineServiceConfig;
	}
	
	public Object getObject() throws Exception {
		MarketServiceConfig marketServiceConfig = createMarketServiceConfig(stateMachineServiceConfig);
		return marketServiceConfig;
	}

	public Class getObjectType() {
		return MarketServiceConfig.class;
	}

	public boolean isSingleton() {
		return true;
	}
	
	private MarketServiceConfig createMarketServiceConfig(StateMachineServiceConfig stateMachineServiceConfig) {
		MarketServiceConfig marketServiceConfig = new MarketServiceConfig();
		
		List<MarketFilter> marketFilters = new ArrayList<MarketFilter>();
		for(StateMachineInfo stateMachineInfo: stateMachineServiceConfig.getStateMachines()) {
			for(MarketFilter marketFilter: stateMachineInfo.getMarketFilters()) {
				marketFilters.add(marketFilter);
			}			
		}
		marketServiceConfig.setMarketFilters(marketFilters);
		
		marketServiceConfig.setStartInHoursFrom(startInHoursFrom);
		marketServiceConfig.setStartInHoursTo(startInHoursTo);
		marketServiceConfig.setExchangeId(exchangeId);
		marketServiceConfig.setMarketStatus(marketStatus);
		marketServiceConfig.setMarketType(marketType);
		
		return marketServiceConfig;
	}

	public int getStartInHoursFrom() {
		return startInHoursFrom;
	}

	public void setStartInHoursFrom(int startInHoursFrom) {
		this.startInHoursFrom = startInHoursFrom;
	}

	public int getStartInHoursTo() {
		return startInHoursTo;
	}

	public void setStartInHoursTo(int startInHoursTo) {
		this.startInHoursTo = startInHoursTo;
	}

	public int getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(int exchangeId) {
		this.exchangeId = exchangeId;
	}

	public String getMarketStatus() {
		return marketStatus;
	}

	public void setMarketStatus(String marketStatus) {
		this.marketStatus = marketStatus;
	}

	public String getMarketType() {
		return marketType;
	}

	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}

	public StateMachineServiceConfig getStateMachineServiceConfig() {
		return stateMachineServiceConfig;
	}

}
