package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.BatchUpdateException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.HealthAid;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class HealthAidController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean saveNewHealthAid(HealthAid information) throws BatchUpdateException{
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long healthAidId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			information.setCreateDate(date);
			information.setChangeDate(date);
			
			healthAidId = (Long) session.save(information);
			session.getTransaction().commit();
		}
		finally{
			session.close();
			sf.close();
		}
		if (healthAidId>=0) return true;
		else return false;
	}
	
	public boolean updateHealthAid(HealthAid information){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long healthAidId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			information.setChangeDate(date);
			
			session.update(information);
			session.getTransaction().commit();
			healthAidId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (healthAidId>=0) return true;
		else return false;
	}
	
	public HealthAid getHealthAidByHealthAidId(long informationId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		HealthAid healthAid = null;
		try{
			
			String SQL_QUERY = "Select HealthAid.id from HealthAid HealthAid where HealthAid.id='"
				+ informationId + "'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			healthAid = (HealthAid) session.get(HealthAid.class,informationId);
		}
		}finally{
			session.close();
			sf.close();
		}
		return healthAid;
	}
	
	public List<HealthAid> getAllHealthAid(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<HealthAid> healthAidList = new ArrayList<HealthAid>();
		Long informationId;
		try{
			
			String SQL_QUERY = "Select HealthAid.id from HealthAid HealthAid";
		Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			informationId = (Long)list.get(i++);
			healthAidList.add((HealthAid) session.get(HealthAid.class,informationId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return healthAidList;
	}
	
	public List<HealthAid> getAllActiveHealthAid(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<HealthAid> healthAidList = new ArrayList<HealthAid>();
		Long informationId;
		try{
			
			String SQL_QUERY = "Select HealthAid.id from HealthAid HealthAid where HealthAid.state='true'";
		Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			informationId = (Long)list.get(i++);
			healthAidList.add((HealthAid) session.get(HealthAid.class,informationId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return healthAidList;
	}
	
	public boolean isHealthAidCreator(long informationId, long login){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List list;
		try{
			String SQL_QUERY = "Select HealthAid.id from HealthAid HealthAid where HealthAid.id='"
				+ informationId + "' and HealthAid.owner='"+ login +"'";
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
	
	public static List<HealthAid> getNewHealthAid(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<HealthAid> healthAidList = new ArrayList<HealthAid>();
		Long HealthAidId;
		try{
			
			String SQL_QUERY = "Select healthAid.id from HealthAid healthAid where healthAid.state='true' order by healthAid.id desc limit 3";
		Query query = session.createQuery(SQL_QUERY).setCacheable(true);
		query.setMaxResults(2);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			HealthAidId = (Long)list.get(i++);
			healthAidList.add((HealthAid) session.get(HealthAid.class,HealthAidId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return healthAidList;
	}
	
	public boolean deleteAllDeactivatedHealthAid(){
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
