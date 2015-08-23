package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.Town;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class TownController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String[] getTownNameList(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] townList;
		Long townId;
		try{
			
			String SQL_QUERY = "Select town.name from Town town";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			townList = new String[list.size()];
			int i = 0;
			while (i<list.size()){
				//townList.add((String) session.get(Town.class,townId));
				System.out.println((String)list.get(i));
				townList[i]=((String)list.get(i++));
			}
			
		}finally{
			session.close();
			sf.close();
		}
		return townList;
	}
	
	public Town getTownByName(String name){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] townList;
		Long townId;
		Town returnTown = null;
		try{
			
			String SQL_QUERY = "Select town from Town town where town.name='"+name+"'";
			Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			returnTown = (Town)list.get(0);
		}

			
		}finally{
			session.close();
			sf.close();
		}
		return returnTown;
	}
	
}
