package sk.stuba.fiit.ztpPortal.core;

import java.util.Date;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.http.WebResponse;

import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class MainPage extends CorePage {

	private static final long serialVersionUID = 1L;

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private CmsContentController cmsController;
	private CmsContent cmsContent;

	private String login;
	private RegisteredUser user;

	private static NewArticles news;

	// TODO neviem co to nefunguje
	protected void setHeaders(WebResponse response) {
		response.setHeader("Cache-Control",
				"no-cache, max-age=0, must-revalidate"); // no-store
		// response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
	}

	public MainPage() {
		
		//news = new NewArticles();
		Date startDate=new Date();
		add(NewArticles.getNewJobArticle());
		add(NewArticles.getNewForumComment());
		add(NewArticles.getNewEvent());
		add(NewArticles.getNewEducation());
		add(NewArticles.getNewLiving());
		add(NewArticles.getNewInformation());
		add(NewArticles.getNewHealthAid());
		add(NewArticles.getNewDayCare());
		System.out.println("!!! " + (new Date().getTime()-startDate.getTime()));
		
		// nastavim CSS
		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		cmsController = new CmsContentController();

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);
		setPageHeaderPanel();
		add(new FooterPanel("footPanel"));

		cmsContent = cmsController.getContentByName("main");
		add(new Label("cmsContent", cmsContent.getContent())
				.setEscapeModelStrings(false));
		System.out.println("!!! " + (new Date().getTime()-startDate.getTime()));
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel", user);
		add(panel);
	}

}
