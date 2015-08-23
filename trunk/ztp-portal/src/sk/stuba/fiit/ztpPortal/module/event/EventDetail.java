package sk.stuba.fiit.ztpPortal.module.event;

import java.sql.BatchUpdateException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
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
import org.apache.wicket.validation.validator.StringValidator;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.core.PageNoAdmitance;
import sk.stuba.fiit.ztpPortal.core.RegionSelector;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.EventController;
import sk.stuba.fiit.ztpPortal.databaseController.EventTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class EventDetail extends CorePage{
	
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	
	public EventDetail(){
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
		
		add(new EventDetailForm("eventDetailForm"));
	}
	
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
		
	}
	
	private void setPageLeftNavigation() {
		Link eventDetailLink = new Link("eventListLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new EventList());
			}
		};
		add(eventDetailLink);
	}
	
	public final class EventDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		private DateTextField startDateTextField;
		private DateTextField endDateTextField;

		private TextField name;
		private TextField address;
		private TextField town;
		
		private DropDownChoice eventType;

		private TextArea textArea;

		private TinyMceBehavior tinyMCE;
		
		RegionSelector region = RegionSelector.getSingletonObject();

		private final List<String> EVENTTYPE_LIST = Arrays
				.asList(new EventTypeController().getEventTypeNameList());

		private Button submit;
		private ValueMap properties = new ValueMap();


		public EventDetailForm(final String id) {
			super(id);

			eventType = new DropDownChoice("eventType", new PropertyModel(
					properties, "eventType"), EVENTTYPE_LIST);
			eventType.setRequired(true);
			eventType.setLabel(new Model("Typ inzerátu"));
			add(eventType);
			add(new Label("eventTypeLabel", "Typ inzerátu *"));
			add(new FormComponentFeedbackBorder("eventTypeAsterix").add(eventType));

			name = new TextField("name", new PropertyModel(
					properties, "name"));
			name.setRequired(true);
			name.setLabel(new Model("Názov"));
			name.add(StringValidator.maximumLength(30));
			add(name);
			add(new Label("nameLabel", "Názov *"));
			add(new FormComponentFeedbackBorder("nameAsterix").add(name));

			startDateTextField = new DateTextField("startDate", new PropertyModel(
					properties, "startDate"), "dd.MM.yyyy");
			startDateTextField.add(new DatePicker());
			startDateTextField.setRequired(true);
			startDateTextField.setLabel(new Model("Zaèiatok"));
			add(startDateTextField);
			add(new Label("startDateLabel", "Zaèiatok *"));
			add(new FormComponentFeedbackBorder("startDateAsterix").add(startDateTextField));
			
			endDateTextField = new DateTextField("endDate", new PropertyModel(
					properties, "endDate"), "dd.MM.yyyy");
			endDateTextField.add(new DatePicker());
			endDateTextField.setRequired(true);
			endDateTextField.setLabel(new Model("Koniec"));
			add(endDateTextField);
			add(new Label("endDateLabel", "Koniec *"));
			add(new FormComponentFeedbackBorder("endDateAsterix").add(endDateTextField));
			
			address = new TextField("address", new PropertyModel(
					properties, "address"));
			address.setRequired(true);
			address.setLabel(new Model("Adresa"));
			add(address);
			add(new Label("addressLabel", "Adresa *"));
			add(new FormComponentFeedbackBorder("addressAsterix").add(address));

			///
			
			
			
			add(region.getCountryDropDownChoice());
			add(region.getCountyDropDownChoice());
			
			add(new Label("countryLabel", "Kraj *"));
			add(new Label("countyLabel", "Okres"));
			////
			
			town = new TextField("town", new PropertyModel(properties,
					"town"));
			town.setRequired(true);
			town.setLabel(new Model("Mesto"));
			add(town);
			add(new Label("townLabel", "Mesto *"));
			add(new FormComponentFeedbackBorder("townAsterix").add(town));
			
			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.add(StringValidator.maximumLength(5000));
			textArea.setEscapeModelStrings(false);
			add(textArea);

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}

		protected void onSubmit() {
			try {
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				
				String startDate = startDateTextField.getModelObjectAsString();
				String endDate = endDateTextField.getModelObjectAsString();

				Date dateStart = null;
				dateFormat.parse(startDate);
				dateStart = dateFormat.parse(startDate);
				java.sql.Date dateS = new java.sql.Date(dateStart.getTime());

				Date dateEnd = null;
				dateFormat.parse(endDate);
				dateEnd = dateFormat.parse(endDate);
				java.sql.Date dateE = new java.sql.Date(dateEnd.getTime());
				
				boolean validator=true;
				
				if (dateS.getTime()>dateE.getTime()){
					feedbackPanel.info("Dátum zaèiatku je neskoršie ako dátum konca. Prosím upravte dátum zaèiatku.");
					validator=false;
				}
				
				if (dateS.getTime()<new Date().getTime()){
					feedbackPanel.info("Nie je možné vloži udalos ktorá sa už konala.");
					validator=false;
				}
					
				if (validator){
				
				Event event = new Event();
				event.setName(name.getModelObjectAsString());
				
				event.setStartDate(dateS);
				event.setEndDate(dateE);

				event.setType(new EventTypeController()
						.getEventTypeByName(eventType
								.getModelObjectAsString()));
				event.setCounty(new CountyController().getCountyByName(region.getMyCounty()));
				event.setTown(town.getModelObjectAsString());
				event.setNote(textArea.getValue());
				event.setAddress(address.getModelObjectAsString());
				event.setLenght((dateEnd.getTime()-dateStart.getTime()));

				// not-user entered data
				event.setState(true);
				event.setActive(true);
				event.setOwner(user);

				EventController eventController = new EventController();

				if (eventController.saveNewEvent(event)){
					submit.setVisible(false);
					feedbackPanel.info("Inzerát úspešne vložený");
				}
				else
					feedbackPanel.warn("Chyba pri vkladaní");
				}

			} catch (BatchUpdateException e){
				System.out.println(e.getMessage());
				System.out.println(e.getNextException());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				
			}
		}

	}
	
}