package sk.stuba.fiit.ztpPortal.server;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sk.stuba.fiit.ztpPortal.databaseController.GlobalSettingController;
import sk.stuba.fiit.ztpPortal.databaseController.InformationController;
import sk.stuba.fiit.ztpPortal.databaseModel.GlobalSetting;
import sk.stuba.fiit.ztpPortal.databaseModel.Information;

public class PortalInformationScheduler implements Job{
	
	private static final Log log = LogFactory.getLog(PortalInformationScheduler.class);
	private InformationController informationController;
	private int dayCount;
	private GlobalSetting deactivationSetting;
	
	 public void execute(JobExecutionContext arg0) throws JobExecutionException{
		    
		 	informationController = new InformationController();
		 	Date now = new Date();
		 	deactivationSetting = new GlobalSettingController().getSettingByName("otherDeactivation");
		 	dayCount = Integer.parseInt(deactivationSetting.getValue());
		 	
		    System.out.println("Scheduler invoked in INFO module at " + now);
		    
		    List<Information> informationList = informationController.getAllActiveInformation();
		    
		    for (Information information: informationList){
		    	
		    	if ((dayCount!=0) && information.isActive()){
		    		
		    		Date createDate = information.getCreateDate();
		    		
		    		if (now.getTime() - createDate.getTime() > dayCount*86400000){	//ak presiahol cas pocet dni
		    			information.setActive(false); 		//deaktivujem
		    		informationController.updateInformation(information); 	//jasne
		    		log.info("Deaktivacia state INFO schedulerom " + information.getName() );
		    	}
		    	}
		    	
		    	
		    	if (!information.isActive()){
		    		
		    		Date changeDate = information.getChangeDate();
		    		
		    		if (now.getTime() - changeDate.getTime() > dayCount*86400000 ){	//ak presiahol cas pocet dni
		    			information.setState(false); 		//deaktivujem
		    		informationController.updateInformation(information); 	//jasne
		    		log.info("Deaktivacia active INFO schedulerom " + information.getName() );
		    	}
		    	}
		    	
		    }
		    
		    
		    
}
}