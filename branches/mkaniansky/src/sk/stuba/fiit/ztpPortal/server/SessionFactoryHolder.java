package sk.stuba.fiit.ztpPortal.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
