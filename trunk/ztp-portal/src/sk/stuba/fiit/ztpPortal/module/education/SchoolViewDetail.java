package sk.stuba.fiit.ztpPortal.module.education;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
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
import sk.stuba.fiit.ztpPortal.core.RegionSelector;
import sk.stuba.fiit.ztpPortal.core.RegionSelectorView;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.SchoolController;
import sk.stuba.fiit.ztpPortal.databaseController.SchoolTypeController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.School;
import sk.stuba.fiit.ztpPortal.databaseModel.SchoolType;
import wicket.contrib.tinymce.TinyMceBehavior;

public class SchoolViewDetail extends CorePage{
	
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private School school;
	
	public SchoolViewDetail(long schoolId){
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		
		if (login != null){
			user = userController.getRegisteredUserByLogin(login);
		}
		else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();
		setPageLeftNavigation();
		
		school = new SchoolController().getSchoolBySchoolId(schoolId);
		
		// formular vlozim
		
		add(new SchoolViewDetailForm("schoolViewDetailForm"));
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
	
	public final class SchoolViewDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		private TextField name;
		private TextField address;
		private TextField town;
		private TextField contactPerson;
		private TextField emailContact;
		private TextField phone;
		private TextField homePage;
		
		private long actualDate = new Date().getTime();
		private long activeDateEnd=school.getCreateDate().getTime()+5184000;
		
		private DropDownChoice schoolType;

		private TextArea textArea;
		private CheckBox active;

		private TinyMceBehavior tinyMCE;
		
		private String[] value = new String[1];

		private final List<String> SCHOOLTYPE_LIST = Arrays
				.asList(new SchoolTypeController().getSchoolTypeNameList());

		private Button submit;
		private Button reactivate;
		private ValueMap properties = new ValueMap();

		private RegionSelectorView regionSelector;
		private RegionSelector region;

		public SchoolViewDetailForm(final String id) {
			super(id);

			SchoolType typSkola = new SchoolType();
			schoolType = new DropDownChoice("schoolType", new PropertyModel(
					typSkola, "name"), SCHOOLTYPE_LIST);
			typSkola.setName(school.getSchoolType().getName());
			schoolType.setRequired(true);
			schoolType.setLabel(new Model("Typ zariadenia"));
			add(schoolType);
			add(new Label("schoolTypeLabel", "Typ zariadenia *"));
			add(new FormComponentFeedbackBorder("schoolTypeAsterix").add(schoolType));

			name = new TextField("schoolName", new PropertyModel(
					properties, "schoolName"));
			name.setRequired(true);
			value[0] = String.valueOf(school.getName());
			name.setModelValue(value);
			name.setLabel(new Model("Názov"));
			name.add(StringValidator.maximumLength(30));
			add(name);
			add(new Label("schoolNameLabel", "Názov *"));
			add(new FormComponentFeedbackBorder("nameAsterix").add(name));
			
			contactPerson = new TextField("schoolContactPerson", new PropertyModel(
					properties, "schoolContactPerson"));
			value[0] = String.valueOf(school.getContactPerson());
			contactPerson.setModelValue(value);
			contactPerson.setRequired(true);
			contactPerson.setLabel(new Model("Kontaktná osoba"));
			contactPerson.add(StringValidator.maximumLength(30));
			add(contactPerson);
			add(new Label("schoolContactPersonLabel", "Kontaktná osoba *"));
			add(new FormComponentFeedbackBorder("contactPersonAsterix").add(contactPerson));
			
			emailContact = new TextField("schoolEmailContact", new PropertyModel(
					properties, "schoolEmailContact"));
			value[0] = String.valueOf(school.getEmail());
			emailContact.setModelValue(value);
			emailContact.add(EmailAddressValidator.getInstance());
			emailContact.setLabel(new Model("Email"));
			add(emailContact);
			add(new Label("schoolEmailContactLabel", "Email"));
			add(new FormComponentFeedbackBorder("emailContactAsterix").add(emailContact));

			phone = new TextField("schoolPhone", new PropertyModel(
					properties, "schoolPhone"));
			value[0] = String.valueOf(school.getPhone());
			phone.setModelValue(value);
			phone.setLabel(new Model("Email"));
			phone.add(StringValidator.maximumLength(30));
			add(phone);
			add(new Label("schoolPhoneLabel", "Telefónny kontakt"));

			homePage = new TextField("schoolHomePage", new PropertyModel(
					properties, "schoolHomePage"));
			value[0] = String.valueOf(school.getHomePage());
			homePage.setModelValue(value);
			homePage.setLabel(new Model("Stránka"));
			homePage.add(StringValidator.maximumLength(30));
			add(homePage);
			add(new Label("schoolHomePageLabel", "Stránka"));
			
			address = new TextField("schoolAddress", new PropertyModel(
					properties, "schoolAddress"));
			value[0] = String.valueOf(school.getAddress());
			address.setModelValue(value);
			address.setRequired(true);
			address.setLabel(new Model("Adresa"));
			add(address);
			add(new Label("schoolAddressLabel", "Adresa *"));
			add(new FormComponentFeedbackBorder("addressAsterix").add(address));

			if (school.getCounty() != null) {
				regionSelector = new RegionSelectorView(school.getCounty());
				add(regionSelector.getCountryDropDownChoice());
				add(regionSelector.getCountyDropDownChoice());
			} else {
				region = RegionSelector.getSingletonObject();
				add(region.getCountryDropDownChoice());
				add(region.getCountyDropDownChoice());
			}
			add(new Label("countryLabel", "Kraj *"));
			add(new Label("countyLabel", "Okres"));
			
			town = new TextField("schoolTown", new PropertyModel(properties,
					"schoolTown"));
			value[0] = String.valueOf(school.getTown());
			town.setModelValue(value);
			town.setRequired(true);
			town.setLabel(new Model("Mesto"));
			add(town);
			add(new Label("schoolTownLabel", "Mesto *"));
			add(new FormComponentFeedbackBorder("townAsterix").add(town));
			
			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.add(StringValidator.maximumLength(5000));
			textArea.setEscapeModelStrings(false);
			add(textArea);
			
			value[0]=String.valueOf(school.isActive());
			active = new CheckBox("active", new PropertyModel(properties,"active"));
			active.setModelValue(value);
			add(active);
			add(new Label("activeLabel","Aktívny"));

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
			
			add(reactivate = new Button("reactivate",
					new ResourceModel("button.submit")));
			
			if (activeDateEnd>actualDate || !school.isActive())
				submit.setVisible(false);
			else
				reactivate.setVisible(false);
		}

		protected void onSubmit() {
			try {
				school.setName(name.getModelObjectAsString());
				school.setContactPerson(contactPerson.getModelObjectAsString());
				school.setPhone(phone.getModelObjectAsString());
				school.setEmail(emailContact.getModelObjectAsString());
				school.setHomePage(homePage.getModelObjectAsString());
				school.setTown(town.getModelObjectAsString());
				
				school.setSchoolType(new SchoolTypeController()
						.getSchoolTypeByName(schoolType
								.getModelObjectAsString()));
				if (school.getCounty()!=null)
					school.setCounty(new CountyController().getCountyByName(regionSelector.getMyCounty()));
				else 
					school.setCounty(new CountyController().getCountyByName(region.getMyCounty()));
				school.setAddress(address.getModelObjectAsString());
				school.setNote(textArea.getValue());

				// not-user entered data
				school.setActive(Boolean.valueOf(active.getModelObjectAsString()));

				SchoolController schoolController = new SchoolController();

				if (schoolController.updateSchool(school)){
					submit.setVisible(false);	
					feedbackPanel.info("Inzerát úspešne vložený");
				}
				else
					feedbackPanel.warn("Chyba pri vkladaní");
				

			} catch (NumberFormatException e) {
				
			}
		}

	}
	

}
