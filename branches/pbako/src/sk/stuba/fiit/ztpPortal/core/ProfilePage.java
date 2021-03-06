package sk.stuba.fiit.ztpPortal.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import sk.stuba.fiit.ztpPortal.databaseController.HandicapTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseModel.HandicapType;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.County;

import org.apache.wicket.behavior.HeaderContributor;
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

public class ProfilePage extends CorePage {

	private static final long serialVersionUID = 1L;
	
	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	
	private PageRenderer pageRenderer;
	
	private static CoreFeedBackPanel feedbackPanel;
	
	public ProfilePage() {
		// nastavim CSS
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		
		if (login!=null)
			user =userController.getRegisteredUserByLogin(login);
		else user = null;

		pageRenderer = new PageRenderer();
		setPageHeaderPanel();
		setPageNavigationPanel();
		
		add(pageRenderer.getSearchForm());
		
		add(new UserDetailForm("userDetailForm"));
		
		feedbackPanel = new CoreFeedBackPanel("feedback");
        add(feedbackPanel);
	}
	
	private void setPageHeaderPanel(){
		pageRenderer.setUser(user);
		Form loginForm = pageRenderer.getLoginForm();
		add(loginForm);
		Form statusForm = pageRenderer.getStatusForm();
		add(statusForm);
		
		if (user==null) pageRenderer.disableStatusForm();
		else pageRenderer.disableLoginForm();
	}
	
	private void setPageNavigationPanel(){
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());	
	}
	
	  public final class UserDetailForm extends Form {

	 		private static final long serialVersionUID = 1L;
	 		
	 		TextField name;
	 		TextField surname;
	 		TextField login;
	 		TextField password;
	 		TextField passwordAgain;
	 		TextField email;
	 		Button submit;
//	 		TextField region;
	 		DropDownChoice town;
	 		DropDownChoice handicapType;
	 		
//	 		private static final List REGION_LIST = Arrays.asList(new String[] {"The Server Side", "Java Lobby", "Java.Net" });
	 		
	 		private final List<String> HANDICAPTYPE_LIST = Arrays.asList(new HandicapTypeController().getHandicapNameList());
	 		
	 		private final List<String> TOWN_LIST = Arrays.asList(new CountyController().getCountyNameList());
	 			 		
	 		CheckBox preferRegion;
	 		CheckBox status;
	 		
//	 		ComponentFeedbackPanel loginNameFeedback;
//	 		ComponentFeedbackPanel passwordFeedback;
//	 		ComponentFeedbackPanel submitFeedback;
//	 		ComponentFeedbackPanel titleFeedback;
//	 		ComponentFeedbackPanel addressFeedback;
//	 		ComponentFeedbackPanel passwordAgainFeedback;
//	 		ComponentFeedbackPanel surnameFeedback;
//	 		ComponentFeedbackPanel nameFeedback;
	 		
	 		private ValueMap properties = new ValueMap();
	 
	 		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	 		
	 		public UserDetailForm(final String id) {
	 			super(id);
	 			
	 			
	 			name = new TextField("name", new PropertyModel(properties,
	 			"name"));
	 			name.setRequired(true);
	 			name.setModelValue(user.getName());
	 			add(name);
	 		//	add(nameFeedback = new ComponentFeedbackPanel("nameFeedback",
	 		//			name));
	 			add(new Label("nameLabel", "Meno"));
	 			add(new FormComponentFeedbackBorder("border").add(name));
	 	
	 			surname = new TextField("surname", new PropertyModel(properties,
	 			"surname"));
	 			surname.setRequired(true);
	 			surname.setModelValue(user.getSurname());
	 			add(surname);
	 		//	add(surnameFeedback = new ComponentFeedbackPanel("surnameFeedback",
	 		//		surname));
	 			add(new Label("surnameLabel", "Priezvisko"));
	 			
	 			email = new TextField("email", new PropertyModel(properties,
	 			"email"));
	 			email.setRequired(true);
	 			email.setModelValue(user.getEmail());
	 			email.setEnabled(false);
	 			add(email);
//	 			add(addressFeedback = new ComponentFeedbackPanel("addressFeedback",
//	 					address));
	 			add(new Label("emailLabel", "Email"));
	 			
	 			login = new TextField("login", new PropertyModel(properties,
	 					"login"));
	 			login.setRequired(true);
	 			login.setModelValue(user.getLogin());
	 			login.setEnabled(false);
	 			add(login);
//	 			add(loginNameFeedback = new ComponentFeedbackPanel("loginNameFeedback",
//	 					loginName));
	 			add(new Label("loginLabel", "Login"));

//	 			region= new TextField("region", new PropertyModel(properties,
//	 					"region"));
//	 			region.setRequired(false);
//	 			region.setModelValue(user.getRegion());
//	 			add(region);
//	 			add(new Label("regionLabel","Region"));
	 			

	 			
	 			County mesto = new County();	
	 			town= new DropDownChoice("town", new PropertyModel(mesto,
				"name"),TOWN_LIST);
	 			town.setRequired(false);
	 			if (user.getTown()!=null)
	 				mesto.setName(user.getTown().getName());
	 			add(town);
	 			add(new Label("townLabel","Town"));
	 			
	 			HandicapType postihnutie = new HandicapType();
	 			handicapType= new DropDownChoice("handicapType", new PropertyModel(postihnutie,
	 					"name"),HANDICAPTYPE_LIST);
	 			handicapType.setRequired(false);
	 			if (user.getHandicapType()!=null)
	 				postihnutie.setName(user.getHandicapType().getName());
	 			add(handicapType);
	 			add(new Label("handicapTypeLabel","Handicap"));
	 			
	 			preferRegion = new CheckBox("preferRegion", new PropertyModel(properties,
	 					"preferRegion"));
		
	 			add(preferRegion);
	 			add(new Label("preferRegionLabel","Preferujem"));
	 			
	 			
	 			add(new Label("registrationDateLabel","D�tum registr�cie:"));
	 			add(new Label("userRegistrationDateLabel",dateFormat.format(user.getRegistrationDate())));
	 			
	 			password = new PasswordTextField("password", new PropertyModel(
	 					properties, "password"));
	 			add(password);
	 			password.setRequired(false);
//	 			add(passwordFeedback = new ComponentFeedbackPanel(
//	 					"passwordFeedback", password));
	 			add(new Label("passwordLabel", "Heslo:"));

	 			passwordAgain = new PasswordTextField("passwordAgain", new PropertyModel(properties,
	 			"passwordAgain"));
	 			add(passwordAgain);
	 			passwordAgain.setRequired(false);
//	 			add(passwordAgainFeedback = new ComponentFeedbackPanel("passwordAgainFeedback",
//	 					passwordAgain));
	 			add(new Label("passwordAgainLabel", "Heslo znova"));
	 			
	 			
	 			add(submit = new Button("submit",
	 					new ResourceModel("button.submit")));
//	 			add(submitFeedback = new ComponentFeedbackPanel("submitFeedback",
//	 					submit));
	 		}
	 		
	 		private boolean checkPassword(String password, String passwordAgain){
	 			return password.equals(passwordAgain);
	 		}

	 		protected void onSubmit() {
	 			userController = new RegisteredUserController();
	 			String passwordCheck = password.getModelObjectAsString();
	 			String passwordAgainCheck =passwordAgain.getModelObjectAsString();
	 			
	 			if (passwordCheck!=null && passwordAgainCheck!=null && passwordCheck!="")
	 				if (checkPassword(passwordCheck,passwordAgainCheck)){

			 			user.setName(name.getModelObjectAsString());
						user.setSurname(surname.getModelObjectAsString());
						user.setLogin(login.getModelObjectAsString());
						user.setPassword(password.getModelObjectAsString());
						user.setEmail(email.getModelObjectAsString());
						
	//					user.setRegion(region.getModelObjectAsString());
						user.setTown(new CountyController().getCountyByName(town.getModelObjectAsString()));
						user.setHandicapType(new HandicapTypeController().getHandicapByName(handicapType.getModelObjectAsString()));
						user.setPreferRegion(Boolean.valueOf(preferRegion.getModelObjectAsString()));
	 			
			 			if (userController.updateUser(user)) feedbackPanel.info("�spe�ne ulo�en�");
			 			else feedbackPanel.warn("Problem occured");
			 			}
	 				else feedbackPanel.warn("Nerovnak� hesl�");
	 			else if  (passwordCheck=="" || passwordAgainCheck==""){
	 				user.setName(name.getModelObjectAsString());
					user.setSurname(surname.getModelObjectAsString());
					user.setLogin(login.getModelObjectAsString());
					user.setEmail(email.getModelObjectAsString());
					
	//				user.setRegion(region.getModelObjectAsString());
					user.setTown(new CountyController().getCountyByName(town.getModelObjectAsString()));
					user.setHandicapType(new HandicapTypeController().getHandicapByName(handicapType.getModelObjectAsString()));
					user.setPreferRegion(Boolean.valueOf(preferRegion.getModelObjectAsString()));
 			
		 			if (userController.updateUser(user)) feedbackPanel.info("�spe�ne ulo�en�");
		 			else feedbackPanel.warn("Problem occured");
	 			}
	 			userController = null;
	 		}
	}
	
}
