package sk.stuba.fiit.ztpPortal.server;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.Comment;
import sk.stuba.fiit.ztpPortal.databaseModel.Course;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.HealthAid;
import sk.stuba.fiit.ztpPortal.databaseModel.Information;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.Living;
import sk.stuba.fiit.ztpPortal.databaseModel.School;

/**
 * Indexacia portalu, vsetkych modulov a obsahu. Zapis indexu sa stara kniznica LUCENE
 * @author Peter Bradac
 *
 */
public class IndexPortal {
	private static final Log log = LogFactory.getLog(IndexPortal.class);

/**
 * Indexacia komentarov. Pri indexacii spravi select query, nad ktorym vykana indexaciu. Vyuziva LUCENE
 * kniznicu. Indexacia poli je urcena pomocou anotacii v Comment.class
 */
	public void indexCommentData(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		try{
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		log.info("Indexácia komentárov");
		
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

		log.info("Indexácia obsahu portálu");
		
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
	
	public void indexJobData(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		try{
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		log.info("Indexácia pracovných ponúk");
		
		List<Job> jobs = session.createQuery("from Job as job").list();
		for (Job job : jobs) {
		    fullTextSession.index(job);
		}
		
		session.getTransaction().commit(); //index is written at commit time       
		}finally{
			session.close();
			sf.close();
		}
		
	}
	
	public void indexEventData(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		try{
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		log.info("Indexácia udalostí");
		
		List<Event> jobs = session.createQuery("from Event as event").list();
		for (Event event : jobs) {
		    fullTextSession.index(event);
		}
		
		session.getTransaction().commit(); //index is written at commit time       
		}finally{
			session.close();
			sf.close();
		}
		
	}
	
	public void indexLivingData(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		try{
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		log.info("Indexácia ubytovania");
		
		List<Living> jobs = session.createQuery("from Living as living").list();
		for (Living event : jobs) {
		    fullTextSession.index(event);
		}
		
		session.getTransaction().commit(); //index is written at commit time       
		}finally{
			session.close();
			sf.close();
		}
		
	}
	
	public void indexCourseData(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		try{
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		log.info("Indexácia kurzov");
		
		List<Course> course = session.createQuery("from Course as course").list();
		for (Course event : course) {
		    fullTextSession.index(event);
		}
		
		session.getTransaction().commit(); //index is written at commit time       
		}finally{
			session.close();
			sf.close();
		}
		
	}
	
	public void indexSchoolData(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		try{
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		log.info("Indexácia škôl");
		
		List<School> schoolList = session.createQuery("from School as school").list();
		for (School school : schoolList) {
		    fullTextSession.index(school);
		}
		
		session.getTransaction().commit(); //index is written at commit time       
		}finally{
			session.close();
			sf.close();
		}
		
	}
	
	public void indexInformationData(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		try{
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		log.info("Indexácia informácií");
		
		List<Information> informationList = session.createQuery("from Information as information").list();
		for (Information information : informationList) {
		    fullTextSession.index(information);
		}
		
		session.getTransaction().commit(); //index is written at commit time       
		}finally{
			session.close();
			sf.close();
		}
		
	}
	
	public void indexHealthAidData(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		try{
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		log.info("Indexácia pomôcok");
		
		List<HealthAid> informationList = session.createQuery("from HealthAid as healthAid").list();
		for (HealthAid information : informationList) {
		    fullTextSession.index(information);
		}
		
		session.getTransaction().commit(); //index is written at commit time       
		}finally{
			session.close();
			sf.close();
		}
	}
	
	public void indexDayCareData(){
		SessionFactory sf = SessionFactoryHolder.getSF();
		Session session = sf.openSession();
		
		try{
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();

		log.info("Indexácia Opatrovatelstvo");
		
		List<HealthAid> dayCareList = session.createQuery("from DayCare as dayCare").list();
		for (HealthAid dayCare : dayCareList) {
		    fullTextSession.index(dayCare);
		}
		
		session.getTransaction().commit(); //index is written at commit time       
		}finally{
			session.close();
			sf.close();
		}
	}
	
}
