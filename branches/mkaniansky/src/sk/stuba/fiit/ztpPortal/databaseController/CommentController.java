package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.Comment;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.Thread;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class CommentController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean saveNewComment(Comment comment){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long threadId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			comment.setCreateDate(date);
			comment.setChangeDate(date);
			
			System.out.println(date.getTime());
			System.out.println(new java.util.Date().getTime());
			
			threadId = (Long) session.save(comment);
			session.getTransaction().commit();
		}finally{
			session.close();
			sf.close();
		}
		if (threadId>=0) return true;
		else return false;
	}
	
	public boolean updateComment(Comment comment){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long commentId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			comment.setChangeDate(date);
			
			session.update(comment);
			session.getTransaction().commit();
			commentId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (commentId>=0) return true;
		else return false;
	}
	
	public boolean noticeUp(Comment comment){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long commentId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			comment.setChangeDate(date);
			
			comment.setNotice(comment.getNotice()+1);
			
			session.update(comment);
			session.getTransaction().commit();
			commentId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (commentId>=0) return true;
		else return false;
	}
	
	
	
	public Comment getCommentByCommentId(long commentId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		Comment comment = null;
		try{
			
			String SQL_QUERY = "Select comment.id from Comment comment where comment.id='"
				+ commentId + "'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			comment = (Comment) session.get(Comment.class,commentId);
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return comment;
	}
	
	
	public List<Comment> getAllComment(Thread thread){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Comment> comment = new ArrayList<Comment>();
		Long commentId;
		try{
			
			String SQL_QUERY = "Select comment.id from Comment comment where comment.thread='"+thread.getId()+"' order by comment.id desc";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		int i = 0;
		while (i<list.size()){
			commentId = (Long)list.get(i++);
			comment.add((Comment) session.get(Comment.class,commentId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return comment;
	}
	
	
	public List<Comment> getActiveComment(Thread thread){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Comment> comment = new ArrayList<Comment>();
		Long commentId;
		try{
			
			String SQL_QUERY = "Select comment.id from Comment comment where comment.state='true' and comment.thread='"+thread.getId()+"' order by comment.id desc";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		int i = 0;
		while (i<list.size()){
			commentId = (Long)list.get(i++);
			comment.add((Comment) session.get(Comment.class,commentId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return comment;
	}
	
	public List<Comment> getNewComment(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Comment> comment = new ArrayList<Comment>();
		Long commentId;
		try{
			
			String SQL_QUERY = "Select comment.id from Comment comment where comment.state='true' order by comment.id desc limit 3";
		Query query = session.createQuery(SQL_QUERY);
		query.setMaxResults(3);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			commentId = (Long)list.get(i++);
			comment.add((Comment) session.get(Comment.class,commentId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return comment;
	}
	
}
