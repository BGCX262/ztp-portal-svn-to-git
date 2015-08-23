package sk.stuba.fiit.ztpPortal.core;

import java.util.Arrays;
import java.util.List;

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

import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.HandicapTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class Registration extends CorePage{
	
	private static final long serialVersionUID = 1L;
	
	private RegisteredUserController userController;
	private CmsContentController cmsController;
	private CmsContent cmsContent;
	private String login;
	private RegisteredUser user;

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
		  
	     setPageHeaderPanel();  
    
	     cmsContent = cmsController.getContentByName("registration");
		 add(new Label("cmsContent",cmsContent.getContent()).setEscapeModelStrings(false));
	        
		 add(new RegistrationForm("registrationForm"));
	}
	
	private void setPageHeaderPanel(){
		HeaderPanel panel = new HeaderPanel("headerPanel", user);
		add(panel);
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
		TextField town;
		DropDownChoice county;
		DropDownChoice handicapType;
			
		private final List<String> COUNTY_LIST = Arrays.asList(new CountyController().getCountyNameList());
		
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
			name.setLabel(new Model("Vaöe meno")); 
			name.add(StringValidator.maximumLength(30));
			add(new Label("nameLabel", "Vaöe meno *"));
			add(new FormComponentFeedbackBorder("nameAsterix").add(name));
	
			surname = new TextField("surname", new PropertyModel(properties,
			"surname"));
			surname.setRequired(true);
			surname.add(StringValidator.maximumLength(50));
			add(surname);
			surname.setLabel(new Model("Vaöe priezvisko"));
			add(new Label("surnameLabel", "Vaöe priezvisko *"));
			add(new FormComponentFeedbackBorder("surnameAsterix").add(surname));
			
			email = new TextField("email", new PropertyModel(properties,
			"email"));
			email.setRequired(true);
			email.add(EmailAddressValidator.getInstance());
			add(email);
			email.setLabel(new Model("Emailov· adresa"));
			add(new Label("emailLabel", "Emailov· adresa *"));
			add(new FormComponentFeedbackBorder("emailAsterix").add(email));
			
			login = new TextField("login", new PropertyModel(properties,
					"login"));
			login.setRequired(true);
			login.add(StringValidator.maximumLength(10));
			add(login);
			login.setLabel(new Model("Prihlasovacie meno"));
			add(new Label("loginLabel", "Prihlasovacie meno *"));
			add(new FormComponentFeedbackBorder("loginAsterix").add(login));

			town= new TextField("town", new PropertyModel(properties,
					"town"));
			town.setRequired(false);
			add(town);
			add(new Label("townLabel","Mesto bydliska"));
			
			county= new DropDownChoice("county", new PropertyModel(properties,
					"county"),COUNTY_LIST);
			county.setRequired(false);
			county.setLabel(new Model("Okres bydliska"));
			add(county);
			add(new Label("countyLabel","Okres bydliska"));
			
			handicapType= new DropDownChoice("handicapType", new PropertyModel(properties,
					"handicapType"),HANDICAPTYPE_LIST);
			handicapType.setRequired(false);
			add(handicapType);
			add(new Label("handicapTypeLabel","Typ postihnutia"));
			
			preferRegion = new CheckBox("preferRegion", new PropertyModel(properties,
					"preferRegion"));
			add(preferRegion);
			add(new Label("preferRegionLabel","Ur˝chlenÈ vyhæad·vanie"));
			
			password = new PasswordTextField("password", new PropertyModel(
					properties, "password"));
			password.setRequired(true);
			password.add(StringValidator.minimumLength(5));
			add(password);
			password.setLabel(new Model("PrÌstupovÈ heslo"));
			add(new Label("passwordLabel", "PrÌstupovÈ heslo *"));
			add(new FormComponentFeedbackBorder("passwordAsterix").add(password));

			passwordAgain = new PasswordTextField("passwordAgain", new PropertyModel(properties,
			"passwordAgain"));
			passwordAgain.setRequired(true);
			add(passwordAgain);
			passwordAgain.setLabel(new Model("Heslo zopakovaù"));
			add(new Label("passwordAgainLabel", "Heslo zopakovaù *"));
			add(new FormComponentFeedbackBorder("passwordAgainAsterix").add(passwordAgain));
			
			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}
		
		private boolean checkPassword(String password, String passwordAgain){
			return password.equals(passwordAgain);
		}
		
		private boolean checkEmail(String email){
			return userController.isEmailRegistered(email);
		}
		
		private boolean checkLogin(String login){
			return userController.isRegisteredUser(login);
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

			if (!checkEmail(email.getModelObjectAsString())) {
				feedbackPanel.info("Zadan˝ email bol uû pri registr·cii pouûit˝. Zvoæte si prosÌm in˝.");
				return;
			}
			
			if (checkLogin(login.getModelObjectAsString())) {
				feedbackPanel.info("Zadan˝ login bol uû pri registr·cii pouûit˝. Zvoæte si prosÌm in˝.");
				return;
			}
			
			String passwordCheck = password.getModelObjectAsString();
			String passwordAgainCheck =passwordAgain.getModelObjectAsString();
			
			if (checkPassword(passwordCheck,passwordAgainCheck)){
				RegisteredUser user= createNewUser();
				user.setName(name.getModelObjectAsString());
				user.setSurname(surname.getModelObjectAsString());
				user.setLogin(login.getModelObjectAsString());
				user.setPassword(password.getModelObjectAsString());
				user.setEmail(email.getModelObjectAsString());
				
				user.setTown(town.getModelObjectAsString());
				user.setCounty(new CountyController().getCountyByName(county.getModelObjectAsString()));
				user.setHandicapType(new HandicapTypeController().getHandicapByName(handicapType.getModelObjectAsString()));
				user.setPreferRegion(Boolean.valueOf(preferRegion.getModelObjectAsString()));
				user.setAdmin(false);
				user.setState(true);
				if (userController.saveNewUser(user)) feedbackPanel.info("Boli ste ˙speöne zaregistrovan˝, Ôakujeme V·m. Teraz sa mÙûete prihl·siù.");
				else feedbackPanel.warn("ProblÈm pri registr·cii, kontaktujte administr·tora");
				
				userController = null;
			} else {
				feedbackPanel.info("KontrolnÈ prÌstupovÈ heslo nie je rovnakÈ.");
			}
		}
		
	}
}
