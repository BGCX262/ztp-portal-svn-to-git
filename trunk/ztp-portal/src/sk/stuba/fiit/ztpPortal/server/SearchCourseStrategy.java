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

import sk.stuba.fiit.ztpPortal.databaseModel.Course;
import sk.stuba.fiit.ztpPortal.databaseModel.SearchResultList;

public class SearchCourseStrategy implements SearchStrategy {

	public List<SearchResultList> searchData(String searchString) {

		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();

		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		List result = null;

		try {
			// create native Lucene query
			String[] fields = new String[] { "name","address","town","homePage","contactPerson","phone" };
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fields,
					new StandardAnalyzer());
			org.apache.lucene.search.Query query =  parser.parse( searchString );

			// wrap Lucene query in a org.hibernate.Query
			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query,
					Course.class);

			// execute search
			result = hibQuery.list();

			session.getTransaction().commit(); // index is written at commit
												// time
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			session.close();
			sf.close();
		}

		List<SearchResultList> list = new ArrayList<SearchResultList>();

//		List<SearchResultList> endResults = new ArrayList<SearchResultList>(
//				result.size());
		for (int i=0; i<result.size(); i++){
			
			if (((Course)result.get(i)).isActive() && ((Course)result.get(i)).isState() ){
			
			SearchResultList item = new SearchResultList();
			item.setModule("Kurz");
			item.setText(((Course)result.get(i)).getName() + ", "+ ((Course)result.get(i)).getTown());
			item.setId(((Course)result.get(i)).getId());
			item.setClas(Course.class);
			list.add(item);
			}
		}

		return list;
	}

}
