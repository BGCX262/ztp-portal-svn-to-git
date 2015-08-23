package sk.stuba.fiit.ztpPortal.server;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Trieda pre vytvorenie session pri spojeni do databazy
 * @author Peter Bradac
 *
 */
public class SessionFactoryHolder {

	private static SessionFactory sessionFactory = null;
	
	public static SessionFactory getSF(){
//		LOG.info("Getting SF for key: " + key);
		
			//Properties props = new Properties();
			//props.loadFromXML(new FileInputStream(new File("connection.xml")));
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			
			sessionFactory = configuration.buildSessionFactory();
				
		return sessionFactory;
	}
	
	public static void close() {
		if (sessionFactory != null)
			sessionFactory.close();
		sessionFactory = null;
	}
	
}
