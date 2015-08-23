package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.BatchUpdateException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.UserEvent;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class UserEventController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean saveNewUserEvent(UserEvent userEvent) throws BatchUpdateException{
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long userEventId = -1;
		try{
			session.beginTransaction();
			
			userEventId = (Long) session.save(userEvent);
			session.getTransaction().commit();
		}
		finally{
			session.close();
			sf.close();
		}
		if (userEventId>=0) return true;
		else return false;
	}
	
	public boolean deleteUserEvent(UserEvent userEvent){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long eventId = -1;
		try{
			session.beginTransaction();
			
			session.delete(userEvent);
			session.getTransaction().commit();
			eventId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (eventId>=0) return true;
		else return false;
	}
	
	public UserEvent getUserEventByEventId(long eventId,long userId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		UserEvent userEvent = null;
		try{
			
			String SQL_QUERY = "Select userEvent.id from UserEvent userEvent where userEvent.event='"
				+ eventId + "' and userEvent.user='"+userId+"'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			userEvent = (UserEvent) session.get(UserEvent.class,eventId);
		}
		}finally{
			session.close();
			sf.close();
		}
		return userEvent;
	}
	
	public boolean isUserForEventRegistered(long eventId, long userId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long count = -1;
		try{
			count = (Long) session.createQuery("select count(*) from UserEvent userEvent where userEvent.event='"+eventId+"' and userEvent.user='"+userId+"'").uniqueResult();
		}finally{
			session.close();
			sf.close();
		}
		if (count>0) return true;
		else return false;
	}
	
	
	public long getRegisteredCountForEvent(long eventId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long count = 0;
		try{
			count = (Long) session.createQuery("select count(*) from UserEvent userEvent where userEvent.event='"+eventId+"'").uniqueResult();

//		Query query = session.createQuery(SQL_QUERY);
//		List list = query.list();
//		int i = 0;
//		while (i<list.size()){
//			eventId = (Long)list.get(i++);
//			eventList.add((Event) session.get(Event.class,eventId));
//		}
		}finally{
			session.close();
			sf.close();
		}
		
		return count;
	}
	
}
