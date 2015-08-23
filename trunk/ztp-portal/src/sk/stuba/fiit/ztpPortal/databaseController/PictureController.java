package sk.stuba.fiit.ztpPortal.databaseController;


import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.Living;
import sk.stuba.fiit.ztpPortal.databaseModel.Picture;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class PictureController implements Serializable{

		private static final long serialVersionUID = 1L;
		
		public boolean saveNewPicture(Picture picture){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			long pictureId = -1;
			try{
				session.beginTransaction();
				java.sql.Date date = new Date( new java.util.Date().getTime());
				picture.setCreateDate(date);
				picture.setChangeDate(date);
				
				pictureId = (Long) session.save(picture);
				session.getTransaction().commit();
			}finally{
				session.close();
				sf.close();
			}
			if (pictureId>=0) return true;
			else return false;
		}
		
		public Picture getPictureByPictureId(long pictureId){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			Picture picture = null;
			try{
				
				String SQL_QUERY = "Select picture.id from Picture picture where picture.id='"
					+ pictureId + "'";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			if (list.size()!=0){
				
				picture = (Picture) session.get(Picture.class,pictureId);
			}
			}finally{
				session.close();
				sf.close();
			}
			
			return picture;
		}
		
		public boolean updatePicture(Picture picture){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			long pictureId = -1;
			try{
				session.beginTransaction();
				java.sql.Date date = new Date( new java.util.Date().getTime());
				picture.setChangeDate(date);
				
				session.update(picture);
				session.getTransaction().commit();
				pictureId=0;
			}finally{
				session.close();
				sf.close();
			}
			if (pictureId>=0) return true;
			else return false;
		}
		
		/**
		 * Vidim aktivne = true, teda co nezmazal admin
		 * @param event
		 * @return
		 */
		
		public List<Picture> getActiveEventPicture(Event event){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			List<Picture> pictureList = new ArrayList<Picture>();
			Long pictureId;
			try{
				
				String SQL_QUERY = "Select picture.id from Picture picture where picture.active='true' and picture.event='"+event.getId()+"' order by picture.id desc";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			int i = 0;
			while (i<list.size()){
				pictureId = (Long)list.get(i++);
				pictureList.add((Picture) session.get(Picture.class,pictureId));
			}
			}finally{
				session.close();
				sf.close();
			}
			
			return pictureList;
		}

		/**
		 * Vidim stav fotky = true, teda tie co vlastnik udalosti aktivoval
		 * @param event
		 * @return
		 */
		public List<Picture> getActiveEventStatePicture(Event event) {
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			List<Picture> pictureList = new ArrayList<Picture>();
			Long pictureId;
			try{
				
				String SQL_QUERY = "Select picture.id from Picture picture where picture.state='true' and picture.event='"+event.getId()+"' order by picture.id desc";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			int i = 0;
			while (i<list.size()){
				pictureId = (Long)list.get(i++);
				pictureList.add((Picture) session.get(Picture.class,pictureId));
			}
			}finally{
				session.close();
				sf.close();
			}
			
			return pictureList;
		}
		
		
		/// teraz pre living
		
		/**
		 * Vidim aktivne = true, teda co nezmazal admin
		 * @param event
		 * @return
		 */
		
		public List<Picture> getActiveLivingPicture(Living living){
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			List<Picture> pictureList = new ArrayList<Picture>();
			Long pictureId;
			try{
				
				String SQL_QUERY = "Select picture.id from Picture picture where picture.active='true' and picture.living='"+living.getId()+"' order by picture.id desc";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			int i = 0;
			while (i<list.size()){
				pictureId = (Long)list.get(i++);
				pictureList.add((Picture) session.get(Picture.class,pictureId));
			}
			}finally{
				session.close();
				sf.close();
			}
			
			return pictureList;
		}

		/**
		 * Vidim stav fotky = true, teda tie co vlastnik udalosti aktivoval
		 * @param event
		 * @return
		 */
		public List<Picture> getActiveLivingStatePicture(Living living) {
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			List<Picture> pictureList = new ArrayList<Picture>();
			Long pictureId;
			try{
				
				String SQL_QUERY = "Select picture.id from Picture picture where picture.state='true' and picture.living='"+living.getId()+"' order by picture.id desc";
			Query query = session.createQuery(SQL_QUERY);

			List list = query.list();
			int i = 0;
			while (i<list.size()){
				pictureId = (Long)list.get(i++);
				pictureList.add((Picture) session.get(Picture.class,pictureId));
			}
			}finally{
				session.close();
				sf.close();
			}
			
			return pictureList;
		}
		
		
	}
