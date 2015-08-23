package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.BatchUpdateException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.Course;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class CourseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean saveNewCourse(Course course) throws BatchUpdateException{
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long courseId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			course.setCreateDate(date);
			course.setChangeDate(date);
			
			courseId = (Long) session.save(course);
			session.getTransaction().commit();
		}
		finally{
			session.close();
			sf.close();
		}
		if (courseId>=0) return true;
		else return false;
	}
	
	public boolean updateCourse(Course course){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long courseId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			course.setChangeDate(date);
			
			session.update(course);
			session.getTransaction().commit();
			courseId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (courseId>=0) return true;
		else return false;
	}
	
	public Course getCourseByCourseId(long courseId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		Course course = null;
		try{
			
			String SQL_QUERY = "Select course.id from Course course where course.id='"
				+ courseId + "'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			course = (Course) session.get(Course.class,courseId);
		}
		}finally{
			session.close();
			sf.close();
		}
		return course;
	}
	
	public List<Course> getAllCourses(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Course> courseList = new ArrayList<Course>();
		Long courseId;
		try{
			
			String SQL_QUERY = "Select course.id from Course course";
		Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			courseId = (Long)list.get(i++);
			courseList.add((Course) session.get(Course.class,courseId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return courseList;
	}
	
	public List<Course> getAllActiveCourse(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Course> courseList = new ArrayList<Course>();
		Long courseId;
		try{
			
			String SQL_QUERY = "Select course.id from Course course where course.state='true'";
		Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			courseId = (Long)list.get(i++);
			courseList.add((Course) session.get(Course.class,courseId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return courseList;
	}
	
	public boolean isCourseCreator(long courseId, long login){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List list;
		try{
			String SQL_QUERY = "Select course.id from Course course where course.id='"
				+ courseId + "' and course.owner='"+ login +"'";
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
	
	public static List<Course> getNewCourse(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Course> courseList = new ArrayList<Course>();
		Long courseId;
		try{
			
			String SQL_QUERY = "Select course.id from Course course where course.state='true' order by course.id desc limit 3";
		Query query = session.createQuery(SQL_QUERY).setCacheable(true);
		query.setMaxResults(2);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			courseId = (Long)list.get(i++);
			courseList.add((Course) session.get(Course.class,courseId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return courseList;
	}
	
	public boolean deleteAllCourse(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long result = -1;
		try{
			session.beginTransaction();
			String SQL_QUERY = "Delete from Course course where course.active='false'";
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
