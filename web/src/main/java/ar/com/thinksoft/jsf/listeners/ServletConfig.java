package ar.com.thinksoft.jsf.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import ar.com.thinksoft.jdbc.HibernateSessionFactory;

@WebListener
public class ServletConfig implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("CONTEXT DESTROYED");
		HibernateSessionFactory.getInstance().destroyServiceRegistry();
	}

}