package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.StuffType;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class StuffTypeController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String[] getStuffTypeNameList(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] StuffTypeList;
		try{
			
			String SQL_QUERY = "Select stuffType.name from StuffType stuffType";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			StuffTypeList = new String[list.size()];
			int i = 0;
			while (i<list.size()){
				StuffTypeList[i]=((String)list.get(i++));
			}
			
		}finally{
			session.close();
			sf.close();
		}
		return StuffTypeList;
	}
	
	public StuffType getStuffTypeByName(String name){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		StuffType returnStuffType = null;
		try{
			String SQL_QUERY = "Select stuffType from StuffType stuffType where stuffType.name='"+name+"'";
			Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		if (list.size()!=0){
			returnStuffType = (StuffType)list.get(0);
		}

			
		}finally{
			session.close();
			sf.close();
		}
		return returnStuffType;
	}
	
}
