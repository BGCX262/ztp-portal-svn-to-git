package sk.stuba.fiit.ztpPortal.core;

import java.util.Arrays;
import java.util.List;

import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.HandicapTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

public class Registration extends CorePage{
	
	private static final long serialVersionUID = 1L;
	
	private RegisteredUserController userController;
	private CmsContentController cmsController;
	private CmsContent cmsContent;
	private String login;
	private RegisteredUser user;
	
	private PageRenderer pageRenderer;
	private static CoreFeedBackPanel feedbackPanel;
	
	public Registration() {
		// nastavim CSS
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		cmsController = new CmsContentController();
		
		if (login!=null)
			user =userController.getRegisteredUserByLogin(login);
		else user = null;
	
		 
		feedbackPanel = new CoreFeedBackPanel("feedback");
	        add(feedbackPanel);
		 
	        
	     pageRenderer = new PageRenderer();   
	     setPageHeaderPanel();  
	     setPageNavigationPanel();
	     
	     add(pageRenderer.getSearchForm());
	     
	     cmsContent = cmsController.getContentByName("registration");
		 add(new Label("cmsContent",cmsContent.getContent()).setEscapeModelStrings(false));
	        
		 add(new RegistrationForm("registrationForm"));
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
	
	public final class RegistrationForm extends Form {

		private static final long serialVersionUID = 1L;
		
		TextField name;
		TextField surname;
		TextField login;
		TextField password;
		TextField passwordAgain;
		TextField email;
		Button submit;
//		DropDownChoice region;
		DropDownChoice town;
		DropDownChoice handicapType;
		
		//private final List REGION_LIST = Arrays.asList(new String[] {"The Server Side", "Java Lobby", "Java.Net" });
	
		private final List<String> TOWN_LIST = Arrays.asList(new CountyController().getCountyNameList());
		
		private final List<String> HANDICAPTYPE_LIST = Arrays.asList(new HandicapTypeController().getHandicapNameList());
		
		CheckBox preferRegion;
		
		private ValueMap properties = new ValueMap();

		//private FormComponentFeedbackBorder borderAsterix = new FormComponentFeedbackBorder("borderAsterix");
		
		public RegistrationForm(final String id) {
			super(id);
			
			name = new TextField("name", new PropertyModel(properties,
			"name"));
			name.setRequired(true);
			add(name);
			name.setLabel(new Model("Va�e meno")); 
			name.add(StringValidator.maximumLength(30));
			add(new Label("nameLabel", "Va�e meno"));
			add(new FormComponentFeedbackBorder("nameAsterix").add(name));
	
			surname = new TextField("surname", new PropertyModel(properties,
			"surname"));
			surname.setRequired(true);
			surname.add(StringValidator.maximumLength(50));
			add(surname);
			surname.setLabel(new Model("Va�e priezvisko"));
			add(new Label("surnameLabel", "Va�e priezvisko"));
			add(new FormComponentFeedbackBorder("surnameAsterix").add(surname));
			
			email = new TextField("email", new PropertyModel(properties,
			"email"));
			email.setRequired(true);
			email.add(EmailAddressValidator.getInstance());
			add(email);
			email.setLabel(new Model("Emailov� adresa"));
			add(new Label("emailLabel", "Emailov� adresa"));
			add(new FormComponentFeedbackBorder("emailAsterix").add(email));
			
			login = new TextField("login", new PropertyModel(properties,
					"login"));
			login.setRequired(true);
			login.add(StringValidator.maximumLength(10));
			add(login);
			login.setLabel(new Model("Prihlasovacie meno"));
			add(new Label("loginLabel", "Prihlasovacie meno"));
			add(new FormComponentFeedbackBorder("loginAsterix").add(login));

//			region= new DropDownChoice("region", new PropertyModel(properties,
//					"region"),REGION_LIST);
//			region.setRequired(false);
//			add(region);
//			add(new Label("regionLabel","Region"));
			
			town= new DropDownChoice("town", new PropertyModel(properties,
					"town"),TOWN_LIST);
			town.setRequired(false);
			town.setLabel(new Model("Miesto bydliska"));
			add(town);
			add(new Label("townLabel","Miesto bydliska"));
			
			handicapType= new DropDownChoice("handicapType", new PropertyModel(properties,
					"handicapType"),HANDICAPTYPE_LIST);
			handicapType.setRequired(false);
			add(handicapType);
			add(new Label("handicapTypeLabel","Typ postihnutia"));
			
			preferRegion = new CheckBox("preferRegion", new PropertyModel(properties,
					"preferRegion"));
			add(preferRegion);
			add(new Label("preferRegionLabel","Ur�chlen� vyh�ad�vanie"));
			
			password = new PasswordTextField("password", new PropertyModel(
					properties, "password"));
			password.setRequired(true);
			password.add(StringValidator.minimumLength(5));
			add(password);
			password.setLabel(new Model("Pr�stupov� heslo"));
			add(new Label("passwordLabel", "Pr�stupov� heslo"));
			add(new FormComponentFeedbackBorder("passwordAsterix").add(password));

			passwordAgain = new PasswordTextField("passwordAgain", new PropertyModel(properties,
			"passwordAgain"));
			passwordAgain.setRequired(true);
			add(passwordAgain);
			passwordAgain.setLabel(new Model("Heslo zopakova�"));
			add(new Label("passwordAgainLabel", "Heslo zopakova�"));
			add(new FormComponentFeedbackBorder("passwordAgainAsterix").add(passwordAgain));
			
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
				
	//user.setRegion(region.getModelObjectAsString());
				user.setTown(new CountyController().getCountyByName(town.getModelObjectAsString()));
				user.setHandicapType(new HandicapTypeController().getHandicapByName(handicapType.getModelObjectAsString()));
				user.setPreferRegion(Boolean.valueOf(preferRegion.getModelObjectAsString()));
				user.setAdmin(false);
				user.setState(true);
				if (userController.saveNewUser(user)) feedbackPanel.info("�spe�ne zaregistrovan�");
				else feedbackPanel.warn("Problem occured");
				
				userController = null;
			} else {
				feedbackPanel.info("Kontroln� pr�stupov� heslo nie je rovnak�.");
			}
		}
		
	}
}
