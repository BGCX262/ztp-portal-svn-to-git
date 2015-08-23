package sk.stuba.fiit.ztpPortal.module.education;

import java.sql.BatchUpdateException;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.core.PageNoAdmitance;
import sk.stuba.fiit.ztpPortal.core.RegionSelector;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.SchoolController;
import sk.stuba.fiit.ztpPortal.databaseController.SchoolTypeController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.School;
import wicket.contrib.tinymce.TinyMceBehavior;

public class SchoolDetail extends CorePage{
	
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	
	public SchoolDetail(){
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		
		if (login != null){
			user = userController.getRegisteredUserByLogin(login);
		}
		else
			throw new RestartResponseAtInterceptPageException(
				      new PageNoAdmitance(this));

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();
		setPageLeftNavigation();
		
		// formular vlozim
		
		add(new SchoolDetailForm("schoolDetailForm"));
	}
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}
	
	private void setPageLeftNavigation() {
		Link eventDetailLink = new Link("schoolListLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new EducationList());
			}
		};
		add(eventDetailLink);
	}
	
	public final class SchoolDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		private TextField name;
		private TextField address;
		private TextField town;
		private TextField contactPerson;
		private TextField emailContact;
		private TextField phone;
		private TextField homePage;
		private DropDownChoice country;
		private DropDownChoice county;
		
		private DropDownChoice schoolType;

		private TextArea textArea;

		private TinyMceBehavior tinyMCE;

		private final List<String> SCHOOLTYPE_LIST = Arrays
				.asList(new SchoolTypeController().getSchoolTypeNameList());

		private Button submit;
		private ValueMap properties = new ValueMap();

		RegionSelector region = RegionSelector.getSingletonObject();

		public SchoolDetailForm(final String id) {
			super(id);

			schoolType = new DropDownChoice("schoolType", new PropertyModel(
					properties, "schoolType"), SCHOOLTYPE_LIST);
			schoolType.setRequired(true);
			schoolType.setLabel(new Model("Typ zariadenia"));
			add(schoolType);
			add(new Label("schoolTypeLabel", "Typ zariadenia *"));
			add(new FormComponentFeedbackBorder("schoolTypeAsterix").add(schoolType));

			name = new TextField("schoolName", new PropertyModel(
					properties, "schoolName"));
			name.setRequired(true);
			name.setLabel(new Model("Názov"));
			name.add(StringValidator.maximumLength(30));
			add(name);
			add(new Label("schoolNameLabel", "Názov *"));
			add(new FormComponentFeedbackBorder("nameAsterix").add(name));
			
			contactPerson = new TextField("schoolContactPerson", new PropertyModel(
					properties, "schoolContactPerson"));
			contactPerson.setRequired(true);
			contactPerson.setLabel(new Model("Kontaktná osoba"));
			contactPerson.add(StringValidator.maximumLength(30));
			add(contactPerson);
			add(new Label("schoolContactPersonLabel", "Kontaktná osoba *"));
			add(new FormComponentFeedbackBorder("contactPersonAsterix").add(contactPerson));
			
			emailContact = new TextField("schoolEmailContact", new PropertyModel(
					properties, "schoolEmailContact"));
			emailContact.add(EmailAddressValidator.getInstance());
			emailContact.setLabel(new Model("Email"));
			emailContact.add(StringValidator.maximumLength(30));
			add(emailContact);
			add(new Label("schoolEmailContactLabel", "Email"));
			add(new FormComponentFeedbackBorder("emailContactAsterix").add(emailContact));

			phone = new TextField("schoolPhone", new PropertyModel(
					properties, "schoolPhone"));
			phone.setLabel(new Model("Email"));
			phone.add(StringValidator.maximumLength(30));
			add(phone);
			add(new Label("schoolPhoneLabel", "Telefónny kontakt"));

			homePage = new TextField("schoolHomePage", new PropertyModel(
					properties, "schoolHomePage"));
			homePage.setLabel(new Model("Stránka"));
			homePage.add(StringValidator.maximumLength(30));
			add(homePage);
			add(new Label("schoolHomePageLabel", "Stránka"));
			
			address = new TextField("schoolAddress", new PropertyModel(
					properties, "schoolAddress"));
			address.setRequired(true);
			address.setLabel(new Model("Adresa"));
			address.add(StringValidator.maximumLength(30));
			add(address);
			add(new Label("schoolAddressLabel", "Adresa *"));
			add(new FormComponentFeedbackBorder("addressAsterix").add(address));

			////
			
			country=region.getCountryDropDownChoice();
			county=region.getCountyDropDownChoice();
			
			add(country);
			add(county);
			
			add(new Label("countryLabel", "Kraj *"));
			add(new Label("countyLabel", "Okres"));
			/////
			
			town = new TextField("schoolTown", new PropertyModel(properties,
					"schoolTown"));
			town.setRequired(true);
			town.setLabel(new Model("Mesto"));
			town.add(StringValidator.maximumLength(30));
			add(town);
			add(new Label("schoolTownLabel", "Mesto *"));
			add(new FormComponentFeedbackBorder("townAsterix").add(town));
			
			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.setEscapeModelStrings(false);
			textArea.add(StringValidator.maximumLength(5000));
			add(textArea);

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}

		protected void onSubmit() {
			try {
				
				School school = new School();
				school.setName(name.getModelObjectAsString());
				school.setContactPerson(contactPerson.getModelObjectAsString());
				school.setPhone(phone.getModelObjectAsString());
				school.setEmail(emailContact.getModelObjectAsString());
				school.setHomePage(homePage.getModelObjectAsString());
				school.setTown(town.getModelObjectAsString());
				
				school.setSchoolType(new SchoolTypeController()
						.getSchoolTypeByName(schoolType
								.getModelObjectAsString()));
				school.setCounty(new CountyController().getCountyByName(region.getMyCounty()));
				school.setAddress(address.getModelObjectAsString());
				school.setNote(textArea.getValue());

				// not-user entered data
				school.setState(true);
				school.setActive(true);
				school.setOwner(user);

				SchoolController schoolController = new SchoolController();

				if (schoolController.saveNewSchool(school)){
					submit.setVisible(false);	
					feedbackPanel.info("Inzerát úspešne vložený");
				}
				else
					feedbackPanel.warn("Chyba pri vkladaní");
				

			} catch (BatchUpdateException e){
				System.out.println(e.getMessage());
				System.out.println(e.getNextException());
			} catch (NumberFormatException e) {
				
			}
		}

	}
	

}
