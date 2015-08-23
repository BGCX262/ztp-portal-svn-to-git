package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.EventType;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class EventTypeController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String[] getEventTypeNameList(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] eventTypeList;
		try{
			
			String SQL_QUERY = "Select eventType.name from EventType eventType";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			eventTypeList = new String[list.size()];
			int i = 0;
			while (i<list.size()){
				eventTypeList[i]=((String)list.get(i++));
			}
			
		}finally{
			session.close();
			sf.close();
		}
		return eventTypeList;
	}
	
	public EventType getEventTypeByName(String name){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		EventType returnEvent = null;
		try{
			String SQL_QUERY = "Select eventType from EventType eventType where eventType.name='"+name+"'";
			Query query = session.createQuery(SQL_QUERY);
			System.out.println(query);
		List list = query.list();
		if (list.size()!=0){
			returnEvent = (EventType)list.get(0);
			System.out.println(returnEvent);
		}

			
		}finally{
			session.close();
			sf.close();
		}
		return returnEvent;
	}
	
}
