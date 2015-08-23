package sk.stuba.fiit.ztpPortal.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Scheduler pre skripty na mazanie obsahov z modulov
 * @author Peter Bradac
 *
 */
public class PortalScheduler {
	private static final Log log = LogFactory.getLog(PortalScheduler.class);
	CronTrigger cronTrigger;
	
	public PortalScheduler()throws Exception{
		
		log.info("Spustenie Schedulera portálu ");
		
		  SchedulerFactory sf=new StdSchedulerFactory();
		  Scheduler sched=sf.getScheduler();
		  sched.start();
		  
		  System.out.println("Rozbehol sa scheduler");
		  
		  JobDetail job=new JobDetail("jobJob","group1",PortalJobScheduler.class);
		
		cronTrigger = new CronTrigger("cron1","group1","0 1 1 * * ?");
		 sched.scheduleJob(job, cronTrigger);
		 
		 JobDetail living=new JobDetail("jobLiving","group2",PortalLivingScheduler.class);
		 
		 cronTrigger = new CronTrigger("cron2","group1","0 3 1 * * ?");
		 sched.scheduleJob(living, cronTrigger);
		 
		 JobDetail dayCare=new JobDetail("jobDayCare","group3",PortalDayCareScheduler.class);
		 
		 cronTrigger = new CronTrigger("cron3","group3","0 5 1 * * ?");
		 sched.scheduleJob(dayCare, cronTrigger);
		 
		 JobDetail course=new JobDetail("jobCourse","group4",PortalCourseScheduler.class);
		 
		 cronTrigger = new CronTrigger("cron4","group4","0 7 1 * * ?");
		 sched.scheduleJob(course, cronTrigger);
		 
		 JobDetail information=new JobDetail("jobInformation","group5",PortalInformationScheduler.class);
		 
		 cronTrigger = new CronTrigger("cron5","group5","0 9 1 * * ?");
		 sched.scheduleJob(information, cronTrigger);
		 
		 JobDetail healthAid=new JobDetail("jobHealthAid","group6",PortalHealthAidScheduler.class);
		 
		 cronTrigger = new CronTrigger("cron6","group6","0 11 1 * * ?");
		 sched.scheduleJob(healthAid, cronTrigger);
		 
		 JobDetail school=new JobDetail("jobSchool","group7",PortalSchoolScheduler.class);
		 
		 cronTrigger = new CronTrigger("cron7","group7","0 13 1 * * ?");
		 sched.scheduleJob(school, cronTrigger);
		 
		 JobDetail event=new JobDetail("jobEvent","group8",PortalEventScheduler.class);
		 
		 cronTrigger = new CronTrigger("cron8","group8","0 15 1 * * ?");
		 sched.scheduleJob(event, cronTrigger);
		  }

}
