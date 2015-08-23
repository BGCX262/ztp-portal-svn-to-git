package sk.stuba.fiit.ztpPortal.databaseController;


import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.JobSector;
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
					townList[i]=((String)list.get(i++));
				}
				
			}finally{
				session.close();
				sf.close();
			}
			return townList;
		}
		
		public JobSector getJobSectorByName(String name){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			JobSector returnSector = null;
			try{
				
				String SQL_QUERY = "Select sector from JobSector sector where sector.name='"+name+"'";
				Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			if (list.size()!=0){
				
				returnSector = (JobSector)list.get(0);
			}
			}finally{
				session.close();
				sf.close();
			}
			return returnSector;
		}
		
	}
