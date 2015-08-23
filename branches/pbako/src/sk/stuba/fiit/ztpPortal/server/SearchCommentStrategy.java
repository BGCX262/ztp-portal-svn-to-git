package sk.stuba.fiit.ztpPortal.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import sk.stuba.fiit.ztpPortal.databaseModel.Comment;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.SearchResultList;
import sk.stuba.fiit.ztpPortal.module.job.JobView;

public class SearchCommentStrategy implements SearchStrategy {

	public List<SearchResultList> searchData(String searchString) {

		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();

		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		List<Object[]> result = null;

		try {
			// create native Lucene query
			String[] fields = new String[] { "name" };
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fields,
					new StandardAnalyzer());

			org.apache.lucene.search.Query query =  parser.parse( searchString );

			System.out.println(query);

			// wrap Lucene query in a org.hibernate.Query
			FullTextQuery hibQuery = fullTextSession.createFullTextQuery(query,
					Comment.class);

			// execute search
			result = hibQuery.setProjection("name", FullTextQuery.SCORE).list();

			// if (result.size()>0)
			// System.out.println(((Comment)result.get(0)).getName());
			// else System.out.println("ziadny navrat");

			// tx.commit();
			// session.close();

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

		System.out.println(">>>" + result.get(0));

		System.out.println(">>>" + result.getClass());

		List<SearchResultList> endResults = new ArrayList<SearchResultList>(
				result.size());
		for (Object[] line : result) {
			SearchResultList item = new SearchResultList();

			System.out.println(">>>" + line[0] + " " + line[1] + " "+ " ");

			// item.setModule("Comment");
			// item.setText(((Comment)result.get(i)).getName());
			// item.setId(((Comment)result.get(i)).getId());
			// item.setClas(Comment.class);
			// (
			// (String) line[0],
			// (String) line[1],
			// (Float) line[2] );
			endResults.add(item);
		}
		return null;

		// for (int i=0; i<result.size(); i++){
		//			
		// SearchResultList item = new SearchResultList();
		// item.setModule("Comment");
		// item.setText(((Comment)result.get(i)).getName());
		// item.setId(((Comment)result.get(i)).getId());
		// item.setClas(Comment.class);
		//			
		// list.add(item);
		// }
		//
		// return list;
	}

}
