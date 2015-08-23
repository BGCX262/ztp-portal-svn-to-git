package sk.stuba.fiit.ztpPortal.core;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class PageNoAdmitance extends CorePage {

	private CorePage outerPage;

	public PageNoAdmitance(final CorePage incommingPage) {
		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));

		outerPage = incommingPage;

		Link homeLink = new Link("homeLink") {
			public void onClick() {
				setResponsePage(MainPage.class);
			}
		};

		Link registrationLink = new Link("registrationLink") {
			public void onClick() {
				setResponsePage(Registration.class);
			}
		};

		add(homeLink);
		add(registrationLink);
		add(new LoginForm("loginForm"));
	}

	class LoginForm extends Form {

		private RegisteredUserController userController;
		private RegisteredUser user;

		TextField login;
		TextField password;
		Button logoutButton;
		Link profilePageLink;
		Label logedAs;
		Label myProfile;
		Button submit;
		Button registrationButton;
		ComponentFeedbackPanel loginFeedback;
		ComponentFeedbackPanel passwordFeedback;
		Form loginForm;
		Form statusForm;
		Form searchForm;

		FeedbackPanel doLoginFeedback;

		private ValueMap properties = new ValueMap();

		public LoginForm(String id) {
			super(id);
			// TODO Auto-generated constructor stub

			login = new TextField("login", new PropertyModel(properties,
					"login"));
			login.setRequired(true);

			if (user != null)
				login.setModelValue(user.getLogin());
			login.setRequired(false);
			login.setLabel(new Model("Prihlasovacie meno"));
			add(login);
			// add(loginFeedback = new ComponentFeedbackPanel("loginFeedback",
			// login));
			add(new Label("headerLoginLabel", "Prihlasovacie meno:"));

			password = new PasswordTextField("password", new PropertyModel(
					properties, "password"));
			password.setRequired(false);
			add(password);
			password.setLabel(new Model("Prístupové heslo"));
			// add(passwordFeedback = new ComponentFeedbackPanel(
			// "passwordFeedback", password));
			add(new Label("headerPasswordLabel", "Heslo:"));

			add(submit = new Button("doLoginButton", new ResourceModel(
					"button.submit")) {
				public void onSubmit() {
					userController = new RegisteredUserController();

					String inputPassword = password.getModelObjectAsString();
					String inputLogin = login.getModelObjectAsString();

					if (inputPassword == null || inputLogin == null) {
						doLoginFeedback
								.warn("Zadajte prosím prihlasovacie údaje");
						// return;
					}

					if (userController.isLoginNameAndPassword(inputLogin,
							inputPassword)
							&& !userController.isStatus(inputLogin)) {
						doLoginFeedback
								.warn("Používate¾ské konto je deaktivované. Kontaktujte administrátora.");
						login.setModelValue("");
						password.setModelValue("");
						return;
					}

					if (userController.isLoginNameAndPassword(inputLogin,
							inputPassword)) {
						RegisteredUser user = userController
								.getRegisteredUserByLogin(login
										.getModelObjectAsString());

						((CoreSession) getSession()).setLoged(user.getLogin());
						((CoreSession) getSession()).setUserId(user.getId());

						setResponsePage(outerPage.getClass());

						userController = null;
					} else {
						login.setModelValue("");
						password.setModelValue("");
						doLoginFeedback
								.warn("Zlé prihlasovacie meno alebo heslo");
					}

				}
			});

			doLoginFeedback = new FeedbackPanel("feedback");
			add(doLoginFeedback);
		}

	}

}
