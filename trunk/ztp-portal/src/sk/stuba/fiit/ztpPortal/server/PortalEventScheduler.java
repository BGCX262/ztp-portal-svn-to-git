package sk.stuba.fiit.ztpPortal.server;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sk.stuba.fiit.ztpPortal.databaseController.EventController;
import sk.stuba.fiit.ztpPortal.databaseController.GlobalSettingController;
import sk.stuba.fiit.ztpPortal.databaseModel.GlobalSetting;

public class PortalEventScheduler implements Job{
	
	private static final Log log = LogFactory.getLog(PortalEventScheduler.class);
	private EventController eventController;
	private int dayCount;
	private GlobalSetting deactivationSetting;
	
	 public void execute(JobExecutionContext arg0) throws JobExecutionException{
		    
		 	eventController = new EventController();
		 	Date now = new Date();
		 	deactivationSetting = new GlobalSettingController().getSettingByName("otherDeactivation");
		 	dayCount = Integer.parseInt(deactivationSetting.getValue());
		 
		    System.out.println("Scheduler invoked in EVENT module at " + now);
		    
		    List<sk.stuba.fiit.ztpPortal.databaseModel.Event> eventList = eventController.getAllActiveEvent();
		    
		    for (sk.stuba.fiit.ztpPortal.databaseModel.Event event: eventList){
		    	
		    	if ((dayCount!=0) && !event.isActive()){	//ak si ho user zmenil
		    		
		    		Date createDate = event.getCreateDate();
		    		
		    		if (now.getTime() - createDate.getTime() > dayCount*86400000)	//ak presiahol cas pocet dni
		    			event.setActive(false); 		//deaktivujem
		    		eventController.updateEvent(event); 	//jasne
		    		log.info("Deaktivacia EVENT schedulerom " + event.getName() );
		    	}
		    	
		    		if (!event.isActive()){
		    		
		    		Date changeDate = event.getChangeDate();
		    		
		    		if (now.getTime() - changeDate.getTime() > dayCount*86400000 ){	//ak presiahol cas pocet dni
		    			event.setState(false); 		//deaktivujem
		    			eventController.updateEvent(event); 	//jasne
		    		log.info("Deaktivacia active EVENT schedulerom " + event.getId() );
		    	}
		    	}
		    	
		    }
		    
		    
		    
}
}