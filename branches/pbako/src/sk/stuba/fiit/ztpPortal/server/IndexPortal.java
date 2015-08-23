package sk.stuba.fiit.ztpPortal.server;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.Comment;

public class IndexPortal {

	public void indexCommentData(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		try{
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		System.out.println("Indexácia komentárov");
		
		List<Comment> comments = session.createQuery("from Comment as comment").list();
		for (Comment comment : comments) {
		    fullTextSession.index(comment);
		}
		
		session.getTransaction().commit(); //index is written at commit time       
		}finally{
			session.close();
			sf.close();
		}
		
	}

	public void indexPortalContent() {
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		try{
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		System.out.println("Indexácia obsahu portálu");
		
		List<CmsContent> cmsContent = session.createQuery("from CmsContent as cmsContent").list();
		for (CmsContent content : cmsContent) {
		    fullTextSession.index(content);
		}
		
		session.getTransaction().commit(); //index is written at commit time       
		}finally{
			session.close();
			sf.close();
		}
		
	}
	
}
