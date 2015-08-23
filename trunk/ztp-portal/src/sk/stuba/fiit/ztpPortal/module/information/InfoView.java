package sk.stuba.fiit.ztpPortal.module.information;

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
import sk.stuba.fiit.ztpPortal.databaseController.InformationController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Information;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.Style;

public class InfoView extends CorePage{
	
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private InformationController eventController;
	private Information information;
	
	public InfoView(long id){
	
		if (getSession().getClass()==CoreSession.class){
			login = ((CoreSession) getSession()).getLoged();
			add(HeaderContributor.forCss(((CoreSession) getSession())
					.getUserStyle()));
		}	//toto robim kvoli prekliku admina
		else add(HeaderContributor.forCss(Style.NORMAL));
		
		userController = new RegisteredUserController();
		eventController = new InformationController();
		
		information = eventController.getInformationByInformationId(id);
		
		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);
		
		setPageHeaderPanel();
		setPageLeftNavigation();

		//form
		add(new InformationViewForm("infoViewForm"));
		
	}
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
		}

	private void setPageLeftNavigation() {
		Link eventListLink = new Link("infoListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new InfoList());
			}
		};
		add(eventListLink);
		
		Link newEventLink = new Link("newInfoLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new InfoDetail());
			}
		};
		add(newEventLink);
		
		add(new Link("commentLink"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new InfoCommentList(information));
			} 
		});
		
	}
	
	public final class InformationViewForm extends Form{
		
		private static final long serialVersionUID = 1L;
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		public InformationViewForm(String id) {
			super(id);
			
			add(new Label("cmsContent",information.getCmsContent()).setEscapeModelStrings(false));
			
		}
		
		
	}
	

}
