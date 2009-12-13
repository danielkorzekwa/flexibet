package dk.flexibet.launcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Prints info to the screen about app beans creation.
 * 
 * @author daniel
 * 
 */
public class AppLoadingMonitor implements BeanPostProcessor {

	private final Log log = LogFactory.getLog(AppLoadingMonitor.class.getSimpleName());

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (!(bean instanceof FactoryBean)) {	
			log.info(beanName + " - OK.");
		}
		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	

}
