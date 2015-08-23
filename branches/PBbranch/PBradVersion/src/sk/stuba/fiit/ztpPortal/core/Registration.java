package sk.stuba.fiit.ztpPortal.core;

import java.util.Arrays;
import java.util.List;


import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.TownController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.value.ValueMap;

public class Registration extends CorePage{
	
	private static final long serialVersionUID = 1L;
	
	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	
	private PageRenderer pageRenderer;
	private static FeedbackPanel feedbackPanel;
	
	public Registration() {
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		
		if (login!=null)
			user =userController.getRegisteredUserByLogin(login);
		else user = null;
	
		//
		//
		//
		//
		//
		//
		//
//		 String path = getServletContext().getRealPath("/");
		 
		 feedbackPanel = new FeedbackPanel("feedback",new ContainerFeedbackMessageFilter(this));
	        add(feedbackPanel);
		 
	        
	     pageRenderer = new PageRenderer();   
	     setPageHeaderPanel();  
	     setPageNavigationPanel();
	        
		 add(new RegistrationForm("registrationForm"));
	}
	
	private void setPageHeaderPanel(){
		pageRenderer.setUser(user);
		add(pageRenderer.getLoginForm());
		add(pageRenderer.getStatusForm());
	}
	
	private void setPageNavigationPanel(){
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());	
	}
	
	public final class RegistrationForm extends Form {

		private static final long serialVersionUID = 1L;
		
		TextField name;
		TextField surname;
		TextField login;
		TextField password;
		TextField passwordAgain;
		TextField email;
		Button submit;
		DropDownChoice region;
		DropDownChoice town;
		DropDownChoice handicapType;
		
		private final List REGION_LIST = Arrays.asList(new String[] {"The Server Side", "Java Lobby", "Java.Net" });
		
//		private final List TOWN_LIST = Arrays.asList(new String[] {"The Server Side", "Java Lobby", "Java.Net" });
	
		private final List TOWN_LIST = Arrays.asList(new TownController().getTownNameList());
		
		private final List HANDICAPTYPE_LIST = Arrays.asList(new String[] {"The Server Side", "Java Lobby", "Java.Net" });
		
		CheckBox preferRegion;
		
//		ComponentFeedbackPanel loginNameFeedback;
//		ComponentFeedbackPanel passwordFeedback;
//		ComponentFeedbackPanel submitFeedback;
//		ComponentFeedbackPanel titleFeedback;
//		ComponentFeedbackPanel addressFeedback;
//		ComponentFeedbackPanel passwordAgainFeedback;
//		ComponentFeedbackPanel surnameFeedback;
//		ComponentFeedbackPanel nameFeedback;
		
		private ValueMap properties = new ValueMap();

		public RegistrationForm(final String id) {
			super(id);
			
			name = new TextField("name", new PropertyModel(properties,
			"name"));
			name.setRequired(true);
			add(name);
		//	add(nameFeedback = new ComponentFeedbackPanel("nameFeedback",
		//			name));
			add(new Label("nameLabel", "Meno"));
			add(new FormComponentFeedbackBorder("border").add(name));
	
			surname = new TextField("surname", new PropertyModel(properties,
			"surname"));
			surname.setRequired(true);
			add(surname);
		//	add(surnameFeedback = new ComponentFeedbackPanel("surnameFeedback",
		//		surname));
			add(new Label("surnameLabel", "Priezvisko"));
			
			email = new TextField("email", new PropertyModel(properties,
			"email"));
			email.setRequired(true);
			add(email);
//			add(addressFeedback = new ComponentFeedbackPanel("addressFeedback",
//					address));
			add(new Label("emailLabel", "Email"));
			
			login = new TextField("login", new PropertyModel(properties,
					"login"));
			login.setRequired(true);
			add(login);
//			add(loginNameFeedback = new ComponentFeedbackPanel("loginNameFeedback",
//					loginName));
			add(new Label("loginLabel", "Login"));

			region= new DropDownChoice("region", new PropertyModel(properties,
					"region"),REGION_LIST);
			region.setRequired(false);
			add(region);
			add(new Label("regionLabel","Region"));
			
			town= new DropDownChoice("town", new PropertyModel(properties,
					"town"),TOWN_LIST);
			town.setRequired(false);
			add(town);
			add(new Label("townLabel","Town"));
			
			handicapType= new DropDownChoice("handicapType", new PropertyModel(properties,
					"handicapType"),HANDICAPTYPE_LIST);
			handicapType.setRequired(false);
			add(handicapType);
			add(new Label("handicapTypeLabel","Handicap"));
			
			preferRegion = new CheckBox("preferRegion", new PropertyModel(properties,
					"preferRegion"));
			
			add(preferRegion);
			add(new Label("preferRegionLabel","Preferujem"));
			
			password = new PasswordTextField("password", new PropertyModel(
					properties, "password"));
			password.setRequired(true);
			add(password);
//			add(passwordFeedback = new ComponentFeedbackPanel(
//					"passwordFeedback", password));
			add(new Label("passwordLabel", "Heslo:"));

			passwordAgain = new PasswordTextField("passwordAgain", new PropertyModel(properties,
			"passwordAgain"));
			passwordAgain.setRequired(true);
			add(passwordAgain);
//			add(passwordAgainFeedback = new ComponentFeedbackPanel("passwordAgainFeedback",
//					passwordAgain));
			add(new Label("passwordAgainLabel", "Heslo znova"));
			
			
			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
//			add(submitFeedback = new ComponentFeedbackPanel("submitFeedback",
//					submit));
		}
		
		private boolean checkPassword(String password, String passwordAgain){
			return password.equals(passwordAgain);
		}
		
		private RegisteredUser createNewUser(){
			RegisteredUser user = new RegisteredUser();

			user.setName(name.getModelObjectAsString());
			user.setSurname(surname.getModelObjectAsString());
			user.setPassword(password.getModelObjectAsString());

			
			//user.setGender(gender.getModelValue());	
			user.setAdmin(false);

			return user;
		}
		
		protected void onSubmit() {
			//userController = new RegisteredUserController();
			String passwordCheck = password.getModelObjectAsString();
			String passwordAgainCheck =passwordAgain.getModelObjectAsString();
			
			if (checkPassword(passwordCheck,passwordAgainCheck)){
			RegisteredUser user= createNewUser();
			user.setName(name.getModelObjectAsString());
			user.setSurname(surname.getModelObjectAsString());
			user.setLogin(login.getModelObjectAsString());
			user.setPassword(password.getModelObjectAsString());
			user.setEmail(email.getModelObjectAsString());
			
			user.setRegion(region.getModelObjectAsString());
			user.setTown(town.getModelObjectAsString());
			user.setHandicapType(handicapType.getModelObjectAsString());
			user.setPreferRegion(Boolean.valueOf(preferRegion.getModelObjectAsString()));
			user.setAdmin(false);
			user.setState(true);
			if (userController.saveNewUser(user)) feedbackPanel.info("Úspešne zaregistrovaný");
			else feedbackPanel.warn("Problem occured");
			
			userController = null;
			} 
		}
		
	}
}
