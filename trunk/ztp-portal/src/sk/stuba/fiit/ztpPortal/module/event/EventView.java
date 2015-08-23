package sk.stuba.fiit.ztpPortal.module.event;

import java.sql.BatchUpdateException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.ResourceModel;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.databaseController.EventController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.UserEventController;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.UserEvent;
import sk.stuba.fiit.ztpPortal.server.Style;

public class EventView extends CorePage{
	
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;

	private EventController eventController;
	private Event event;
	
	public EventView(long id){
	
		if (getSession().getClass()==CoreSession.class){
			login = ((CoreSession) getSession()).getLoged();
			add(HeaderContributor.forCss(((CoreSession) getSession())
					.getUserStyle()));
		}	//toto robim kvoli prekliku admina
		else add(HeaderContributor.forCss(Style.NORMAL));
		
		userController = new RegisteredUserController();
		eventController = new EventController();
		
		event = eventController.getEventByEventId(id);
		
		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);
		
		setPageHeaderPanel();
		setPageLeftNavigation();
		
		//form
		
		add(new EventViewForm("eventViewForm"));
		
	}
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}
	
	private void setPageLeftNavigation() {
		Link eventListLink = new Link("eventListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new EventList());
			}
		};
		add(eventListLink);
		
		Link newEventLink = new Link("newEventLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new EventDetail());
			}
		};
		add(newEventLink);
		
		
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
		
		add(new EventForm("eventForm"));
		
	}
	
	public final class EventForm extends Form{

		private static final long serialVersionUID = 1L;
		
		private Button submit;
		private Label registeredCount; 
		
		public EventForm(String id) {
			super(id);
			
			registeredCount = new Label("registeredCount",""+new UserEventController().getRegisteredCountForEvent(event.getId()));
			add(registeredCount);
			
			submit = new Button("submit",
					new ResourceModel("button.submit"));
			
			add(submit);
			if (login==null || new UserEventController().isUserForEventRegistered(event.getId(),user.getId())) submit.setVisible(false);
			
		}
		protected void onSubmit() {
			try {
			UserEvent userEvent = new UserEvent();
			userEvent.setUser(user);
			userEvent.setEvent(event);
			UserEventController userEventcontroller = new UserEventController();
			
			if (userEventcontroller.saveNewUserEvent(userEvent)){
				feedbackPanel.info("Pridali ste sa.");
				submit.setVisible(false);
			}
			else
				feedbackPanel.warn("Chyba pri vkladaní.");
				
			
			
			} catch (BatchUpdateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public final class EventViewForm extends Form{
		
		private static final long serialVersionUID = 1L;
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		public EventViewForm(String id) {
			super(id);
			
			add(new Label("eventTypeLabel","Typ"));
			add(new Label("eventType",event.getType().getName()));
			
			add(new Label("eventNameLabel","Názov"));
			add(new Label("eventName",event.getName()));
			
			add(new Label("startDateLabel", "Zaèiatok"));
			add(new Label("startDate", dateFormat.format(event.getStartDate())));
			
			add(new Label("endDateLabel", "Koniec"));
			add(new Label("endDate", dateFormat.format(event.getEndDate())));
			
			add(new Label("eventAddressLabel","Miesto konania"));
			add(new Label("eventAddress",event.getAddress()));
			
			if (event.getCounty() != null){
			
				add(new Label("eventCountryLabel","Kraj"));
				add(new Label("eventCountry", event.getCounty().getCountry().getName()));
				
				add(new Label("eventCountyLabel","Okres"));
				add(new Label("eventCounty", event.getCounty().getName()));
			
			} else{
				add(new Label("eventCountryLabel","Kraj"));
				add(new Label("eventCountry", "Neuvedené"));
				
				add(new Label("eventCountyLabel","Okres"));
				add(new Label("eventCounty", "Neuvedené"));
			}
			
			add(new Label("eventTownLabel","Mesto"));
			add(new Label("eventTown", event.getTown()));
			
			add(new Label("cmsContent",event.getNote()).setEscapeModelStrings(false));
			
		}
		
		
	}
	

}
