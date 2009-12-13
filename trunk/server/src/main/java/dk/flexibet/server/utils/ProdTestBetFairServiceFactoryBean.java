package dk.flexibet.server.utils;

import org.springframework.beans.factory.FactoryBean;

import dk.bot.betfairservice.BetFairService;
import dk.bot.betfairservice.DefaultBetFairServiceFactoryBean;

/**Returns real or test BetFairAPI service
 * 
 * @author daniel
 *
 */
public class ProdTestBetFairServiceFactoryBean implements FactoryBean{
	
	private BetFairService betfairService;
	
	public ProdTestBetFairServiceFactoryBean(String user, String password, int productId, boolean productionMode) {
				
		if(productionMode) {
			DefaultBetFairServiceFactoryBean prodFactory = new DefaultBetFairServiceFactoryBean();
			prodFactory.setUser(user);
			prodFactory.setPassword(password);
			prodFactory.setProductId(productId);
			prodFactory.login();
			try {
				betfairService = (BetFairService)prodFactory.getObject();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		else {
			betfairService = new BetDaqBetFairServiceImpl();
		}
		
	}
	
	public Object getObject() throws Exception {
		return betfairService;
	}

	public Class getObjectType() {
		return BetFairService.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
