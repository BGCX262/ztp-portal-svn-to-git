package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.AdvertType;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.JobSector;
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
			
			jobId = (Long) session.save(job);
			session.getTransaction().commit();
		}finally{
			session.close();
			sf.close();
		}
		if (jobId>=0) return true;
		else return false;
	}
	
	public boolean updateJob(Job job){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long jobId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			job.setChangeDate(date);
			
			session.update(job);
			session.getTransaction().commit();
			jobId=0;
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
			job.add((Job) session.get(Job.class,jobId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return job;
	}
	
/**
 * Vracia vsetko co je aktivne a neobmedzil to administrator. Vrati aj tie ktore deaktivoval creator inzeratov
 * @return vsetky aktivne inzeraty
 */
	
	public List<Job> getAllActiveJob(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Job> job = new ArrayList<Job>();
		Long jobId;
		try{
			
			String SQL_QUERY = "Select job.id from Job job where job.state='true'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		int i = 0;
		while (i<list.size()){
			jobId = (Long)list.get(i++);
			job.add((Job) session.get(Job.class,jobId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return job;
	}
	
	
	public boolean isJobCreator(long jobId, long login){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List list;
		try{
			String SQL_QUERY = "Select job.id from Job job where job.id='"
				+ jobId + "' and job.creator='"+ login +"'";
		Query query = session.createQuery(SQL_QUERY);
		
		list = query.list();
		
		}finally{
			session.close();
			sf.close();
		}
		
		if (list.size()!=0){
			return true;
		}
		
		return false;
	}
	
	public List<Job> getNewJob(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Job> job = new ArrayList<Job>();
		Long jobId;
		try{
			
			String SQL_QUERY = "Select job.id from Job job where job.state='true' order by job.id desc limit 3";
		Query query = session.createQuery(SQL_QUERY);
		query.setMaxResults(3);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			jobId = (Long)list.get(i++);
			job.add((Job) session.get(Job.class,jobId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return job;
	}
	
}
