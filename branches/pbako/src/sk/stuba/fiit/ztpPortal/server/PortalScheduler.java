package sk.stuba.fiit.ztpPortal.server;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class PortalScheduler {
	
	
	
	public PortalScheduler()throws Exception{
		System.out.println("rozbehol som sa");
		  SchedulerFactory sf=new StdSchedulerFactory();
		  Scheduler sched=sf.getScheduler();
		  sched.start();
		  JobDetail jd=new JobDetail("myjob",Scheduler.DEFAULT_GROUP,PortalJobScheduler.class);
		  SimpleTrigger st=new SimpleTrigger("mytrigger",Scheduler.DEFAULT_GROUP,new Date(),
		  null,SimpleTrigger.REPEAT_INDEFINITELY,60L*1000L);
		  sched.scheduleJob(jd, st);
		  }

}
