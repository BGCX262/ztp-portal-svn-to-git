package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.BatchUpdateException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.Information;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class InformationController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean saveNewInformation(Information information) throws BatchUpdateException{
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long InformationId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			information.setCreateDate(date);
			information.setChangeDate(date);
			
			InformationId = (Long) session.save(information);
			session.getTransaction().commit();
		}
		finally{
			session.close();
			sf.close();
		}
		if (InformationId>=0) return true;
		else return false;
	}
	
	public boolean updateInformation(Information information){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long InformationId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			information.setChangeDate(date);
			
			session.update(information);
			session.getTransaction().commit();
			InformationId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (InformationId>=0) return true;
		else return false;
	}
	
	public Information getInformationByInformationId(long informationId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		Information information = null;
		try{
			
			String SQL_QUERY = "Select Information.id from Information Information where Information.id='"
				+ informationId + "'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			information = (Information) session.get(Information.class,informationId);
		}
		}finally{
			session.close();
			sf.close();
		}
		return information;
	}
	
	public List<Information> getAllInformation(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Information> informationList = new ArrayList<Information>();
		Long informationId;
		try{
			
			String SQL_QUERY = "Select Information.id from Information Information";
		Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			informationId = (Long)list.get(i++);
			informationList.add((Information) session.get(Information.class,informationId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return informationList;
	}
	
	public List<Information> getAllActiveInformation(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Information> informationList = new ArrayList<Information>();
		Long informationId;
		try{
			
			String SQL_QUERY = "Select Information.id from Information Information where Information.state='true'";
		Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			informationId = (Long)list.get(i++);
			informationList.add((Information) session.get(Information.class,informationId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return informationList;
	}
	
	public boolean isInformationCreator(long informationId, long login){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List list;
		try{
			String SQL_QUERY = "Select Information.id from Information Information where Information.id='"
				+ informationId + "' and Information.owner='"+ login +"'";
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
	
	public static List<Information> getNewInformation(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Information> informationList = new ArrayList<Information>();
		Long InformationId;
		try{
			
			String SQL_QUERY = "Select information.id from Information information where information.state='true' order by information.id desc limit 3";
		Query query = session.createQuery(SQL_QUERY).setCacheable(true);
		query.setMaxResults(2);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			InformationId = (Long)list.get(i++);
			informationList.add((Information) session.get(Information.class,InformationId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return informationList;
	}
	
	public boolean deleteInformation(Information information){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long informationId = -1;
		try{
			session.beginTransaction();
			session.delete(information);
			session.getTransaction().commit();
			informationId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (informationId>=0) return true;
		else return false;
	}
	
	public boolean deleteAllDeacivatedInformation(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long result = -1;
		try{
			session.beginTransaction();
			String SQL_QUERY = "Delete from Information information where information.active='false'";
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
