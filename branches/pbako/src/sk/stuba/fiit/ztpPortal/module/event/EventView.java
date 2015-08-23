package sk.stuba.fiit.ztpPortal.module.event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.PageRenderer;
import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.EventController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.module.event.EventViewDetail.EventViewDetailForm;
import sk.stuba.fiit.ztpPortal.server.Style;

public class EventView extends CorePage{
	
	private PageRenderer pageRenderer;
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private long userID;

	private CmsContentController cmsControler;
	private CmsContent pageContent;
	private EventController eventController;
	private Event event;
	
	public EventView(long id){
	
		if (getSession().getClass()==CoreSession.class){
			login = ((CoreSession) getSession()).getLoged();
			add(HeaderContributor.forCss(((CoreSession) getSession())
					.getUserStyle()));
			userID = ((CoreSession) getSession()).getUserId();
		}	//toto robim kvoli prekliku admina
		else add(HeaderContributor.forCss(Style.NORMAL));
		
		userController = new RegisteredUserController();
		cmsControler = new CmsContentController();
		eventController = new EventController();
		
		event = eventController.getEventByEventId(id);
		
		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);
		
		pageRenderer = new PageRenderer();
		setPageHeaderPanel();
		setPageNavigationPanel();
		setPageLeftNavigation();
		
		add(pageRenderer.getSearchForm());
		
		//form
		
		add(new EventViewForm("eventViewForm"));
		
	}
	
	private void setPageHeaderPanel() {
		pageRenderer.setUser(user);
		Form loginForm = pageRenderer.getLoginForm();
		add(loginForm);
		Form statusForm = pageRenderer.getStatusForm();
		add(statusForm);

		if (user == null)
			pageRenderer.disableStatusForm();
		else
			pageRenderer.disableLoginForm();

	}

	private void setPageNavigationPanel() {
		add(pageRenderer.getEventLink());
		add(pageRenderer.getForumLink());
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());
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
			
			add(new Label("eventTownLabel","Región:"));
			add(new Label("eventTown", event.getTown().getName()));
			
			add(new Label("cmsContent",event.getNote()).setEscapeModelStrings(false));
			
		}
		
		
	}
	

}
