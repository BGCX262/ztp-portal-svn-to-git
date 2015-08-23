package sk.stuba.fiit.ztpPortal.core;


import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.module.job.JobList;
import sk.stuba.fiit.ztpPortal.module.opatrovatelstvo.AidList;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

public class PageRenderer extends CorePage{

	private static final long serialVersionUID = 1L;

	private RegisteredUserController userController;
	private RegisteredUser user;
	
	TextField login;
	TextField password;
	Button submit;
	Button registrationButton;
	ComponentFeedbackPanel loginFeedback;
	ComponentFeedbackPanel passwordFeedback;
	
	FeedbackPanel doLoginFeedback;
	
	private ValueMap properties = new ValueMap();

	public void setUser(RegisteredUser user){
		this.user = user;
	}
	
	public Form getLoginForm(){
		return new LoginForm("loginForm");
	}
		
	public Form getStatusForm(){
		return new StatusForm("statusForm");
	}
	
	public Link getAidListLink(){
		Link aidListLink = new Link("aidList") {
			public void onClick() {
				setResponsePage(AidList.class);
			}
		};
		return aidListLink;
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
		add(login);
//		add(loginFeedback = new ComponentFeedbackPanel("loginFeedback",
//				login));
		add(new Label("headerLoginLabel", "Prihlasovacie meno:"));

		password = new PasswordTextField("password", new PropertyModel(
				properties, "password"));
		password.setRequired(false);
		add(password);
//		add(passwordFeedback = new ComponentFeedbackPanel(
//				"passwordFeedback", password));
		add(new Label("headerPasswordLabel", "Heslo:"));

		add(submit = new Button("doLoginButton",
				new ResourceModel("button.submit")){
			public void onSubmit() {
					userController = new RegisteredUserController();
					
					String inputPassword=password.getModelObjectAsString();
					String inputLogin=login.getModelObjectAsString();
					
					if (userController.isLoginNameAndPassword(inputLogin, inputPassword)){
						RegisteredUser user = userController.getRegisteredUserByLogin(login.getModelObjectAsString());
					
						((CoreSession) getSession()).setLoged(user.getLogin());
						
						System.out.println("authorizing " + user.getSurname());
						MainPage page = new MainPage();
						setResponsePage(page);
						
						userController =null;
					}else {
						login.setModelValue("");
						password.setModelValue("");
						doLoginFeedback.warn("Zlé meno alebo heslo");
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

		doLoginFeedback= new FeedbackPanel("doLoginFeedback",new ContainerFeedbackMessageFilter(this));
	    add(doLoginFeedback);
	}

}
	/**
	 * Kto je prihlásený formulár
	 * @author devel
	 *
	 */
	public final class StatusForm extends Form {
		private static final long serialVersionUID = 1L;
		Button loginButton;
		Button registrationButton;
		
		public StatusForm(final String id){
			super(id);
			
			add(new Label("statusLoginLabel", "Ste prihlásený ako"));
			String userName;
			if (user==null) userName="";
			else userName=user.getLogin();
			add(new Label("loginNameLabel", userName));
			add(loginButton = new Button("doLogout",
					new ResourceModel("button.doLogout")));
			
			Link profilePageLink = new Link("profilePageLink"){
				public void onClick() {
					setResponsePage(new ProfilePage());
				}
			};
			add(profilePageLink);
			
		}
		
		protected void onSubmit() {
			((CoreSession) getSession()).setLoged(null);
			((CoreSession) getSession()).invalidateNow();
			user = null;
			login = null;
			MainPage page = new MainPage();
            setResponsePage(MainPage.class);
            System.out.println(login +" !!");
			} 
	}
	
}
