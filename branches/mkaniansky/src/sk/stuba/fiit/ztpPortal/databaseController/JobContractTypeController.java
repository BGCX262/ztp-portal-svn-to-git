package sk.stuba.fiit.ztpPortal.databaseController;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.JobContractType;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;

public class JobContractTypeController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String[] getJobContractNameList(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		String[] townList;
		Long townId;
		try{
			
			String SQL_QUERY = "Select contract.name from JobContractType contract";
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
	
	public JobContractType getJobContractByName(String name){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		JobContractType returnContract = null;
		try{
			
			String SQL_QUERY = "Select contract from JobContractType contract where contract.name='"+name+"'";
			Query query = session.createQuery(SQL_QUERY);

		List list = query.list();
		if (list.size()!=0){
			
			returnContract = (JobContractType)list.get(0);
		}

			
		}finally{
			session.close();
			sf.close();
		}
		return returnContract;
	}
	
}
