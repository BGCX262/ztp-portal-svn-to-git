package sk.stuba.fiit.ztpPortal.databaseModel;

import java.io.Serializable;

import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.MainPage;
import sk.stuba.fiit.ztpPortal.core.Registration;
import sk.stuba.fiit.ztpPortal.databaseController.CommentController;
import sk.stuba.fiit.ztpPortal.module.accomodation.LivingList;
import sk.stuba.fiit.ztpPortal.module.accomodation.LivingView;
import sk.stuba.fiit.ztpPortal.module.dayCare.DayCareView;
import sk.stuba.fiit.ztpPortal.module.education.CourseView;
import sk.stuba.fiit.ztpPortal.module.education.EducationList;
import sk.stuba.fiit.ztpPortal.module.education.SchoolView;
import sk.stuba.fiit.ztpPortal.module.event.EventView;
import sk.stuba.fiit.ztpPortal.module.forum.CommentList;
import sk.stuba.fiit.ztpPortal.module.forum.ThemeList;
import sk.stuba.fiit.ztpPortal.module.healthAid.HealthAidView;
import sk.stuba.fiit.ztpPortal.module.information.InfoView;
import sk.stuba.fiit.ztpPortal.module.job.JobList;
import sk.stuba.fiit.ztpPortal.module.job.JobView;

/**
 * Sluzi na docasne odkladanie a zlepenie vysledkov vyhladavania z viacerych modulov
 * @author Peter Bradac
 *
 */
public class SearchResultList implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private long id;	//id inzeratu
	
	private String module;	//modul

	private String text;	//text
	
	private String foundText;
	
	private Class<?> clas;	
	
	private CorePage response;	//odpoved pri vybere vysledku, stranka ktora sa zobrazi
	
	private float hit;
	
	public CorePage getResponse() {
		return response;
	}

	public void setResponse(CorePage response) {
		this.response = response;
	}

	public long getId() {
		return id;
	}

	public void setId(long l) {
		this.id = l;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFoundText() {
		return foundText;
	}

	public void setFoundText(String foundText) {
		this.foundText = foundText;
	}

	public float getHit() {
		return hit;
	}

	public void setHit(float hit) {
		this.hit = hit;
	}
		
	public Class<?> getClas() {
		return clas;
	}

	public void setClas(Class<?> class1) {
		this.clas = class1;
	}
/**
 * Nastavenie triedy (stranky) ktora bude odpovedou pri vybere detailu vysledku hladania
 * @return kontkretna CorePage
 */
	public CorePage getResponsePage(){
		CorePage clasa = null;
		
		if (this.clas.toString().equals(Comment.class.toString())) 
			return clasa = new CommentList((new CommentController().getCommentByCommentId(this.id).getThread()));
		
		if (this.clas.toString().equals(MainPage.class.toString()))
			return clasa=new MainPage();
		
		if (this.clas.toString().equals(Registration.class.toString()))
			return clasa=new Registration();
		
		if (this.clas.toString().equals(JobList.class.toString()))
			return clasa=new JobList();
		
		if (this.clas.toString().equals(EducationList.class.toString()))
			return clasa=new EducationList();
		
		if (this.clas.toString().equals(LivingList.class.toString()))
			return clasa=new LivingList();
		
		if (this.clas.toString().equals(Theme.class.toString()))
			return clasa=new ThemeList();
		
		if (this.clas.toString().equals(Job.class.toString()))
			return clasa=new JobView(this.id);
		
		if (this.clas.toString().equals(Course.class.toString()))
			return clasa=new CourseView(this.id);

		if (this.clas.toString().equals(School.class.toString()))
			return clasa=new SchoolView(this.id);
		
		if (this.clas.toString().equals(Event.class.toString()))
			return clasa=new EventView(this.id);
		
		if (this.clas.toString().equals(Living.class.toString()))
			return clasa=new LivingView(this.id);
		
		if (this.clas.toString().equals(Information.class.toString()))
			return clasa=new InfoView(this.id);
		
		if (this.clas.toString().equals(HealthAid.class.toString()))
			return clasa=new HealthAidView(this.id);
		
		if (this.clas.toString().equals(DayCare.class.toString()))
			return clasa=new DayCareView(this.id);
		
		return clasa;
		
		
	}
	
}
