package sk.stuba.fiit.ztpPortal.core;


import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.module.dayCare.DayCareList;
import sk.stuba.fiit.ztpPortal.module.event.EventList;
import sk.stuba.fiit.ztpPortal.module.forum.ThemeList;
import sk.stuba.fiit.ztpPortal.module.job.JobList;
import sk.stuba.fiit.ztpPortal.server.SearchCommentStrategy;

public class PageRenderer extends CorePage{

	private static final long serialVersionUID = 1L;

	private RegisteredUserController userController;
	private RegisteredUser user;
	
	TextField login;
	TextField password;
	Button logoutButton;
	Link profilePageLink;
	Label logedAs;
	Label myProfile;
	Button submit;
	Button registrationButton;
	ComponentFeedbackPanel loginFeedback;
	ComponentFeedbackPanel passwordFeedback;
	Form loginForm;
	Form statusForm;
	Form searchForm;
	
	FeedbackPanel doLoginFeedback;
	
	private ValueMap properties = new ValueMap();

	public void setUser(RegisteredUser user){
		this.user = user;
	}
	
	public Form getLoginForm(){
		loginForm = new LoginForm("loginForm");
		return loginForm;
	}
		
	public Form getStatusForm(){
		statusForm = new StatusForm("statusForm");
		return statusForm;
	}
	
	public Form getSearchForm(){
		searchForm = new SearchForm("searchForm");
		return searchForm;
	}
	
	public Link getDayCareListLink(){
		Link dayCareListLink = new Link("dayCareList") {
			public void onClick() {
				setResponsePage(DayCareList.class);
			}
		};
		return dayCareListLink;
}
	
	public Link getJobListLink(){
			Link jobListLink = new Link("jobList") {
				public void onClick() {
					setResponsePage(JobList.class);
				}
			};
			return jobListLink;
	}
	
	public Link getHomePageLink(){
			Link homeLink = new Link("homeLink"){
				public void onClick() {
					setResponsePage(new MainPage());
				}
			};
			return homeLink;
	}
	
	public Link getForumLink(){
		Link forumLink = new Link("forumList"){
			public void onClick() {
				setResponsePage(new ThemeList());
			}
		};
		return forumLink;
		
	}
	
	public Link getEventLink(){
		Link eventLink = new Link("eventList"){
			public void onClick() {
				setResponsePage(new EventList());
			}
		};
		return eventLink;
		
	}
	
	public Link getDayCareLink(){
		Link homeLink = new Link("dayCareList"){
			public void onClick() {
				setResponsePage(new DayCareList());
			}
		};
		return homeLink;
}
	
	public FeedbackPanel getFeedback(){ 
		return doLoginFeedback;
	}
	
	public void disableStatusForm(){
		statusForm.setVisible(false);
		logedAs.setVisible(false);
		logoutButton.setVisible(false);
		profilePageLink.setVisible(false);
		myProfile.setVisible(false);
	}
	
	public void disableLoginForm(){
		loginForm.setVisible(false);
		login.setVisible(false);
		password.setVisible(false);
		submit.setVisible(false);
		registrationButton.setVisible(false);
	}
	
	
	
	/**
	 * Login formulár
	 * @author devel
	 *
	 */
	public final class LoginForm extends Form {
		
	public LoginForm(final String id) {
		super(id);
		
		login = new TextField("login", new PropertyModel(properties,
				"login"));
		login.setRequired(true);
		
		if (user!=null)
		login.setModelValue(user.getLogin());
		login.setRequired(false);
		login.setLabel(new Model("Prihlasovacie meno"));
		add(login);
//		add(loginFeedback = new ComponentFeedbackPanel("loginFeedback",
//				login));
		add(new Label("headerLoginLabel", "Prihlasovacie meno:"));

		password = new PasswordTextField("password", new PropertyModel(
				properties, "password"));
		password.setRequired(false);
		add(password);
		password.setLabel(new Model("Prístupové heslo"));
//		add(passwordFeedback = new ComponentFeedbackPanel(
//				"passwordFeedback", password));
		add(new Label("headerPasswordLabel", "Heslo:"));

		add(submit = new Button("doLoginButton",
				new ResourceModel("button.submit")){
			public void onSubmit() {
					userController = new RegisteredUserController();
					
					String inputPassword=password.getModelObjectAsString();
					String inputLogin=login.getModelObjectAsString();
					
					if (userController.isLoginNameAndPassword(inputLogin, inputPassword) && !userController.isStatus(inputLogin)){
						doLoginFeedback.warn("Používate¾ské konto je deaktivované. Kontaktujte administrátora.");
						login.setModelValue("");
						password.setModelValue("");
						return;
					}
					
					if (userController.isLoginNameAndPassword(inputLogin, inputPassword)){
						RegisteredUser user = userController.getRegisteredUserByLogin(login.getModelObjectAsString());
					
						((CoreSession) getSession()).setLoged(user.getLogin());
						((CoreSession) getSession()).setUserId(user.getId());
						
						System.out.println("authorizing " + user.getSurname());
						MainPage page = new MainPage();
						setResponsePage(page);
						
						userController =null;
					}else {
						login.setModelValue("");
						password.setModelValue("");
//						loginFeedback.warn("Zlé meno alebo heslo");
						doLoginFeedback.warn("Zlé prihlasovacie meno alebo heslo");
					}
						
			 }
		}
		);
		
		add(registrationButton = new Button("registrationButton",
				new ResourceModel("button.registrationButton")){

			public void onSubmit() {
					setResponsePage(new Registration());
					}
			 }
		);

//		loginFeedback=new ComponentFeedbackPanel("feedback",this);
		
		
		doLoginFeedback= new FeedbackPanel("feedback");
		
		//doLoginFeedback= new FeedbackPanel("doLoginFeedback",new ContainerFeedbackMessageFilter(this));
	}

}
	/**
	 * Kto je prihlásený formulár
	 * @author devel
	 *
	 */
	public final class StatusForm extends Form {
		
		
		private static final long serialVersionUID = 1L;
		
		public StatusForm(final String id){
			super(id);
			
			logedAs = new Label("statusLoginLabel", "Ste prihlásený ako");
			add(logedAs);
			String userName;
			if (user==null) userName="";
			else userName=user.getLogin();
			add(new Label("loginNameLabel", userName));
			
			myProfile = new Label("myProfile", "môj profil");
			
			add(logoutButton = new Button("doLogout",
					new ResourceModel("button.doLogout")));
			
			profilePageLink = new Link("profilePageLink"){ 
				
				public void onClick() {
					setResponsePage(new ProfilePage());
				}
			};
			add(profilePageLink.add(myProfile));
//			add(myProfile);
			
		}
		
		protected void onSubmit() {
			((CoreSession) getSession()).setLoged(null);
			((CoreSession) getSession()).invalidateNow();
			user = null;
			login = null;
			MainPage page = new MainPage();
            setResponsePage(MainPage.class);
			} 
	}
	
	
	public final class SearchForm extends Form{
		private static final long serialVersionUID = 1L;
		
		TextField searchField;
		Button searchButton;
		
		public SearchForm(final String id){
			super(id);
			
			searchField = new TextField("search", new PropertyModel(properties,
			"search"));
			
			add(searchField);
			
			add(searchButton = new Button("searchButton",
					new ResourceModel("button.submit")){
				public void onSubmit() {
					SearchCommentStrategy portal = new SearchCommentStrategy();
					
					String searchText = searchField.getModelObjectAsString();
					System.out.println("WW"+searchText+"WW");
					if (searchText=="") searchText="//t"; 
					
					List resultList = portal.searchData(searchText);
					setResponsePage(new SearchPage(resultList));
				}
			});
			
		}
	}
	
}
