package dk.flexibet.server.utils;

import dk.bot.betfairservice.BetFairServiceFactoryBean;
import dk.bot.betfairservice.model.LoginResponse;

public class TestBetFairServiceFactoryBean implements BetFairServiceFactoryBean{

	private TestBetFairServiceImpl testBetfairService;
	
	public TestBetFairServiceFactoryBean() {
		testBetfairService = new TestBetFairServiceImpl();
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
		return testBetfairService;
	}

	public Class getObjectType() {
		return TestBetFairServiceImpl.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
