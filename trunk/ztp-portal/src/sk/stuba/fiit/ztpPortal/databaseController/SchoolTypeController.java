package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.SchoolType;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class SchoolTypeController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String[] getSchoolTypeNameList(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] schoolTypeList;
		try{
			
			String SQL_QUERY = "Select schoolType.name from SchoolType schoolType";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			schoolTypeList = new String[list.size()];
			int i = 0;
			while (i<list.size()){
				schoolTypeList[i]=((String)list.get(i++));
			}
			
		}finally{
			session.close();
			sf.close();
		}
		return schoolTypeList;
	}
	
	public SchoolType getSchoolTypeByName(String name){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		SchoolType returnSchoolType = null;
		try{
			String SQL_QUERY = "Select schoolType from SchoolType schoolType where schoolType.name='"+name+"'";
			Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		if (list.size()!=0){
			returnSchoolType = (SchoolType)list.get(0);
		}

			
		}finally{
			session.close();
			sf.close();
		}
		return returnSchoolType;
	}
	
}
