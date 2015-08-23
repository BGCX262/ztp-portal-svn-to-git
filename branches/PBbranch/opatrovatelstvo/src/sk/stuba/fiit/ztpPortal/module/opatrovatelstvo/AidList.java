package sk.stuba.fiit.ztpPortal.module.opatrovatelstvo;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import sk.stuba.fiit.ztpPortal.admin.AdminPage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.PageRenderer;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class AidList extends AdminPage{
	private PageRenderer pageRenderer;
	private static FeedbackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	public AidList(){
		
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;

		feedbackPanel = new FeedbackPanel("feedback",new ContainerFeedbackMessageFilter(this));
		add(feedbackPanel);

		pageRenderer = new PageRenderer();
		setPageHeaderPanel();
		setPageNavigationPanel();
	}

	private void setPageHeaderPanel() {
		pageRenderer.setUser(user);
		add(pageRenderer.getLoginForm());
		add(pageRenderer.getStatusForm());
	}

	private void setPageNavigationPanel() {
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());
	}
}
