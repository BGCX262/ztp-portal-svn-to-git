package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class CmsContentController implements Serializable{

		private static final long serialVersionUID = 1L;
		
		public CmsContent getContentByName(String name){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			CmsContent returnCmsContent = null;
			try{
				
				String SQL_QUERY = "Select content from CmsContent content where content.name='"+name+"'";
				Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			if (list.size()!=0){
				
				returnCmsContent = (CmsContent)list.get(0);
			}

				
			}finally{
				session.close();
				sf.close();
			}
			return returnCmsContent;
		}
		
		public boolean updateCmsContent(CmsContent cmsContent){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			long cmsContentId = -1;
			try{
				session.beginTransaction();				
				session.update(cmsContent);
				session.getTransaction().commit();
				cmsContentId=0;
			}finally{
				session.close();
				sf.close();
			}
			if (cmsContentId>=0) return true;
			else return false;
		}
		
	}
