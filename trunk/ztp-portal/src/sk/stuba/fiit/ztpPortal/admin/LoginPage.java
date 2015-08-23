package sk.stuba.fiit.ztpPortal.admin;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class LoginPage extends AdminPage {

	private static final long serialVersionUID = 1L;

	private RegisteredUserController userController;

	public LoginPage() {
		add(new LoginForm("loginForm"));
	}

	public final class LoginForm extends Form {

		private static final long serialVersionUID = 1L;
		TextField login;
		TextField password;
		Button submit;
		ComponentFeedbackPanel loginFeedback;
		ComponentFeedbackPanel passwordFeedback;
		ComponentFeedbackPanel submitFeedback;

		private ValueMap properties = new ValueMap();

		public LoginForm(final String id) {
			super(id);

			login = new TextField("login", new PropertyModel(properties,
					"login"));
			login.setRequired(true);
			add(login);
			add(loginFeedback = new ComponentFeedbackPanel("loginFeedback",
					login));
			add(new Label("loginLabel", "Prihlasovacie meno:"));

			password = new PasswordTextField("password", new PropertyModel(
					properties, "password"));
			password.setRequired(true);
			add(password);
			add(passwordFeedback = new ComponentFeedbackPanel(
					"passwordFeedback", password));
			add(new Label("passwordLabel", "Heslo:"));

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
			add(submitFeedback = new ComponentFeedbackPanel("submitFeedback",
					submit));

		}

		protected void onSubmit() {
			String inputPassword = password.getModelObjectAsString();
			String loginName = login.getModelObjectAsString();
			userController = new RegisteredUserController();
			if (userController.isLoginNameAndPassword(loginName, inputPassword)
					
					&& userController.isAdmin(loginName)
					&& userController.isStatus(loginName)) {
				RegisteredUser user = userController
						.getRegisteredUserByLogin(login
								.getModelObjectAsString());

				((AdminSession) getSession()).setLoged(user.getLogin());

				System.out.println("Authorizing " + user.getSurname());
				MainPage page = new MainPage();
				setResponsePage(page);

			}
			//pristup pre superadministratora
			if (userController.isLoginNameAndPassword(loginName, inputPassword)
					&& loginName.equals("superadministrator")) {
				RegisteredUser user = userController
						.getRegisteredUserByLogin(login
								.getModelObjectAsString());

				((AdminSession) getSession()).setLoged(user.getLogin());

				System.out.println("Authorizing " + user.getSurname());
				MainPage page = new MainPage();
				setResponsePage(page);

			}
			userController = null;
		}
	}
}
