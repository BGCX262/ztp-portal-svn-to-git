package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class JobController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public boolean saveNewJob(Job job){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long jobId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			job.setCreationDate(date);
			job.setChangeDate(date);
			job.setStartDate(date);
			
			jobId = (Long) session.save(job);
			session.getTransaction().commit();
		}finally{
			session.close();
			sf.close();
		}
		if (jobId>=0) return true;
		else return false;
	}
	
	public Job getJobByJobId(long jobId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		Job job = null;
		try{
			
			String SQL_QUERY = "Select job.id from Job job where job.id='"
				+ jobId + "'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			job = (Job) session.get(Job.class,jobId);
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return job;
	}
	
	public List<Job> getAllJob(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Job> job = new ArrayList<Job>();
		Long jobId;
		try{
			
			String SQL_QUERY = "Select job.id from Job job";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		int i = 0;
		while (i<list.size()){
			jobId = (Long)list.get(i++);
			System.out.println(jobId);
			job.add((Job) session.get(Job.class,jobId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return job;
		
	}
	
}
