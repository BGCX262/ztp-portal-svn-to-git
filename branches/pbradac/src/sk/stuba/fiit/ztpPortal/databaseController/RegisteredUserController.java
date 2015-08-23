package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class RegisteredUserController implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Uloženie nového používate¾a
	 * @param user
	 * @return
	 */
	public boolean saveNewUser(RegisteredUser user){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long userId = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			user.setRegistrationDate(date);
			user.setChangeDate(date);
			
			userId = (Long) session.save(user);
			session.getTransaction().commit();
		}finally{
			session.close();
			sf.close();
		}
		if (userId>=0) return true;
		else return false;
	}
	
	/**
	 * Aktualizácia všetkých údajov používate¾a
	 * @param user
	 * @return
	 */
	public boolean updateUser(RegisteredUser user){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		long returnValue = -1;
		try{
			session.beginTransaction();
			java.sql.Date date = new Date( new java.util.Date().getTime());
			user.setChangeDate(date);
			session.update(user);
			returnValue = 1;
			session.getTransaction().commit();
		}finally{
			session.close();
			sf.close();
		}
		if (returnValue>=0) return true;
		else return false;
	}
	
	/**
	 * Vrátenie registrovaného používate¾a
	 * @param loginName
	 * @return
	 */
	public RegisteredUser getRegisteredUserByLogin(String loginName){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		RegisteredUser user = null;
		try{
			
			String SQL_QUERY = "Select users.id from RegisteredUser users where users.login='"
				+ loginName + "'";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			long userId = (Long) list.get(0);
			
			user = (RegisteredUser) session.get(RegisteredUser.class,userId);
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return user;
	}
	
	/**
	 * Vrátenie všetkých registrovaných používate¾ov
	 * @return
	 */
	public List<RegisteredUser> getAllRegisteredUser(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		List<RegisteredUser> userList = new ArrayList<RegisteredUser>();
		Long userId;
		try{
			
			String SQL_QUERY = "Select user.id from RegisteredUser user";
		Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		int i = 0;
		while (i<list.size()){
			userId = (Long)list.get(i++);
			userList.add((RegisteredUser) session.get(RegisteredUser.class,userId));
		}
		}finally{
			session.close();
			sf.close();
		}
		
		return userList;
	}
	
	/**
	 * Overenie èi je používate¾ zaregistrovaný
	 * @param loginName
	 * @return
	 */
	public boolean isRegisteredUser(String loginName){
		if (getRegisteredUserByLogin(loginName)!=null) return true;
		else return false;
	}
	
	/**
	 * Nastavenie nového hesla používate¾a
	 * @param loginName
	 * @param password
	 * @return
	 */
	public boolean setNewPassword(String loginName, String password){
		RegisteredUser user;
		if ((user = getRegisteredUserByLogin(loginName))!=null) {
			user.setPassword(password);
			updateUser(user);
			return true;
		}else return false;
	}
	
	/**
	 * Overenie èi je používate¾ administrátor
	 * @param loginName
	 * @return
	 */
	public boolean isAdmin(String loginName){
		RegisteredUser user;
		if ((user = getRegisteredUserByLogin(loginName))!=null) {
			return user.isAdmin();
		}else return false;
	}
	
	/**
	 * Overenie èi je používate¾ aktívny
	 * @param loginName
	 * @return
	 */
	public boolean isStatus(String loginName){
		RegisteredUser user;
		if ((user = getRegisteredUserByLogin(loginName))!=null) {
			return user.isState();
		}else return false;
	}
	
	/**
	 * Overenie hesla pre používate¾a
	 * @param loginName
	 * @param password
	 * @return
	 */
	public boolean isLoginNameAndPassword(String loginName, String password){
		RegisteredUser user;
		if ((user = getRegisteredUserByLogin(loginName))!=null) {
			System.out.println("Autorizacia "+ user.getPassword() +" "+password);
			if (password.equals(user.getPassword())) return true;
			else return false;
		}else return false;
	}
}
