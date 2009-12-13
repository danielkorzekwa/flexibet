package dk.flexibet.server.config.bettingengine;

import org.springframework.beans.factory.FactoryBean;

import dk.bot.bettingengine.statemachine.StateMachineServiceConfig;

/**Factory bean for StateMachineService config
 * 
 * @author daniel
 *
 */
public class StateMachineServiceConfigFactoryBean  implements FactoryBean{

	private final String configFile;

	public StateMachineServiceConfigFactoryBean(String configFile) {
		this.configFile = configFile;
	}
	
	public Object getObject() throws Exception {
		StateMachineServiceConfig stateMachineServiceConfig = new FSStateMachineConfigLoaderImpl(configFile).load();
		return stateMachineServiceConfig;
	}

	public Class getObjectType() {
		return StateMachineServiceConfig.class;
	}

	public boolean isSingleton() {
	return true;
	}

}
