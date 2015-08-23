package sk.stuba.fiit.ztpPortal.core;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class HeaderPanel extends Panel{

	private static final long serialVersionUID = -4049106319370888526L;
	private static PageRenderer pageRenderer;
	private RegisteredUser registeredUser;
	private Form loginForm;
	private Form statusForm;
	
	public HeaderPanel(String id, RegisteredUser registeredUser) {
		super(id);
		this.registeredUser=registeredUser;
		
		pageRenderer = new PageRenderer();
		
		add(pageRenderer.getSearchForm());
		
		pageRenderer.setUser(registeredUser);
		loginForm = pageRenderer.getLoginForm();
		add(loginForm);
		statusForm = pageRenderer.getStatusForm();
		add(statusForm);
	
		if (registeredUser==null) pageRenderer.disableStatusForm();
		else pageRenderer.disableLoginForm();
		
		add(pageRenderer.getLivingLink());
		add(pageRenderer.getEducationLink());
		add(pageRenderer.getEventLink());
		add(pageRenderer.getForumLink());
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());
		add(pageRenderer.getInformationLink());
		add(pageRenderer.getHealthAidLink());
		add(pageRenderer.getStyleSelectPageLink());
		add(pageRenderer.getDayCareLink());
		add(pageRenderer.getPageMapLink());
	}

	public RegisteredUser getRegisteredUser() {
		return registeredUser;
	}

	public void setRegisteredUser(RegisteredUser registeredUser) {
		this.registeredUser = registeredUser;
	}

	public Form getLoginForm() {
		return loginForm;
	}

	public void setLoginForm(Form loginForm) {
		this.loginForm = loginForm;
	}

	public Form getStatusForm() {
		return statusForm;
	}

	public void setStatusForm(Form statusForm) {
		this.statusForm = statusForm;
	}
	
}
