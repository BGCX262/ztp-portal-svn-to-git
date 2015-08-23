package sk.stuba.fiit.ztpPortal.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import sk.stuba.fiit.ztpPortal.core.MainPage;
import sk.stuba.fiit.ztpPortal.core.Registration;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.SearchResultList;
import sk.stuba.fiit.ztpPortal.module.job.JobList;

public class SearchPortalContentStrategy implements SearchStrategy{

	public List<SearchResultList> searchData(String searchString){
		
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		List result = null;
		
		try{
		// create native Lucene query
		String[] fields = new String[]{"content"};
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
		org.apache.lucene.search.Query query = parser.parse( searchString );

		System.out.println(query);
		
		// wrap Lucene query in a org.hibernate.Query
		org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, CmsContent.class);

		// execute search
		result = hibQuery.list();
		
		if (result.size()>0) System.out.println(((CmsContent)result.get(0)).getName());
		else System.out.println("ziadny navrat");
		
//		tx.commit();
//		session.close();  
		
		session.getTransaction().commit(); //index is written at commit time       
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			session.close();
			sf.close();
		}
		
		List <SearchResultList> list = new ArrayList <SearchResultList>();
		
		for (int i=0; i<result.size(); i++){
			
			SearchResultList item = new SearchResultList();
			item.setModule("PortalContent");
			item.setText(((CmsContent)result.get(i)).getName());
			item.setId(((CmsContent)result.get(i)).getId());
			
			if (((CmsContent)result.get(i)).getName().equals("main")) {
				item.setClas(MainPage.class);
			}
			
			if (((CmsContent)result.get(i)).getName().equals("registration")) 
				item.setClas(Registration.class);
			
			if (((CmsContent)result.get(i)).getName().equals("job")) 
				item.setClas(JobList.class);
			
			list.add(item);
		}

		return list;
	}
	

	
}
