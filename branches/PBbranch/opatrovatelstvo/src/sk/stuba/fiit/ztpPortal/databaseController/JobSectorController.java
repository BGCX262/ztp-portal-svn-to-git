package sk.stuba.fiit.ztpPortal.databaseController;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class JobSectorController implements Serializable{

		private static final long serialVersionUID = 1L;
		
		public String[] getJobSectorNameList(){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			String[] townList;
			Long townId;
			try{
				
				String SQL_QUERY = "Select sector.name from JobSector sector";
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
		
	}
