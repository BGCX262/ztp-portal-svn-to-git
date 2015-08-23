package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.BatchUpdateException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.Living;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class LivingController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean saveNewLiving(Living living) throws BatchUpdateException{
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long livingId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			living.setCreateDate(date);
			living.setChangeDate(date);
			
			livingId = (Long) session.save(living);
			session.getTransaction().commit();
		}
		finally{
			session.close();
			sf.close();
		}
		if (livingId>=0) return true;
		else return false;
	}
	
	public boolean updateLiving(Living living){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long livingId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			living.setChangeDate(date);
			
			session.update(living);
			session.getTransaction().commit();
			livingId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (livingId>=0) return true;
		else return false;
	}
	
	public Living getLivingByLivingId(long livingId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		Living Living = null;
		try{
			
			String SQL_QUERY = "Select living.id from Living living where living.id='"
				+ livingId + "'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			Living = (Living) session.get(Living.class,livingId);
		}
		}finally{
			session.close();
			sf.close();
		}
		return Living;
	}
	
	public List<Living> getAllLiving(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Living> eventList = new ArrayList<Living>();
		Long livingId;
		try{
			
			String SQL_QUERY = "Select living.id from Living living";
		Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			livingId = (Long)list.get(i++);
			eventList.add((Living) session.get(Living.class,livingId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return eventList;
	}
	
	public List<Living> getAllActiveLiving(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Living> eventList = new ArrayList<Living>();
		Long livingId;
		try{
			
			String SQL_QUERY = "Select living.id from Living living where living.state='true'";
		Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			livingId = (Long)list.get(i++);
			eventList.add((Living) session.get(Living.class,livingId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return eventList;
	}
	
	public boolean isLivingCreator(long livingId, long login){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List list;
		try{
			String SQL_QUERY = "Select living.id from Living living where living.id='"
				+ livingId + "' and living.owner='"+ login +"'";
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
	
	public static List<Living> getNewLiving(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Living> livingList = new ArrayList<Living>();
		Long livingId;
		try{
			
			String SQL_QUERY = "Select living.id from Living living where living.state='true' order by living.id desc limit 3";
		Query query = session.createQuery(SQL_QUERY).setCacheable(true);
		query.setMaxResults(2);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			livingId = (Long)list.get(i++);
			livingList.add((Living) session.get(Living.class,livingId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return livingList;
	}
	
	public boolean deleteAllDeactivatedLiving(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long result = -1;
		try{
			session.beginTransaction();
			String SQL_QUERY = "Delete from HealthAid healthAid where healthAid.active='false'";
			Query query = session.createQuery(SQL_QUERY);
			result = query.executeUpdate();
			session.getTransaction().commit();
		}finally{
			session.close();
			sf.close();
		}
		if (result>=0) return true;
		else return false;
	}
	
}
