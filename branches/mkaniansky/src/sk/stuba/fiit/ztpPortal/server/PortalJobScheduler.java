package sk.stuba.fiit.ztpPortal.server;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sk.stuba.fiit.ztpPortal.databaseController.JobController;

public class PortalJobScheduler implements Job{
	
	private JobController jobController;
	private Date now;
	private int dayCount;
	
	 public void execute(JobExecutionContext arg0) throws JobExecutionException{
		    
		 	jobController = new JobController();
		 	Date now = new Date();
		 	dayCount = 1;
		 
		    System.out.println("Scheduler invoked in JOB module at " + now);
		    
		    List<sk.stuba.fiit.ztpPortal.databaseModel.Job> jobList = jobController.getAllActiveJob();
		    
		    for (sk.stuba.fiit.ztpPortal.databaseModel.Job job: jobList){
		    	
		    	if (!job.isActive()){	//ak si ho user zmenil
		    		
		    		Date changeDate = job.getChangeDate();
		    		
		    		if (changeDate.getTime()+dayCount*86400000 > now.getTime())	//ak presiahol cas pocet dni
		    			job.setState(false); 		//deaktivujem
		    		jobController.updateJob(job); 	//jasne
		    		System.out.println("Deaktivacia JOB schedulerom " + job.getSpecification() );
		    	}
		    	
		    }
		    
		    
		    
}
}