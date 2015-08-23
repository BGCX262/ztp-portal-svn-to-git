package sk.stuba.fiit.ztpPortal.server;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sk.stuba.fiit.ztpPortal.databaseController.GlobalSettingController;
import sk.stuba.fiit.ztpPortal.databaseController.LivingController;
import sk.stuba.fiit.ztpPortal.databaseModel.GlobalSetting;
import sk.stuba.fiit.ztpPortal.databaseModel.Living;

public class PortalLivingScheduler implements Job{
	
	private static final Log log = LogFactory.getLog(PortalLivingScheduler.class);
	private LivingController jobController;
	private int dayCount;
	private GlobalSetting deactivationSetting;
	
	 public void execute(JobExecutionContext arg0) throws JobExecutionException{
		    
		 	jobController = new LivingController();
		 	Date now = new Date();
		 	deactivationSetting = new GlobalSettingController().getSettingByName("jobDeactivation");
		 	dayCount = Integer.parseInt(deactivationSetting.getValue());
		 	
		    System.out.println("Scheduler invoked in LIVING module at " + now);
		    
		    List<Living> livingList = jobController.getAllLiving();
		    
		    for (Living living: livingList){
		    	
		    	// mazanie ked je state = true
		    	if ((dayCount!=0) && living.isActive()){
		    		
		    		Date createDate = living.getCreateDate();
		    		
		    		if (now.getTime() - createDate.getTime() > dayCount*86400000){	//ak presiahol cas pocet dni
		    			living.setActive(false); 		//deaktivujem
		    		jobController.updateLiving(living); 	//jasne
		    		log.info("Deaktivacia state LIVING schedulerom " + living.getId() );
		    	}
		    	}
		    	
		    	
		    	// mazanie ked je state = false, pouzivatel ju nastavil
		    	if (!living.isActive()){
		    		
		    		Date changeDate = living.getChangeDate();
		    		
		    		if (now.getTime() - changeDate.getTime() > dayCount*86400000 ){	//ak presiahol cas pocet dni
		    			living.setState(false); 		//deaktivujem
		    		jobController.updateLiving(living); 	//jasne
		    		log.info("Deaktivacia active LIVING schedulerom " + living.getId() );
		    	}
		    	}
		    	
		    }
		    
		    
		    
}
}