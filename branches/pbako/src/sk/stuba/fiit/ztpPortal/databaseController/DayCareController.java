package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.DayCare;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class DayCareController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public boolean saveNewDayCare(DayCare dayCare){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
				
		long dayCareId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			dayCare.setCreationDate(date);
			dayCare.setChangeDate(date);
			
			dayCareId = (Long) session.save(dayCare);
			session.getTransaction().commit();
		}finally{
			session.close();
			sf.close();
		}
		if (dayCareId>=0) return true;
		else return false;
	}

	public boolean updateDayCare(DayCare dayCare){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long dayCareId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			dayCare.setChangeDate(date);
			
			session.update(dayCare);
			session.getTransaction().commit();
			dayCareId=0;
		}finally{
			session.close();
			sf.close();
		}
		if (dayCareId>=0) return true;
		else return false;
	}
	
	public DayCare getDayCareByDayCareId(long dayCareId){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		DayCare dayCare = null;
		try{
			
			String SQL_QUERY = "Select dc.id from DayCare dc where dc.id='"	//TODO: SQL injection
				+ dayCareId + "'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			dayCare = (DayCare) session.get(DayCare.class,dayCareId);
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return dayCare;
	}

	public List<DayCare> getAllDayCares(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<DayCare> dayCare = new ArrayList<DayCare>();
		Long dayCareId;
		try{
			
			String SQL_QUERY = "Select dc.id from DayCare dc";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		int i = 0;
		while (i<list.size()){
			dayCareId = (Long)list.get(i++);
			dayCare.add((DayCare) session.get(DayCare.class,dayCareId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return dayCare;
	}

	/**
	 * Vracia vsetko co je aktivne a neobmedzil to administrator. Vrati aj tie ktore deaktivoval creator inzeratov
	 * @return vsetky aktivne inzeraty
	 */
		
	public List<DayCare> getAllActiveDayCares(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<DayCare> dayCare = new ArrayList<DayCare>();
		Long dayCareId;
		try{

			String SQL_QUERY = "Select dc.id from DayCare dc where dc.state='true'";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			int i = 0;
			while (i<list.size()){
				dayCareId = (Long)list.get(i++);
				dayCare.add((DayCare) session.get(DayCare.class,dayCareId));
			}
		}finally{
			session.close();
			sf.close();
		}

		return dayCare;
	}

	public boolean isDayCareCreator(long dayCareId, long login){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List list;
		try{
			String SQL_QUERY = "Select dc.id from DayCare dc where dc.id='"
				+ dayCareId + "' and dc.creator='"+ login +"'";
		Query query = session.createQuery(SQL_QUERY);
		
		list = query.list();
		
		}finally{
			session.close();
			sf.close();
		}
		
		if (list.size()!=0){
			return true;
		}
		
		return false;
	}

	public List<DayCare> getNewDayCare(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<DayCare> dayCareList = new ArrayList<DayCare>();
		Long dayCareId;
		try{
			
			String SQL_QUERY = "Select dc.id from DayCare dc where dc.state='true' order by dc.id desc limit 3";
		Query query = session.createQuery(SQL_QUERY);
		query.setMaxResults(3);
		List list = query.list();
		int i = 0;
		while (i<list.size()){
			dayCareId = (Long)list.get(i++);
			dayCareList.add((DayCare) session.get(DayCare.class,dayCareId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return dayCareList;
	}
	
}
