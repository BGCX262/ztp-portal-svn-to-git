package sk.stuba.fiit.ztpPortal.module.healthAid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.databaseController.HealthAidController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.HealthAid;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.Style;

public class HealthAidView extends CorePage{
	
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private HealthAidController eventController;
	private HealthAid information;
	
	public HealthAidView(long id){
	
		if (getSession().getClass()==CoreSession.class){
			login = ((CoreSession) getSession()).getLoged();
			add(HeaderContributor.forCss(((CoreSession) getSession())
					.getUserStyle()));
		}	//toto robim kvoli prekliku admina
		else add(HeaderContributor.forCss(Style.NORMAL));
		
		userController = new RegisteredUserController();
		eventController = new HealthAidController();
		
		information = eventController.getHealthAidByHealthAidId(id);
		
		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);
	
		setPageHeaderPanel();
		setPageLeftNavigation();
		
		
		//form
		
		add(new HealthAidViewForm("infoViewForm"));
		
	}
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);

	}

	private void setPageLeftNavigation() {
		Link eventListLink = new Link("healthAidListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new HealthAidList());
			}
		};
		add(eventListLink);
		
		Link newEventLink = new Link("newHealthAidLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new HealthAidDetail());
			}
		};
		add(newEventLink);
		
		add(new Link("commentLink"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new HealthAidCommentList(information));
			} 
		});
		
	}
	
	public final class HealthAidViewForm extends Form{
		
		private static final long serialVersionUID = 1L;
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		public HealthAidViewForm(String id) {
			super(id);
			
			add(new Label("cmsContent",information.getCmsContent()).setEscapeModelStrings(false));
			
		}
		
		
	}
	

}
