package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.HandicapType;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class HandicapTypeController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String[] getHandicapNameList(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] handicapTypeList;
		Long townId;
		try{
			
			String SQL_QUERY = "Select handicap.name from HandicapType handicap";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			handicapTypeList = new String[list.size()];
			int i = 0;
			while (i<list.size()){
				handicapTypeList[i]=((String)list.get(i++));
			}
			
		}finally{
			session.close();
			sf.close();
		}
		return handicapTypeList;
	}
	
	public HandicapType getHandicapByName(String name){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] townList;
		Long townId;
		HandicapType returnType = null;
		try{
			
			String SQL_QUERY = "Select handicap from HandicapType handicap where handicap.name='"+name+"'";
			Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			returnType = (HandicapType)list.get(0);
		}

		}finally{
			session.close();
			sf.close();
		}
		return returnType;
	}
	
}
