package dk.flexibet.server;

import javax.annotation.Resource;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import dk.flexibet.server.mbean.BettingServerMBean;

/**
 * The main server side component of a flexibet. Integrates other components such as marketObserver,
 * bettingEngine.
 * 
 * 
 * @author daniel
 * 
 */

public class BettingServerImpl implements BettingServer{
	
	private BettingServerMBean bettingServerMBean;

	public BettingServerImpl() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring-flexibet-server.xml","spring-betapi.xml","spring-flexibet-server-tasks.xml","spring-flexibet-server-dao.xml"});
		this.bettingServerMBean = (BettingServerMBean)context.getBean("bettingServerMBean");
	}
	
	public BettingServerMBean getMBean() {
		return bettingServerMBean;
	}

	
}
