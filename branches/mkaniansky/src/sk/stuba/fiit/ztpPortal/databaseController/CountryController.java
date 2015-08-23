package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.Country;
import sk.stuba.fiit.ztpPortal.databaseModel.County;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class CountryController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String[] getCountryNameList(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] townList;
		Long townId;
		try{
			
			String SQL_QUERY = "Select country.name from Country country";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			townList = new String[list.size()];
			int i = 0;
			while (i<list.size()){
				townList[i]=((String)list.get(i++));
			}
			
		}finally{
			session.close();
			sf.close();
		}
		return townList;
	}
	
	public Country getCountryByName(String name){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] townList;
		Long townId;
		Country returnTown = null;
		try{
			
			String SQL_QUERY = "Select country from Country country where country.name='"+name+"'";
			Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			returnTown = (Country)list.get(0);
		}

		}finally{
			session.close();
			sf.close();
		}
		return returnTown;
	}
	
}
