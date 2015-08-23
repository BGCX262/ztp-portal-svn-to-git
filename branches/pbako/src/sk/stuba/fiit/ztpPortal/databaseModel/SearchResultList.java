package sk.stuba.fiit.ztpPortal.databaseModel;

import java.io.Serializable;

import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.MainPage;
import sk.stuba.fiit.ztpPortal.core.Registration;
import sk.stuba.fiit.ztpPortal.databaseController.CommentController;
import sk.stuba.fiit.ztpPortal.module.forum.CommentList;
import sk.stuba.fiit.ztpPortal.module.job.JobList;

public class SearchResultList implements Serializable{
	
	private long id;
	
	private String module;
	
	private String text;
	
	private String foundText;
	
	private Class<?> clas;
	
	private float hit;
	
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

	public CorePage getResponsePage(){
		CorePage clasa = null;
		
		if (this.clas.equals(Comment.class)) 
			return clasa = new CommentList((new CommentController().getCommentByCommentId(this.id).getThread()));
		
		if (this.clas.toString().equals(MainPage.class.toString()))
			return clasa=new MainPage();
		
		if (this.clas.toString().equals(Registration.class.toString()))
			return clasa=new Registration();
		
		if (this.clas.toString().equals(JobList.class.toString()))
			return clasa=new JobList();

		
		System.out.println("vraciam " + clasa);
		return clasa;
		
		
	}
	
}
