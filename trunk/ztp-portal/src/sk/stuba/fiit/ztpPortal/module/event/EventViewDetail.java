package sk.stuba.fiit.ztpPortal.module.event;

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
import sk.stuba.fiit.ztpPortal.core.RegionSelector;
import sk.stuba.fiit.ztpPortal.core.RegionSelectorView;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.EventController;
import sk.stuba.fiit.ztpPortal.databaseController.EventTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.EventType;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class EventViewDetail extends CorePage {

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;

	private EventController eventController;
	private Event event;
	private ModalWindowPage modal;
	
	public EventViewDetail(long id) {

		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		eventController = new EventController();
		
		event = eventController.getEventByEventId(id);
		
		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		modal = new ModalWindowPage(event);
		
		setPageHeaderPanel();
		setPageLeftNavigation();

		
		// formular vlozim

		add(new EventViewDetailForm("eventDetailForm"));
		
		add(modal.getLabel());
		add(modal.getModal());
		
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}

	private void setPageLeftNavigation() {
		Link eventDetailLink = new Link("eventListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new EventList());
			}
		};
		add(eventDetailLink);
		
		add(new Link("pictureView"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new PictureViewer(event.getId()));
			} 
		});
		
		add(new Link("commentLink"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new EventCommentList(event));
			} 
		});
		add(modal.getLink());
	}
	

	public final class EventViewDetailForm extends Form {

			private static final long serialVersionUID = 1L;

			private DateTextField startDateTextField;
			private DateTextField endDateTextField;

			private TextField name;
			private TextField address;
			private TextField town;
			
			private DropDownChoice eventType;

			private TextArea textArea;
			private CheckBox active;

			private TinyMceBehavior tinyMCE;

			private final List<String> EVENTTYPE_LIST = Arrays
					.asList(new EventTypeController().getEventTypeNameList());

			private Button submit;
			private ValueMap properties = new ValueMap();
			private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
			private String[] value = new String[1];
			
			private Date startDate;
			private Date endDate;
			
			private RegionSelectorView regionSelector;
			private RegionSelector region;
			
			public Date getStartDate(){
				return event.getStartDate();
			}
			
			public void setStartDate(Date startDate){
				this.startDate = startDate;
			}
			
			
			public Date getEndDate(){
				return event.getEndDate();
			}
			
			public void setEndDate(Date endDate){
				this.endDate = endDate;
			}
			
			
		public EventViewDetailForm(final String id) {
			super(id);

			EventType typ = new EventType();
			eventType = new DropDownChoice("eventType", new PropertyModel(
					typ, "name"), EVENTTYPE_LIST);
			typ.setName(event.getType().getName());
			eventType.setRequired(true);
			eventType.setLabel(new Model("Typ inzerátu"));
			add(eventType);
			add(new Label("eventTypeLabel", "Typ inzerátu *"));

			name = new TextField("name", new PropertyModel(
					properties, "name"));
			name.setRequired(true);
			name.setLabel(new Model("Názov"));
			value[0] = event.getName();
			name.setModelValue(value);
			name.add(StringValidator.maximumLength(30));
			add(name);
			add(new Label("nameLabel", "Názov *"));

			value[0]=dateFormat.format(event.getStartDate());
			startDateTextField = new DateTextField("startDate", new PropertyModel(
					this, "startDate"), "dd.MM.yyyy");
			startDateTextField.add(new DatePicker());
			startDateTextField.setRequired(true);
			startDateTextField.setLabel(new Model("Zaèiatok"));
			add(startDateTextField);
			add(new Label("startDateLabel", "Zaèiatok *"));
			
			value[0]=dateFormat.format(event.getEndDate());
			endDateTextField = new DateTextField("endDate", new PropertyModel(
					this, "endDate"), "dd.MM.yyyy");
			endDateTextField.add(new DatePicker());
			endDateTextField.setRequired(true);
			endDateTextField.setLabel(new Model("Koniec *"));
			add(endDateTextField);
			add(new Label("endDateLabel", "Koniec"));

			value[0] = event.getAddress();
			address = new TextField("address", new PropertyModel(
					properties, "address"));
			address.setRequired(true);
			address.setModelValue(value);
			address.setLabel(new Model("Adresa"));
			add(address);
			add(new Label("addressLabel", "Adresa *"));

			if (event.getCounty() != null) {
				regionSelector = new RegionSelectorView(event.getCounty());
				add(regionSelector.getCountryDropDownChoice());
				add(regionSelector.getCountyDropDownChoice());
			} else {
				region = RegionSelector.getSingletonObject();
				add(region.getCountryDropDownChoice());
				add(region.getCountyDropDownChoice());
			}
			add(new Label("countryLabel", "Kraj *"));
			add(new Label("countyLabel", "Okres *"));
				
			value[0] = event.getAddress();
			town = new TextField("town", new PropertyModel(
					properties, "town"));
			town.setRequired(true);
			town.setModelValue(value);
			town.setLabel(new Model("Mesto"));
			add(town);
			add(new Label("townLabel", "Mesto *"));

			value[0] = event.getNote();
			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.add(StringValidator.maximumLength(5000));
			textArea.setModelValue(value);
			textArea.setEscapeModelStrings(false);
			add(textArea);

			value[0]=String.valueOf(event.isActive());
			active = new CheckBox("active", new PropertyModel(properties,"active"));
			active.setModelValue(value);
			add(active);
			add(new Label("activeLabel","Aktívny"));
			
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
				java.sql.Date date = new java.sql.Date(dateStart.getTime());
				event.setStartDate(date);
				java.sql.Date dateS = new java.sql.Date(dateStart.getTime());
				
				Date dateEnd = null;
				dateFormat.parse(endDate);
				dateEnd = dateFormat.parse(endDate);
				date = new java.sql.Date(dateEnd.getTime());
				event.setEndDate(date);
				java.sql.Date dateE = new java.sql.Date(dateEnd.getTime());
				
				boolean validator = true;
				
				if (dateS.getTime()>dateE.getTime()){
					feedbackPanel.info("Dátum zaèiatku je neskoršie ako dátum konca. Prosím upravte dátum zaèiatku.");
					validator=false;
				}
				
//				if (dateS.getTime()<new Date().getTime()){
//					feedbackPanel.info("Nie je možné vloži udalos ktorá sa už konala.");
//					validator=false;
//				}
				
				if (validator){
				
				
				event.setName(name.getModelObjectAsString());
				
				event.setType(new EventTypeController()
						.getEventTypeByName(eventType
								.getModelObjectAsString()));
				if (event.getCounty()!=null)
					event.setCounty(new CountyController().getCountyByName(regionSelector.getMyCounty()));
				else 
					event.setCounty(new CountyController().getCountyByName(region.getMyCounty()));
				event.setTown(town.getModelObjectAsString());
				event.setNote(textArea.getValue());
				event.setAddress(address.getModelObjectAsString());
				event.setLenght((dateEnd.getTime()-dateStart.getTime()));

				// not-user entered data
				event.setActive(Boolean.valueOf(active.getModelObjectAsString()));

				//EventController eventController = new EventController();

				if (eventController.updateEvent(event))
					feedbackPanel.info("Inzerát úspešne aktualizovaný");
				else
					feedbackPanel.warn("Chyba pri vkladaní");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				
			}
		}

	}

}
