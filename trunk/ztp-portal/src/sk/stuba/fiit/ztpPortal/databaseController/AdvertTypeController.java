package sk.stuba.fiit.ztpPortal.databaseController;


import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.AdvertType;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class AdvertTypeController implements Serializable{

		private static final long serialVersionUID = 1L;
		
		public String[] getAdvertTypeNameList(){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			String[] townList;
			Long townId;
			try{
				
				String SQL_QUERY = "Select advert.name from AdvertType advert";
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
		
		public AdvertType getAdvertTypeByName(String name){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			AdvertType returnSector = null;
			try{
				
				String SQL_QUERY = "Select advert from AdvertType advert where advert.name='"+name+"'";
				Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			if (list.size()!=0){
				
				returnSector = (AdvertType)list.get(0);
			}

				
			}finally{
				session.close();
				sf.close();
			}
			return returnSector;
		}
		
	}
