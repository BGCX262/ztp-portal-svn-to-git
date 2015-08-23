package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.GlobalSetting;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class GlobalSettingController implements Serializable{

		private static final long serialVersionUID = 1L;
		
		public GlobalSetting getSettingByName(String name){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			GlobalSetting returnGlobalSetting = null;
			try{
				
				String SQL_QUERY = "Select globalsetting from GlobalSetting globalsetting where globalsetting.name='"+name+"'";
				Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			if (list.size()!=0){
				
				returnGlobalSetting = (GlobalSetting)list.get(0);
			}

				
			}finally{
				session.close();
				sf.close();
			}
			return returnGlobalSetting;
		}
		
		public boolean updateGlobalSetting(GlobalSetting globalSetting){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			long globalSettingId = -1;
			try{
				session.beginTransaction();				
				session.update(globalSetting);
				session.getTransaction().commit();
				globalSettingId=0;
			}finally{
				session.close();
				sf.close();
			}
			if (globalSettingId>=0) return true;
			else return false;
		}
		
	}
