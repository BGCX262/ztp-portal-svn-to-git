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

import sk.stuba.fiit.ztpPortal.databaseModel.Comment;
import sk.stuba.fiit.ztpPortal.databaseModel.SearchResultList;
/**
 * Strategia pre vyhladavanie komentarov
 * @author devel
 *
 */
public class SearchCommentStrategy implements SearchStrategy {

	public List<SearchResultList> searchData(String searchString) {

		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();

		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		List result = null;

		try {
			// query pre LUCENE, ktore polia bude prehladavat
			String[] fields = new String[] { "name" };
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fields,
					new StandardAnalyzer());
			org.apache.lucene.search.Query query =  parser.parse( searchString );

			// wrap Lucene query do org.hibernate.Query
			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query,
					Comment.class);

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

		List<SearchResultList> endResults = new ArrayList<SearchResultList>(
				result.size());
		
		for (int i=0; i<result.size(); i++){
		//iba ak je aktivny (admin a vlastnik)	
			if (((Comment)result.get(i)).isActive() && ((Comment)result.get(i)).isState() ){
			
				//Pridanie do LIST vysledkov
			SearchResultList item = new SearchResultList();
			item.setModule("PortalComment");	//nazov modulu
			item.setText(((Comment)result.get(i)).getName());	//text ktory sa ma zobrazovat
			item.setId(((Comment)result.get(i)).getId());	//id inzeratu
			item.setClas(Comment.class);	//trieda (modul) inzeratu
			list.add(item);
			}
		}

		return list;
	}

}
