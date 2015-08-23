package sk.stuba.fiit.ztpPortal.server;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sk.stuba.fiit.ztpPortal.databaseController.GlobalSettingController;
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseModel.GlobalSetting;

public class PortalJobScheduler implements Job{
	
	private static final Log log = LogFactory.getLog(PortalJobScheduler.class);
	private JobController jobController;
	private int dayCount;
	private GlobalSetting deactivationSetting;
	
	 public void execute(JobExecutionContext arg0) throws JobExecutionException{
		    
		 	jobController = new JobController();
		 	Date now = new Date();
		 	deactivationSetting = new GlobalSettingController().getSettingByName("jobDeactivation");
		 	dayCount = Integer.parseInt(deactivationSetting.getValue());
		 	
		    System.out.println("Scheduler invoked in JOB module at " + now);
		    
		    List<sk.stuba.fiit.ztpPortal.databaseModel.Job> jobList = jobController.getAllActiveJob();
		    
		    for (sk.stuba.fiit.ztpPortal.databaseModel.Job job: jobList){
		    	
		    	// mazanie ked je state = true
		    	if ((dayCount!=0) && job.isActive()){
		    		
		    		Date createDate = job.getCreationDate();
		    		
		    		if (now.getTime() - createDate.getTime() > dayCount*86400000){	//ak presiahol cas pocet dni
		    			job.setActive(false); 		//deaktivujem
		    		jobController.updateJob(job); 	//jasne
		    		log.info("Deaktivacia state JOB schedulerom " + job.getSpecification() );
		    	}
		    	}
		    	
		    	
		    	// mazanie ked je state = false, pouzivatel ju nastavil
		    	if (!job.isActive()){
		    		
		    		Date changeDate = job.getChangeDate();
		    		
		    		if (now.getTime() - changeDate.getTime() > dayCount*86400000 ){	//ak presiahol cas pocet dni
		    			job.setState(false); 		//deaktivujem
		    		jobController.updateJob(job); 	//jasne
		    		log.info("Deaktivacia active JOB schedulerom " + job.getSpecification() );
		    	}
		    	}
		    	
		    }
		    
		    
		    
}
}