package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.BatchUpdateException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class EventController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean saveNewEvent(Event event) throws BatchUpdateException{
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long eventId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			event.setCreateDate(date);
			event.setChangeDate(date);
			
			eventId = (Long) session.save(event);
			session.getTransaction().commit();
		}
		finally{
			session.close();
			sf.close();
		}
		if (eventId>=0) return true;
		else return false;
	}
	
	public boolean updateEvent(Event event){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long eventId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			event.setChangeDate(date);
			
			session.update(event);
			session.getTransaction().commit();
			eventId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (eventId>=0) return true;
		else return false;
	}
	
	public Event getEventByEventId(long eventId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		Event event = null;
		try{
			
			String SQL_QUERY = "Select event.id from Event event where event.id='"
				+ eventId + "'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			event = (Event) session.get(Event.class,eventId);
		}
		}finally{
			session.close();
			sf.close();
		}
		return event;
	}
	
	public List<Event> getAllEvent(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Event> eventList = new ArrayList<Event>();
		Long eventId;
		try{
			
			String SQL_QUERY = "Select event.id from Event event";
		Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			eventId = (Long)list.get(i++);
			eventList.add((Event) session.get(Event.class,eventId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return eventList;
	}
	
	public List<Event> getAllActiveEvent(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Event> eventList = new ArrayList<Event>();
		Long eventId;
		try{
			
			String SQL_QUERY = "Select event.id from Event event where event.state='true'";
		Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			eventId = (Long)list.get(i++);
			eventList.add((Event) session.get(Event.class,eventId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return eventList;
	}
	
	public boolean isEventCreator(long eventId, long login){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List list;
		try{
			String SQL_QUERY = "Select event.id from Event event where event.id='"
				+ eventId + "' and event.owner='"+ login +"'";
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
	
	public static List<Event> getNewEvent(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Event> eventList = new ArrayList<Event>();
		Long eventId;
		try{
			
			String SQL_QUERY = "Select event.id from Event event where event.state='true' order by event.id desc limit 3";
		Query query = session.createQuery(SQL_QUERY).setCacheable(true);
		query.setMaxResults(2);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			eventId = (Long)list.get(i++);
			eventList.add((Event) session.get(Event.class,eventId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return eventList;
	}

	public boolean deleteAllDeactivatedEvent(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long result = -1;
		try{
			session.beginTransaction();
			String SQL_QUERY = "Delete from Event event where event.active='false'";
			Query query = session.createQuery(SQL_QUERY);
			result = query.executeUpdate();
			session.getTransaction().commit();
			result=0;
		}finally{
			session.close();
			sf.close();
		}
		if (result>=0) return true;
		else return false;
	}
}
