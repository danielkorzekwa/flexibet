package dk.flexibet.launcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**Starts Betting Application.
 * 
 * @author daniel
 *
 */
public class FlexiBetLauncher {

	private static final Log log = LogFactory.getLog(FlexiBetLauncher.class.getSimpleName());
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		long now = System.currentTimeMillis();
		log.info("Bot is starting...");
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring-bettingapp.xml"});
		
		long startingTime = (System.currentTimeMillis()-now)/1000;
		log.info("Bot is started. Starting time: " + startingTime + " sec.");
	}

}
