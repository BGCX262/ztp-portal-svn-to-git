package sk.stuba.fiit.ztpPortal.module.education;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
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
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.NumberValidator;
import org.apache.wicket.validation.validator.StringValidator;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.core.RegionSelector;
import sk.stuba.fiit.ztpPortal.core.RegionSelectorView;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.CourseController;
import sk.stuba.fiit.ztpPortal.databaseController.CourseTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Course;
import sk.stuba.fiit.ztpPortal.databaseModel.CourseType;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class CourseViewDetail extends CorePage{
	
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private Course course;
	
	public CourseViewDetail(long courseId){
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		
		
		if (login != null){
			user = userController.getRegisteredUserByLogin(login);
		}
		else
			user = null;

		course = new CourseController().getCourseByCourseId(courseId);
		
		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();
		setPageLeftNavigation();
		
		// formular vlozim
		
		add(new CourseViewDetailForm("courseViewDetailForm"));
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
		Link schoolDetailLink = new Link("newSchoolLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new SchoolDetail());
			}
		};
		add(schoolDetailLink);
		
		Link courseDetailLink = new Link("newCourseLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new CourseDetail());
			}
		};
		add(courseDetailLink);
	}
	
	public final class CourseViewDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		private DateTextField dateTextField;
		private TextField startDate;
		private TextField name;
		private TextField address;
		private TextField town;
		private TextField contactPerson;
		private TextField emailContact;
		private TextField phone;
		private TextField price;
		private TextField length;
		DropDownChoice country;
		DropDownChoice county;
		
		private DropDownChoice courseType;

		private TextArea textArea;

		private TinyMceBehavior tinyMCE;
		private CheckBox active;

		private long actualDate = new Date().getTime();
		private long activeDateEnd=course.getCreateDate().getTime()+5184000;

		private final List<String> COURSETYPE_LIST = Arrays
				.asList(new CourseTypeController().getCourseTypeNameList());

		private Button submit;
		private Button reactivate;
		private ValueMap properties = new ValueMap();

		private String[] value = new String[1];
		
		private RegionSelectorView regionSelector;
		private RegionSelector region;

		private	IValidator numberValidate = NumberValidator.minimum(0);
		
		//private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		private Date courseStartDate;
		
		public Date getCourseStartDate(){
			return course.getStartDate();
		}
		
		public void setCourseStartDate(Date startDate){
			this.courseStartDate = startDate;
		}
		
		public CourseViewDetailForm(final String id) {
			super(id);

			CourseType typKurzu = new CourseType();
			courseType = new DropDownChoice("courseType", new PropertyModel(
					typKurzu, "name"), COURSETYPE_LIST);
			typKurzu.setName(course.getCourseType().getName());
			courseType.setRequired(true);
			courseType.setLabel(new Model("Typ kurzu"));
			add(courseType);
			add(new Label("courseTypeLabel", "Typ kurzu *"));
			add(new FormComponentFeedbackBorder("courseTypeAsterix").add(courseType));

			name = new TextField("name", new PropertyModel(
					properties, "name"));
			name.setRequired(true);
			name.setLabel(new Model("Názov"));
			name.add(StringValidator.maximumLength(30));
			value[0] = String.valueOf(course.getName());
			name.setModelValue(value);
			add(name);
			add(new Label("nameLabel", "Názov *"));
			add(new FormComponentFeedbackBorder("nameAsterix").add(name));
			
			contactPerson = new TextField("contactPerson", new PropertyModel(
					properties, "contactPerson"));
			contactPerson.setRequired(true);
			contactPerson.setLabel(new Model("Kontaktná osoba"));
			contactPerson.add(StringValidator.maximumLength(30));
			value[0] = String.valueOf(course.getContactPerson());
			contactPerson.setModelValue(value);
			add(contactPerson);
			add(new Label("contactPersonLabel", "Kontaktná osoba *"));
			add(new FormComponentFeedbackBorder("contactPersonAsterix").add(contactPerson));
			
			emailContact = new TextField("emailContact", new PropertyModel(
					properties, "emailContact"));
			emailContact.add(EmailAddressValidator.getInstance());
			emailContact.setLabel(new Model("Email"));
			value[0] = String.valueOf(course.getEmail());
			emailContact.setModelValue(value);
			add(emailContact);
			add(new Label("emailContactLabel", "Email"));

			phone = new TextField("phone", new PropertyModel(
					properties, "phone"));
			phone.setLabel(new Model("Telefón"));
			phone.add(StringValidator.maximumLength(30));
			value[0] = String.valueOf(course.getPhone());
			phone.setModelValue(value);
			add(phone);
			add(new Label("phoneLabel", "Telefón"));

			price = new TextField("price", new PropertyModel(
					properties, "price"), Float.class);
			price.setRequired(true);
			price.setLabel(new Model("Cena"));
			price.add(numberValidate);
			value[0] = String.valueOf(course.getPrice());
			price.setModelValue(value);
			add(price);
			add(new Label("priceLabel", "Cena *"));
			
			dateTextField = new DateTextField("startDate", new PropertyModel(
					this, "courseStartDate"), "dd.MM.yyyy");
			dateTextField.add(new DatePicker());
			dateTextField.setRequired(true);
			dateTextField.setLabel(new Model("Zaèiatok *"));
			add(dateTextField);
			add(new Label("startDateLabel", "Zaèiatok *"));
			
			length = new TextField("length", new PropertyModel(
					properties, "length"), Float.class);
			length.setRequired(true);
			length.setLabel(new Model("Trvanie"));
			length.add(numberValidate);
			value[0] = String.valueOf(course.getLength());
			length.setModelValue(value);
			add(length);
			add(new Label("lengthLabel", "Trvanie *"));
			add(new FormComponentFeedbackBorder("lengthAsterix").add(length));
			
			address = new TextField("address", new PropertyModel(
					properties, "address"));
			address.setRequired(true);
			address.setLabel(new Model("Adresa"));
			value[0] = String.valueOf(course.getAddress());
			address.setModelValue(value);
			add(address);
			add(new Label("addressLabel", "Adresa *"));
			add(new FormComponentFeedbackBorder("addressAsterix").add(address));

			if (course.getCounty() != null) {
				regionSelector = new RegionSelectorView(course.getCounty());
				add(regionSelector.getCountryDropDownChoice());
				add(regionSelector.getCountyDropDownChoice());
			} else {
				region = RegionSelector.getSingletonObject();
				add(region.getCountryDropDownChoice());
				add(region.getCountyDropDownChoice());
			}
			add(new Label("countryLabel", "Kraj *"));
			add(new Label("countyLabel", "Okres"));
			
			town = new TextField("town", new PropertyModel(properties,
					"town"));
			town.setRequired(true);
			town.setLabel(new Model("Mesto"));
			value[0] = String.valueOf(course.getTown());
			town.setModelValue(value);
			add(town);
			add(new Label("townLabel", "Mesto *"));
			add(new FormComponentFeedbackBorder("townAsterix").add(town));
			
			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.add(StringValidator.maximumLength(5000));
			textArea.setEscapeModelStrings(false);
			add(textArea);

			value[0]=String.valueOf(course.isActive());
			active = new CheckBox("active", new PropertyModel(properties,"active"));
			active.setModelValue(value);
			add(active);
			add(new Label("activeLabel","Aktívny"));
			
			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
			
			add(reactivate = new Button("reactivate",
					new ResourceModel("button.submit")));
			
			if (activeDateEnd>actualDate || !course.isActive())
				submit.setVisible(false);
			else
				reactivate.setVisible(false);
		}

		protected void onSubmit() {
			try {
				String datum = dateTextField.getModelObjectAsString();
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				Date dat = null;
				dateFormat.parse(datum);
				dat = dateFormat.parse(datum);

				java.sql.Date date = new java.sql.Date(dat.getTime());
								
				boolean validator = true;
				
				if (date.getTime()<new Date().getTime()) {
					feedbackPanel.warn("Dátum môže by najskôr nasledujúci deò.");
					validator = false;
				}
				
				if (validator){
				
				course.setStartDate(date);
				course.setName(name.getModelObjectAsString());
				course.setContactPerson(contactPerson.getModelObjectAsString());
				course.setPhone(phone.getModelObjectAsString());
				course.setEmail(emailContact.getModelObjectAsString());
				course.setPrice(Integer.parseInt(price.getModelObjectAsString()));
				course.setLength(Integer.parseInt(length.getModelObjectAsString()));
				course.setTown(town.getModelObjectAsString());
				course.setCmsContent(textArea.getValue());
				
				course.setCourseType(new CourseTypeController()
						.getCourseTypeByName(courseType
								.getModelObjectAsString()));
				if (course.getCounty()!=null)
					course.setCounty(new CountyController().getCountyByName(regionSelector.getMyCounty()));
				else 
					course.setCounty(new CountyController().getCountyByName(region.getMyCounty()));
				course.setAddress(address.getModelObjectAsString());
				
				// not-user entered data
				course.setActive(Boolean.valueOf(active.getModelObjectAsString()));

				CourseController courseController = new CourseController();

				if (courseController.updateCourse(course)){
					submit.setVisible(true);
					reactivate.setVisible(false);
					feedbackPanel.info("Inzerát úspešne aktualizovaný");
				}
				else
					feedbackPanel.warn("Chyba pri aktualizácii");
				
				}
			} catch (NumberFormatException e) {
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	

}

