package sk.stuba.fiit.ztpPortal.core;

import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

import org.apache.wicket.protocol.http.WebApplication;

public class MainPage extends CorePage {

	private static final long serialVersionUID = 1L;

	private PageRenderer pageRenderer;
	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;

	public MainPage() {
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;

//		String path = ((WebApplication) getApplication()).getWicketServlet()
//				.getServletContext().getRealPath("/");

		pageRenderer = new PageRenderer();
		setPageHeaderPanel();
		setPageNavigationPanel();
	}

	private void setPageHeaderPanel() {
//		PageRenderer pageRenderer = new PageRenderer();
		pageRenderer.setUser(user);
		add(pageRenderer.getLoginForm());
		add(pageRenderer.getStatusForm());
	}

	private void setPageNavigationPanel() {
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());
	}

}
