package sk.stuba.fiit.ztpPortal.server;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sk.stuba.fiit.ztpPortal.databaseController.EventController;

public class PortalEventScheduler implements Job{
	
	private EventController eventController;
	private Date now;
	private int dayCount;
	
	 public void execute(JobExecutionContext arg0) throws JobExecutionException{
		    
		 	eventController = new EventController();
		 	Date now = new Date();
		 	dayCount = 1;
		 
		    System.out.println("Scheduler invoked in EVENT module at " + now);
		    
		    List<sk.stuba.fiit.ztpPortal.databaseModel.Event> eventList = eventController.getAllActiveEvent();
		    
		    for (sk.stuba.fiit.ztpPortal.databaseModel.Event event: eventList){
		    	
		    	if (!event.isActive()){	//ak si ho user zmenil
		    		
		    		Date changeDate = event.getChangeDate();
		    		
		    		if (changeDate.getTime()+dayCount*86400000 > now.getTime())	//ak presiahol cas pocet dni
		    			event.setState(false); 		//deaktivujem
		    		eventController.updateEvent(event); 	//jasne
		    		System.out.println("Deaktivacia JOB schedulerom " + event.getName() );
		    	}
		    	
		    }
		    
		    
		    
}
}