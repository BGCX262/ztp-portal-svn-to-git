package sk.stuba.fiit.ztpPortal.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.SearchResultList;
import sk.stuba.fiit.ztpPortal.module.accomodation.LivingList;
import sk.stuba.fiit.ztpPortal.module.dayCare.DayCareList;
import sk.stuba.fiit.ztpPortal.module.education.EducationList;
import sk.stuba.fiit.ztpPortal.module.event.EventList;
import sk.stuba.fiit.ztpPortal.module.forum.ThemeList;
import sk.stuba.fiit.ztpPortal.module.healthAid.HealthAidList;
import sk.stuba.fiit.ztpPortal.module.information.InfoList;
import sk.stuba.fiit.ztpPortal.module.job.JobList;
import sk.stuba.fiit.ztpPortal.server.SearchCommentStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchContext;
import sk.stuba.fiit.ztpPortal.server.SearchCourseStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchEventStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchHealthAidStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchInformationStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchJobStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchLivingStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchPortalContentStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchSchoolStrategy;

public class PageRenderer extends CorePage {

	private static final long serialVersionUID = 1L;

	private RegisteredUserController userController;
	private RegisteredUser user;

	private TextField login;
	private TextField password;
	private Button logoutButton;
	private Link profilePageLink;
	private Label logedAs;
	private Label myProfile;
	private Button submit;
	private Button registrationButton;
	private Form loginForm;
	private Form statusForm;
	private Form searchForm;

	private BookmarkablePageLink jobListLink;
	private BookmarkablePageLink homeLink;
	private BookmarkablePageLink forumLink;
	private BookmarkablePageLink eventLink;
	private BookmarkablePageLink schoolLink;
	private BookmarkablePageLink livingLink;
	private BookmarkablePageLink informationLink;
	private BookmarkablePageLink healthAidLink;
	private BookmarkablePageLink dayCareListLink;
	private BookmarkablePageLink styleSelectPageLink;
	private BookmarkablePageLink pageMapLink;
	
	private FeedbackPanel doLoginFeedback;

	private ValueMap properties = new ValueMap();

	public void setUser(RegisteredUser user) {
		this.user = user;
	}

	public Form getLoginForm() {
		loginForm = new LoginForm("loginForm");
		return loginForm;
	}

	public Form getStatusForm() {
		statusForm = new StatusForm("statusForm");
		return statusForm;
	}

	public Form getSearchForm() {
		searchForm = new SearchForm("searchForm");
		return searchForm;
	}

	public PageRenderer() {
		homeLink = new BookmarkablePageLink("homeLink", MainPage.class);
		jobListLink = new BookmarkablePageLink("jobList", JobList.class);
		forumLink = new BookmarkablePageLink("forumList", ThemeList.class);
		eventLink = new BookmarkablePageLink("eventList", EventList.class);
		schoolLink = new BookmarkablePageLink("educationList",
				EducationList.class);
		livingLink = new BookmarkablePageLink("livingList", LivingList.class);
		informationLink = new BookmarkablePageLink("informationList",
				InfoList.class);
		healthAidLink = new BookmarkablePageLink("healthAidList",
				HealthAidList.class);
		dayCareListLink = new BookmarkablePageLink("dayCareList",
				DayCareList.class);

		styleSelectPageLink = new BookmarkablePageLink("styleSelectPage",
				StyleChooser.class);
		pageMapLink = new BookmarkablePageLink("pageMapLink",
				PageMap.class);
	}

	public Link getJobListLink() {
		return jobListLink;
	}

	public Link getHomePageLink() {
		return homeLink;
	}

	public Link getForumLink() {
		return forumLink;

	}

	public Link getEventLink() {
		return eventLink;
	}

	public Link getEducationLink() {
		return schoolLink;
	}

	public Link getLivingLink() {
		return livingLink;
	}

	public Link getInformationLink() {
		return informationLink;
	}

	public Link getHealthAidLink() {
		return healthAidLink;
	}

	public Link getDayCareLink() {
		return dayCareListLink;
	}

	public Link getStyleSelectPageLink() {
		return styleSelectPageLink;
	}
	
	public Link getPageMapLink(){
		return pageMapLink;
	}

	public FeedbackPanel getFeedback() {
		return doLoginFeedback;
	}

	public void disableStatusForm() {
		statusForm.setVisible(false);
		logedAs.setVisible(false);
		logoutButton.setVisible(false);
		profilePageLink.setVisible(false);
		myProfile.setVisible(false);
	}

	public void disableLoginForm() {
		loginForm.setVisible(false);
		login.setVisible(false);
		password.setVisible(false);
		submit.setVisible(false);
		registrationButton.setVisible(false);
	}

	/**
	 * Login formulár
	 * 
	 * @author devel
	 * 
	 */
	public final class LoginForm extends Form {

		private static final long serialVersionUID = 1L;

		private String[] value = new String[1];

		public LoginForm(final String id) {
			super(id);

			login = new TextField("login", new PropertyModel(properties,
					"login"));
			login.setRequired(true);

			if (user != null) {
				value[0] = user.getLogin();
				login.setModelValue(value);
			}
			login.setRequired(false);
			login.setLabel(new Model("Prihlasovacie meno"));
			add(login);
			add(new Label("headerLoginLabel", "Prihlasovacie meno:"));

			password = new PasswordTextField("password", new PropertyModel(
					properties, "password"));
			password.setRequired(false);
			add(password);
			password.setLabel(new Model("Prístupové heslo"));
			add(new Label("headerPasswordLabel", "Heslo:"));

			add(submit = new Button("doLoginButton", new ResourceModel(
					"button.submit")) {

				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					userController = new RegisteredUserController();

					String inputPassword = password.getModelObjectAsString();
					String inputLogin = login.getModelObjectAsString();

					if (userController.isLoginNameAndPassword(inputLogin,
							inputPassword)
							&& !userController.isStatus(inputLogin)) {
						doLoginFeedback
								.warn("Používate¾ské konto je deaktivované. Kontaktujte administrátora.");
						value[0] = "";
						login.setModelValue(value);
						password.setModelValue(value);
						return;
					}

					if (userController.isLoginNameAndPassword(inputLogin,
							inputPassword)) {
						RegisteredUser user = userController
								.getRegisteredUserByLogin(login
										.getModelObjectAsString());

						((CoreSession) getSession()).setLoged(user.getLogin());
						((CoreSession) getSession()).setUserId(user.getId());

						setResponsePage(MainPage.class);

						userController = null;
					} else {
						value[0] = "";
						login.setModelValue(value);
						password.setModelValue(value);
						// loginFeedback.warn("Zlé meno alebo heslo");
						doLoginFeedback
								.warn("Zlé prihlasovacie meno alebo heslo");
					}

				}
			});

			add(registrationButton = new Button("registrationButton",
					new ResourceModel("button.registrationButton")) {

				public void onSubmit() {
					setResponsePage(new Registration());
				}
			});

			doLoginFeedback = new FeedbackPanel("feedback");

			// doLoginFeedback= new FeedbackPanel("doLoginFeedback",new
			// ContainerFeedbackMessageFilter(this));
		}

	}

	/**
	 * Kto je prihlásený formulár
	 * 
	 * @author devel
	 * 
	 */
	public final class StatusForm extends Form {

		private static final long serialVersionUID = 1L;

		public StatusForm(final String id) {
			super(id);

			logedAs = new Label("statusLoginLabel", "Ste prihlásený ako");
			add(logedAs);
			String userName;
			if (user == null)
				userName = "";
			else
				userName = user.getLogin();
			add(new Label("loginNameLabel", userName));

			myProfile = new Label("myProfile", "môj profil");

			add(logoutButton = new Button("doLogout", new ResourceModel(
					"button.doLogout")));

			profilePageLink = new Link("profilePageLink") {

				public void onClick() {
					setResponsePage(new ProfilePage());
				}
			};
			add(profilePageLink.add(myProfile));
			// add(myProfile);

		}

		protected void onSubmit() {
			((CoreSession) getSession()).setLoged(null);
			((CoreSession) getSession()).invalidateNow();
			getRequestCycle().setRedirect(true);
			user = null;
			login = null;
			setResponsePage(MainPage.class);
		}
	}

	public final class SearchForm extends Form {
		private static final long serialVersionUID = 1L;

		TextField searchField;
		Button searchButton;

		public SearchForm(final String id) {
			super(id);

			searchField = new TextField("search", new PropertyModel(properties,
					"search"));

			add(searchField);

			add(searchButton = new Button("searchButton", new ResourceModel(
					"button.submit")) {
				private static final long serialVersionUID = 1L;

				public void onSubmit() {

					String searchString = searchField.getModelObjectAsString();
					if (searchString == "")
						searchString = "//t";

					List<SearchResultList> resultList = new ArrayList<SearchResultList>();

					resultList.addAll(new SearchContext(
							new SearchCommentStrategy()).execute(searchString));
					resultList.addAll(new SearchContext(
							new SearchEventStrategy()).execute(searchString));
					resultList
							.addAll(new SearchContext(new SearchJobStrategy())
									.execute(searchString));
					resultList.addAll(new SearchContext(
							new SearchLivingStrategy()).execute(searchString));
					resultList.addAll(new SearchContext(
							new SearchPortalContentStrategy())
							.execute(searchString));
					resultList.addAll(new SearchContext(
							new SearchSchoolStrategy()).execute(searchString));
					resultList.addAll(new SearchContext(
							new SearchCourseStrategy()).execute(searchString));
					resultList.addAll(new SearchContext(
							new SearchInformationStrategy())
							.execute(searchString));
					resultList.addAll(new SearchContext(
							new SearchHealthAidStrategy())
							.execute(searchString));

					setResponsePage(new SearchPage(resultList));
				}
			});

		}
	}

}
