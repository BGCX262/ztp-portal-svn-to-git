package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.Theme;
import sk.stuba.fiit.ztpPortal.databaseModel.Thread;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class ThreadController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean saveNewThread(Thread thread){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long threadId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			thread.setCreateDate(date);
			thread.setChangeDate(date);
			
			threadId = (Long) session.save(thread);
			session.getTransaction().commit();
		}finally{
			session.close();
			sf.close();
		}
		if (threadId>=0) return true;
		else return false;
	}
	
	public boolean updateThread(Thread thread){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long threadId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			thread.setChangeDate(date);
			
			session.update(thread);
			session.getTransaction().commit();
			threadId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (threadId>=0) return true;
		else return false;
	}
	
	public Thread getThreadByThreadId(long threadId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		Thread theme = null;
		try{
			
			String SQL_QUERY = "Select thread.id from Thread thread where thread.id='"
				+ threadId + "'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			theme = (Thread) session.get(Thread.class,threadId);
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return theme;
	}
	
	public List<Thread> getAllThread(Theme theme){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Thread> thread = new ArrayList<Thread>();
		Long threadId;
		try{
			
			String SQL_QUERY = "Select thread.id from Thread thread where thread.theme='"+theme.getId()+"' order by thread.id desc";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		int i = 0;
		while (i<list.size()){
			threadId = (Long)list.get(i++);
			thread.add((Thread) session.get(Thread.class,threadId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return thread;
	}
	
	
	public List<Thread> getAllActiveThread(Theme theme){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Thread> thread = new ArrayList<Thread>();
		Long threadId;
		try{
			
			String SQL_QUERY = "Select thread.id from Thread thread where thread.active='true' and thread.theme='"+theme.getId()+"'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		int i = 0;
		while (i<list.size()){
			threadId = (Long)list.get(i++);
			thread.add((Thread) session.get(Thread.class,threadId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return thread;
	}
	
	public int getThreadCommentCount(Thread threadId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List list;
		try{
			
			String SQL_QUERY = "Select comment.id from Comment comment where comment.state='true' and comment.thread="+threadId.getId();
		Query query = session.createQuery(SQL_QUERY);
		list = query.list();
		}finally{
			session.close();
			sf.close();
		}
		
		return list.size();
	}
	
}
