package sk.stuba.fiit.ztpPortal.core;

import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.protocol.http.WebApplication;

public class MainPage extends CorePage {

	private static final long serialVersionUID = 1L;

	private PageRenderer pageRenderer;
	private static CoreFeedBackPanel feedbackPanel;
	
	private RegisteredUserController userController;
	private CmsContentController cmsController;
	private CmsContent cmsContent;
	
	private String login;
	private RegisteredUser user;
	
	private NewArticles news;

	public MainPage() {
		// nastavim CSS
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		cmsController = new CmsContentController();

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;
		
		 feedbackPanel = new CoreFeedBackPanel("feedback");
		 	add(feedbackPanel);

		pageRenderer = new PageRenderer();
		setPageHeaderPanel();
		setPageNavigationPanel();
		
		add(pageRenderer.getSearchForm());
		
		cmsContent = cmsController.getContentByName("main");
		add(new Label("cmsContent",cmsContent.getContent()).setEscapeModelStrings(false));
		
		news = new NewArticles();
		add(news.getNewJobArticle());
		add(news.getNewForumComment());
		add(news.getNewEvent());
	}

	private void setPageHeaderPanel() {
		pageRenderer.setUser(user);
		Form loginForm = pageRenderer.getLoginForm();
		add(loginForm);
		Form statusForm = pageRenderer.getStatusForm();
		add(statusForm);
		
		if (user==null) pageRenderer.disableStatusForm();
		else pageRenderer.disableLoginForm();
		
	}

	private void setPageNavigationPanel() {
		add(pageRenderer.getEventLink());
		add(pageRenderer.getForumLink());
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());
	}

}
