package sk.stuba.fiit.ztpPortal.admin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.HandicapTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.County;
import sk.stuba.fiit.ztpPortal.databaseModel.HandicapType;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class UserDetail extends AdminPage {

	private static final long serialVersionUID = 1L;

	private static RegisteredUserController userController;

	private static RegisteredUser user;

	private static FeedbackPanel feedbackPanel;

	private String login;

	public UserDetail(String loginName) {
		login = ((AdminSession) getSession()).getLoged();

		user = new RegisteredUserController()
				.getRegisteredUserByLogin(loginName);

		Label loginNameInfoLabel = new Label("profileNameInfo",
				"Ste prihlásený ako " + login);
		add(loginNameInfoLabel);

		setNavigation();

		feedbackPanel = new FeedbackPanel("feedback");
		add(feedbackPanel);

		add(new RegistrationForm("registrationForm"));

	}

	private void setNavigation() {
		AdminNavigation adminNavigation = new AdminNavigation();

		add(adminNavigation.getUserLogOutLink());

		add(new NavigationPanel("panel"));

	}

	public final static class RegistrationForm extends Form {

		private static final long serialVersionUID = 1L;

		TextField name;
		TextField surname;
		TextField login;
		TextField password;
		TextField passwordAgain;
		TextField email;
		Button submit;
		TextField region;
		DropDownChoice town;
		DropDownChoice handicapType;

		private final List<String> HANDICAPTYPE_LIST = Arrays
				.asList(new HandicapTypeController().getHandicapNameList());

		private final List<String> TOWN_LIST = Arrays
				.asList(new CountyController().getCountyNameList());

		CheckBox preferRegion;
		CheckBox status;
		CheckBox admin;

		private ValueMap properties = new ValueMap();

		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

		public RegistrationForm(final String id) {
			super(id);

			name = new TextField("name", new PropertyModel(properties, "name"));
			name.setRequired(true);
			name.setModelValue(user.getName());
			name.setEnabled(false);
			add(name);
			add(new Label("nameLabel", "Meno"));

			surname = new TextField("surname", new PropertyModel(properties,
					"surname"));
			surname.setRequired(true);
			surname.setModelValue(user.getSurname());
			surname.setEnabled(false);
			add(surname);
			add(new Label("surnameLabel", "Priezvisko"));

			email = new TextField("email", new PropertyModel(properties,
					"email"));
			email.setRequired(true);
			if (user.getEmail() != null)
				email.setModelValue(user.getEmail());
			email.setEnabled(false);
			add(email);
			// add(addressFeedback = new
			// ComponentFeedbackPanel("addressFeedback",
			// address));
			add(new Label("emailLabel", "Email"));

			login = new TextField("login", new PropertyModel(properties,
					"login"));
			login.setRequired(true);
			login.setModelValue(user.getLogin());
			login.setEnabled(false);
			add(login);
			// add(loginNameFeedback = new
			// ComponentFeedbackPanel("loginNameFeedback",
			// loginName));
			add(new Label("loginLabel", "Login"));

			County mesto = new County();
			town = new DropDownChoice("town", new PropertyModel(mesto, "name"),
					TOWN_LIST);
			town.setRequired(false);
			if (user.getCounty() != null)
				mesto.setName(user.getCounty().getName());
			add(town);
			add(new Label("townLabel", "Mesto"));

			HandicapType postihnutie = new HandicapType();
			handicapType = new DropDownChoice("handicapType",
					new PropertyModel(postihnutie, "name"), HANDICAPTYPE_LIST);
			handicapType.setRequired(false);
			if (user.getHandicapType() != null)
				postihnutie.setName(user.getHandicapType().getName());
			add(handicapType);
			add(new Label("handicapTypeLabel", "Handicap"));

			status = new CheckBox("status", new PropertyModel(properties,
					"status"));
			if (user.isState())
				status.setModelValue("true");
			else
				status.setModelValue("false");
			add(status);
			add(new Label("statusLabel", "Aktívny"));

			admin = new CheckBox("admin",
					new PropertyModel(properties, "admin"));
			if (user.isAdmin())
				admin.setModelValue("true");
			else
				admin.setModelValue("false");
			add(admin);
			add(new Label("adminLabel", "Administrátor"));

			add(new Label("registrationDateLabel", "Dátum registrácie:"));
			add(new Label("userRegistrationDateLabel", dateFormat.format(user
					.getRegistrationDate())));

			add(new Label("lastChangeDateLabel", "Dátum zmeny:"));
			add(new Label("userLastChangeDateLabel", dateFormat.format(user
					.getChangeDate())));

			password = new PasswordTextField("password", new PropertyModel(
					properties, "password"));
			add(password);
			password.setRequired(false);
			// add(passwordFeedback = new ComponentFeedbackPanel(
			// "passwordFeedback", password));
			add(new Label("passwordLabel", "Heslo:"));

			passwordAgain = new PasswordTextField("passwordAgain",
					new PropertyModel(properties, "passwordAgain"));
			passwordAgain.setRequired(false);
			add(passwordAgain);
			// add(passwordAgainFeedback = new
			// ComponentFeedbackPanel("passwordAgainFeedback",
			// passwordAgain));
			add(new Label("passwordAgainLabel", "Heslo znovu:"));

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
			// add(submitFeedback = new ComponentFeedbackPanel("submitFeedback",
			// submit));
		}

		private boolean checkPassword(String password, String passwordAgain) {
			if (password.equals("") || passwordAgain.equals(""))
				return false;
			return password.equals(passwordAgain);
		}

		protected void onSubmit() {
			userController = new RegisteredUserController();
			String passwordCheck = password.getModelObjectAsString();
			String passwordAgainCheck = passwordAgain.getModelObjectAsString();

			if (status.getModelObjectAsString().equals("true"))
				user.setState(true);
			else
				user.setState(false);

			if (admin.getModelObjectAsString().equals("true"))
				user.setAdmin(true);
			else
				user.setAdmin(false);

			if (checkPassword(passwordCheck, passwordAgainCheck)) {
				user.setPassword(userController.getMD5(password.getModelObjectAsString()));

				// user.setState(state);

				if (userController.updateUser(user))
					feedbackPanel.info("Údaje a zmena hesla uložené");
				else
					feedbackPanel.warn("Problém pri ukladaní");

			} else {
				if (userController.updateUser(user))
					feedbackPanel.info("Údaje uložené bez zmeny hesla");
				else
					feedbackPanel.warn("Problém pri ukladaní");
			}
			userController = null;
		}
	}
}