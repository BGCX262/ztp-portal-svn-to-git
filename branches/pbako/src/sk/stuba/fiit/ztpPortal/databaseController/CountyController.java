package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import sk.stuba.fiit.ztpPortal.databaseModel.County;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class CountyController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String[] getCountyNameList(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] townList;
		Long townId;
		try{
			
			String SQL_QUERY = "Select county.name from County county";
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
	
	public County getCountyByName(String name){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] townList;
		Long townId;
		County returnTown = null;
		try{
			
			String SQL_QUERY = "Select county from County county where county.name='"+name+"'";
			Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			returnTown = (County)list.get(0);
		}

		}finally{
			session.close();
			sf.close();
		}
		return returnTown;
	}
	
	public String[] getCountyListByCountry(String country){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] townList;
		Long townId;
		String[] returnCounty;
		try{
			CountryController countryController = new CountryController();

			
			String SQL_QUERY = "Select county from County county where county.country='"+countryController.getCountryByName(country).getId()+"'";
			Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		returnCounty = new String[list.size()];
		if (list.size()!=0){
			
			for(int i=0;i<list.size();i++){
			returnCounty[i] = ((County)list.get(i)).getName();
			}
		}

		}finally{
			session.close();
			sf.close();
		}
		return returnCounty;
	}
	
	public String getCountryByCounty(String county){
		String retVal="";
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		String[] returnCounty;
		try{
			CountryController countryController = new CountryController();

			
			String SQL_QUERY = "Select county from County county where county.name='"+county+"'";
			Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		
		if (list.size()!=0){
			
			System.out.println("Country "+((County)list.get(0)).getCountry().getName()+"Vstupny string:"+county);
			retVal=((County)list.get(0)).getCountry().getName();
		}

		}finally{
			session.close();
			sf.close();
		}
		
		return retVal;
	}
}
