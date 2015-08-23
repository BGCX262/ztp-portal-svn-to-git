package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.CourseType;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class CourseTypeController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String[] getCourseTypeNameList(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] CourseTypeList;
		try{
			
			String SQL_QUERY = "Select courseType.name from CourseType courseType";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			CourseTypeList = new String[list.size()];
			int i = 0;
			while (i<list.size()){
				CourseTypeList[i]=((String)list.get(i++));
			}
			
		}finally{
			session.close();
			sf.close();
		}
		return CourseTypeList;
	}
	
	public CourseType getCourseTypeByName(String name){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		CourseType returnCourseType = null;
		try{
			String SQL_QUERY = "Select courseType from CourseType courseType where courseType.name='"+name+"'";
			Query query = session.createQuery(SQL_QUERY);
		List list = query.list();
		if (list.size()!=0){
			returnCourseType = (CourseType)list.get(0);
		}

			
		}finally{
			session.close();
			sf.close();
		}
		return returnCourseType;
	}
	
}
