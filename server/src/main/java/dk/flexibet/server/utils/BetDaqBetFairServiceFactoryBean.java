package dk.flexibet.server.utils;

import dk.bot.betfairservice.BetFairServiceFactoryBean;
import dk.bot.betfairservice.model.LoginResponse;

public class BetDaqBetFairServiceFactoryBean implements BetFairServiceFactoryBean{

	private BetDaqBetFairServiceImpl betDaqBetfairService;
	
	public BetDaqBetFairServiceFactoryBean() {
		betDaqBetfairService = new BetDaqBetFairServiceImpl();
	}
	
	public LoginResponse login() {
		return null;
	}

	public void setPassword(String password) {
		
	}

	public void setProductId(int productId) {
		
	}

	public void setUser(String user) {
		
	}

	public Object getObject() throws Exception {
		return betDaqBetfairService;
	}

	public Class getObjectType() {
		return BetDaqBetFairServiceImpl.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
