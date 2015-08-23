package sk.stuba.fiit.ztpPortal.server;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sk.stuba.fiit.ztpPortal.databaseController.DayCareController;
import sk.stuba.fiit.ztpPortal.databaseController.GlobalSettingController;
import sk.stuba.fiit.ztpPortal.databaseModel.DayCare;
import sk.stuba.fiit.ztpPortal.databaseModel.GlobalSetting;

public class PortalDayCareScheduler implements Job{
	
	private static final Log log = LogFactory.getLog(PortalDayCareScheduler.class);
	private DayCareController dayCareController;
	private int dayCount;
	private GlobalSetting deactivationSetting;
	
	 public void execute(JobExecutionContext arg0) throws JobExecutionException{
		    
		 	dayCareController = new DayCareController();
		 	Date now = new Date();
		 	deactivationSetting = new GlobalSettingController().getSettingByName("jobDeactivation");
		 	dayCount = Integer.parseInt(deactivationSetting.getValue());
		 	
		    System.out.println("Scheduler invoked in DAYCARE module at " + now);
		    
		    List<DayCare> dayCareList = dayCareController.getAllActiveDayCares();
		    
		    for (DayCare dayCare: dayCareList){
		    	
		    	// mazanie ked je state = true
		    	if ((dayCount!=0) && dayCare.isActive()){
		    		
		    		Date createDate = dayCare.getCreationDate();
		    		
		    		if (now.getTime() - createDate.getTime() > dayCount*86400000){	//ak presiahol cas pocet dni
		    			dayCare.setActive(false); 		//deaktivujem
		    		dayCareController.updateDayCare(dayCare); 	//jasne
		    		log.info("Deaktivacia state DAYCARE schedulerom " + dayCare.getShortDesc() );
		    	}
		    	}
		    	
		    	
		    	// mazanie ked je state = false, pouzivatel ju nastavil
		    	if (!dayCare.isActive()){
		    		
		    		Date changeDate = dayCare.getChangeDate();
		    		
		    		if (now.getTime() - changeDate.getTime() > dayCount*86400000 ){	//ak presiahol cas pocet dni
		    			dayCare.setState(false); 		//deaktivujem
		    		dayCareController.updateDayCare(dayCare); 	//jasne
		    		log.info("Deaktivacia active DAYCARE schedulerom " + dayCare.getShortDesc() );
		    	}
		    	}
		    	
		    }
}
}