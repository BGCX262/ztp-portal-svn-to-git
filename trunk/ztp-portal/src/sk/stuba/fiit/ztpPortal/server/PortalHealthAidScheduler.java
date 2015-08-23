package sk.stuba.fiit.ztpPortal.server;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sk.stuba.fiit.ztpPortal.databaseController.GlobalSettingController;
import sk.stuba.fiit.ztpPortal.databaseController.HealthAidController;
import sk.stuba.fiit.ztpPortal.databaseModel.GlobalSetting;
import sk.stuba.fiit.ztpPortal.databaseModel.HealthAid;

public class PortalHealthAidScheduler implements Job{
	
	private static final Log log = LogFactory.getLog(PortalHealthAidScheduler.class);
	private HealthAidController informationController;
	private int dayCount;
	private GlobalSetting deactivationSetting;
	
	 public void execute(JobExecutionContext arg0) throws JobExecutionException{
		    
		 	informationController = new HealthAidController();
		 	Date now = new Date();
		 	deactivationSetting = new GlobalSettingController().getSettingByName("otherDeactivation");
		 	dayCount = Integer.parseInt(deactivationSetting.getValue());
		 	
		    System.out.println("Scheduler invoked in HEALTHAID module at " + now);
		    
		    List<HealthAid> healthAidList = informationController.getAllActiveHealthAid();
		    
		    for (HealthAid healthAid: healthAidList){
		    	
		    	// mazanie ked je state = true
		    	if ((dayCount!=0) && healthAid.isActive()){
		    		
		    		Date createDate = healthAid.getCreateDate();
		    		
		    		if (now.getTime() - createDate.getTime() > dayCount*86400000){	//ak presiahol cas pocet dni
		    			healthAid.setActive(false); 		//deaktivujem
		    		informationController.updateHealthAid(healthAid); 	//jasne
		    		log.info("Deaktivacia state HEALTHAID schedulerom " + healthAid.getName() );
		    	}
		    	}
		    	
		    	
		    	// mazanie ked je state = false, pouzivatel ju nastavil
		    	if (!healthAid.isActive()){
		    		
		    		Date changeDate = healthAid.getChangeDate();
		    		
		    		if (now.getTime() - changeDate.getTime() > dayCount*86400000 ){	//ak presiahol cas pocet dni
		    			healthAid.setState(false); 		//deaktivujem
		    		informationController.updateHealthAid(healthAid); 	//jasne
		    		log.info("Deaktivacia active HEALTHAID schedulerom " + healthAid.getName() );
		    	}
		    	}
		    	
		    }
		    
		    
		    
}
}