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
		try{
			
			String SQL_QUERY = "Select county.name from County county";
			
			List list = session.createQuery(SQL_QUERY).setCacheable(true).setCacheRegion("countyRegion").list();
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
		County returnTown = null;
		try{
			
			String SQL_QUERY = "Select county from County county where county.name='"+name+"'";

		List list = session.createQuery(SQL_QUERY).setCacheable(true).setCacheRegion("countyRegion").list();
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
		String[] returnCounty;
		try{
			CountryController countryController = new CountryController();

			
			String SQL_QUERY = "Select county from County county where county.country='"+countryController.getCountryByName(country).getId()+"'";

		List list = session.createQuery(SQL_QUERY).setCacheable(true).setCacheRegion("countyRegion").list();
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
	
}
