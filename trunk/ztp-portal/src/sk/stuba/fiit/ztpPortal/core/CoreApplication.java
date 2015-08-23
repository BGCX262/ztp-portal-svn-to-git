package sk.stuba.fiit.ztpPortal.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.settings.IApplicationSettings;

import sk.stuba.fiit.ztpPortal.server.PortalScheduler;


/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see wicket.quickstart.Start#main(String[])
 */
public class CoreApplication extends WebApplication {
	/** Logging */
	private static final Log log = LogFactory.getLog(CoreApplication.class);

	/**
	 * Constructor
	 */
	public CoreApplication() {
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();

		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        
        IApplicationSettings settings = getApplicationSettings();
        settings.setAccessDeniedPage(PageExpiration.class);
        settings.setPageExpiredErrorPage(PageExpiration.class);
        settings.setInternalErrorPage(PageExpiration.class);
        
        try {
			PortalScheduler sched= new PortalScheduler();
			log.info("Scheduler spusteny");
		} catch (Exception e) {
			log.warn("Nespustil sa scheduler!");
		}
		
	}

	@Override
	public Class getHomePage() {
		//return StyleChooser.class;
		return MainPage.class;
	}
	
	@Override
	public Session newSession(Request request, Response response) {
	   return new CoreSession(request);
	}
}