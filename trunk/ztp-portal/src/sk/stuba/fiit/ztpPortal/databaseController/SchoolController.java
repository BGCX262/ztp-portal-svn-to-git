package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.BatchUpdateException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.School;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class SchoolController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean saveNewSchool(School school) throws BatchUpdateException{
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long schoolId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			school.setCreateDate(date);
			school.setChangeDate(date);
			
			schoolId = (Long) session.save(school);
			session.getTransaction().commit();
		}
		finally{
			session.close();
			sf.close();
		}
		if (schoolId>=0) return true;
		else return false;
	}
	
	public boolean updateSchool(School school){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long schoolId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			school.setChangeDate(date);
			
			session.update(school);
			session.getTransaction().commit();
			schoolId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (schoolId>=0) return true;
		else return false;
	}
	
	public School getSchoolBySchoolId(long schoolId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		School school = null;
		try{
			
			String SQL_QUERY = "Select school.id from School school where school.id='"
				+ schoolId + "'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			school = (School) session.get(School.class,schoolId);
		}
		}finally{
			session.close();
			sf.close();
		}
		return school;
	}
	
	public List<School> getAllSchools(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<School> eventList = new ArrayList<School>();
		Long schoolId;
		try{
			
			String SQL_QUERY = "Select school.id from School school";
		Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			schoolId = (Long)list.get(i++);
			eventList.add((School) session.get(School.class,schoolId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return eventList;
	}
	
	public List<School> getAllActiveSchool(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<School> eventList = new ArrayList<School>();
		Long schoolId;
		try{
			
			String SQL_QUERY = "Select school.id from School school where school.state='true'";
		Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			schoolId = (Long)list.get(i++);
			eventList.add((School) session.get(School.class,schoolId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return eventList;
	}
	
	public boolean isSchoolCreator(long schoolId, long login){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List list;
		try{
			String SQL_QUERY = "Select school.id from School school where school.id='"
				+ schoolId + "' and school.owner='"+ login +"'";
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
	
	public List<School> getNewSchool(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<School> schoolList = new ArrayList<School>();
		Long schoolId;
		try{
			
			String SQL_QUERY = "Select school from School school where school.state='true' order by event.id desc limit 3";
		Query query = session.createQuery(SQL_QUERY);
		query.setMaxResults(2);
		schoolList = query.list();
//		int i = 0;
//		while (i<list.size()){
//			schoolId = (Long)list.get(i++);
//			schoolList.add((School) session.get(School.class,schoolId));
//		}
		}finally{
			session.close();
			sf.close();
		}
		
		return schoolList;
	}
	
	public boolean deleteAllDeactivatedSchool(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long result = -1;
		try{
			session.beginTransaction();
			String SQL_QUERY = "Delete from School school where school.active='false'";
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
