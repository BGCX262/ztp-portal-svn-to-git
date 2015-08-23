package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.LivingType;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class LivingTypeController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String[] getLivingTypeNameList(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] livingTypeList;
		try{
			
			String SQL_QUERY = "Select livingType.name from LivingType livingType";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			livingTypeList = new String[list.size()];
			int i = 0;
			while (i<list.size()){
				livingTypeList[i]=((String)list.get(i++));
			}
			
		}finally{
			session.close();
			sf.close();
		}
		return livingTypeList;
	}
	
	public LivingType getLivingTypeByName(String name){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		LivingType returnLivingType = null;
		try{
			String SQL_QUERY = "Select livingType from LivingType livingType where livingType.name='"+name+"'";
			Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		if (list.size()!=0){
			returnLivingType = (LivingType)list.get(0);
		}

			
		}finally{
			session.close();
			sf.close();
		}
		return returnLivingType;
	}
	
}
