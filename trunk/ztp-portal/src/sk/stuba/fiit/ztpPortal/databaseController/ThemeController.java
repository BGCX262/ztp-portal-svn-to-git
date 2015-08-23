package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.Theme;
import sk.stuba.fiit.ztpPortal.databaseModel.Thread;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class ThemeController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean saveNewTheme(Theme theme){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long themeId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			theme.setCreateDate(date);
			theme.setChangeDate(date);
			
			themeId = (Long) session.save(theme);
			session.getTransaction().commit();
		}finally{
			session.close();
			sf.close();
		}
		if (themeId>=0) return true;
		else return false;
	}
	
	public boolean updateTheme(Theme theme){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long themeId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			theme.setChangeDate(date);
			
			session.update(theme);
			session.getTransaction().commit();
			themeId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (themeId>=0) return true;
		else return false;
	}
	
	public Theme getThemeByThemeId(long themeId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		Theme theme = null;
		try{
			
			String SQL_QUERY = "Select theme.id from Theme theme where theme.id='"
				+ themeId + "'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			theme = (Theme) session.get(Theme.class,themeId);
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return theme;
	}
	
	public List<Theme> getAllTheme(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Theme> theme = new ArrayList<Theme>();
		Long themeId;
		try{
			
			String SQL_QUERY = "Select theme.id from Theme theme";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		int i = 0;
		while (i<list.size()){
			themeId = (Long)list.get(i++);
			theme.add((Theme) session.get(Theme.class,themeId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return theme;
	}
	
	
	public List<Theme> getAllActiveTheme(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<Theme> theme = new ArrayList<Theme>();
		Long themeId;
		try{
			
			String SQL_QUERY = "Select theme.id from Theme theme where theme.state='true'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		int i = 0;
		while (i<list.size()){
			themeId = (Long)list.get(i++);
			theme.add((Theme) session.get(Theme.class,themeId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return theme;
	}
	
	public int getThemeThreadCount(Theme theme){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List list;
		try{
			
			String SQL_QUERY = "Select thread.id from Thread thread where thread.state='true' and thread.theme="+theme.getId();
		Query query = session.createQuery(SQL_QUERY);
		list = query.list();
		}finally{
			session.close();
			sf.close();
		}
		
		return list.size();
	}
	
	
}
