package sk.stuba.fiit.ztpPortal.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.HandicapTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.County;
import sk.stuba.fiit.ztpPortal.databaseModel.HandicapType;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class ProfilePage extends CorePage {

	private static final long serialVersionUID = 1L;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;

	private static CoreFeedBackPanel feedbackPanel;

	public ProfilePage() {
		// nastavim CSS
		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;
		setPageHeaderPanel();

		add(new UserDetailForm("userDetailForm"));

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel", user);
		add(panel);
	}

	public final class UserDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		TextField name;
		TextField surname;
		TextField login;
		TextField password;
		TextField passwordAgain;
		TextField email;
		Button submit;
		TextField town;
		DropDownChoice county;
		DropDownChoice handicapType;

		private final List<String> HANDICAPTYPE_LIST = Arrays
				.asList(new HandicapTypeController().getHandicapNameList());

		private final List<String> COUNTY_LIST = Arrays
				.asList(new CountyController().getCountyNameList());

		CheckBox preferRegion;
		CheckBox status;

		String[] value = new String[1];

		Button deactivateButton;

		private ValueMap properties = new ValueMap();

		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

		public UserDetailForm(final String id) {
			super(id);

			name = new TextField("name", new PropertyModel(properties, "name"));
			name.setRequired(true);
			value[0] = user.getName();
			name.setModelValue(value);
			add(name);
			add(new Label("nameLabel", "Meno *"));
			add(new FormComponentFeedbackBorder("border").add(name));

			surname = new TextField("surname", new PropertyModel(properties,
					"surname"));
			surname.setRequired(true);
			surname.setModelValue(user.getSurname());
			add(surname);
			// add(surnameFeedback = new
			// ComponentFeedbackPanel("surnameFeedback",
			// surname));
			add(new Label("surnameLabel", "Priezvisko *"));

			email = new TextField("email", new PropertyModel(properties,
					"email"));
			email.setRequired(true);
			email.setModelValue(user.getEmail());
			email.setEnabled(false);
			add(email);
			// add(addressFeedback = new
			// ComponentFeedbackPanel("addressFeedback",
			// address));
			add(new Label("emailLabel", "Email *"));

			login = new TextField("login", new PropertyModel(properties,
					"login"));
			login.setRequired(true);
			login.setModelValue(user.getLogin());
			login.setEnabled(false);
			add(login);
			// add(loginNameFeedback = new
			// ComponentFeedbackPanel("loginNameFeedback",
			// loginName));
			add(new Label("loginLabel", "Login *"));

			town = new TextField("town", new PropertyModel(properties, "town"));
			town.setRequired(false);
			value[0] = user.getTown();
			town.setModelValue(value);
			add(town);
			add(new Label("townLabel", "Mesto"));

			County okres = new County();
			county = new DropDownChoice("county", new PropertyModel(okres,
					"name"), COUNTY_LIST);
			county.setRequired(false);
			if (user.getCounty() != null)
				okres.setName(user.getCounty().getName());
			add(county);
			add(new Label("countyLabel", "Okres"));

			HandicapType postihnutie = new HandicapType();
			handicapType = new DropDownChoice("handicapType",
					new PropertyModel(postihnutie, "name"), HANDICAPTYPE_LIST);
			handicapType.setRequired(false);
			if (user.getHandicapType() != null)
				postihnutie.setName(user.getHandicapType().getName());
			add(handicapType);
			add(new Label("handicapTypeLabel", "Typ postihnutia"));

			preferRegion = new CheckBox("preferRegion", new PropertyModel(
					properties, "preferRegion"));
			preferRegion.setModelObject(user.isPreferRegion());
			add(preferRegion);
			add(new Label("preferRegionLabel", "Ur˝chlenÈ vyhæad·vanie"));

			add(new Label("registrationDateLabel", "D·tum registr·cie"));
			add(new Label("userRegistrationDateLabel", dateFormat.format(user
					.getRegistrationDate())));

			password = new PasswordTextField("password", new PropertyModel(
					properties, "password"));
			add(password);
			password.setRequired(false);
			// add(passwordFeedback = new ComponentFeedbackPanel(
			// "passwordFeedback", password));
			add(new Label("passwordLabel", "NovÈ heslo"));

			passwordAgain = new PasswordTextField("passwordAgain",
					new PropertyModel(properties, "passwordAgain"));
			add(passwordAgain);
			passwordAgain.setRequired(false);

			add(new Label("passwordAgainLabel", "NovÈ heslo znova"));

			add(submit = new Button("submit",
					new ResourceModel("button.submit")) {
				public void onSubmit() {
					userController = new RegisteredUserController();
					String passwordCheck = password.getModelObjectAsString();
					String passwordAgainCheck = passwordAgain
							.getModelObjectAsString();

					if (passwordCheck != null && passwordAgainCheck != null
							&& passwordCheck != "")
						if (checkPassword(passwordCheck, passwordAgainCheck)) {

							user.setName(name.getModelObjectAsString());
							user.setSurname(surname.getModelObjectAsString());
							user.setPassword(userController.getMD5(password.getModelObjectAsString()));
							user.setEmail(email.getModelObjectAsString());
							user.setCounty(new CountyController()
									.getCountyByName(county
											.getModelObjectAsString()));
							user.setHandicapType(new HandicapTypeController()
									.getHandicapByName(handicapType
											.getModelObjectAsString()));
							user.setPreferRegion(Boolean.valueOf(preferRegion
									.getModelObjectAsString()));
							
							if (userController.updateUser(user))
								feedbackPanel.info("Profil bol ˙speöne uloûen˝");
							else
								feedbackPanel.warn("ProblÈm pri ukladanÌ, sk˙ste neskÙr");
						} else
							feedbackPanel.warn("NerovnakÈ hesl·");
					else if (passwordCheck == "" || passwordAgainCheck == "") {
						user.setName(name.getModelObjectAsString());
						user.setSurname(surname.getModelObjectAsString());
						user.setEmail(email.getModelObjectAsString());
						user.setTown(town.getModelObjectAsString());
						user.setCounty(new CountyController()
								.getCountyByName(county
										.getModelObjectAsString()));
						user.setHandicapType(new HandicapTypeController()
								.getHandicapByName(handicapType
										.getModelObjectAsString()));
						user.setPreferRegion(Boolean.valueOf(preferRegion
								.getModelObjectAsString()));

						if (userController.updateUser(user))
							feedbackPanel.info("Profil bol ˙speöne uloûen˝");
						else
							feedbackPanel.warn("ProblÈm pri ukladanÌ, sk˙ste neskÙr");
					}
					userController = null;
				}
			});

			add(deactivateButton = new Button("deactivateButton",
					new ResourceModel("button.deactivateButton")) {

				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					String passwordCheck = password.getModelObjectAsString();
					String passwordAgainCheck = passwordAgain
							.getModelObjectAsString();

					if (passwordCheck != null && passwordAgainCheck != null
							&& passwordCheck != "")
						if (checkPassword(passwordCheck, passwordAgainCheck)) {
							try {
								userController.deactivateUser(user);
								((CoreSession) getSession()).setLoged(null);
								((CoreSession) getSession()).invalidateNow();
								getRequestCycle().setRedirect(true);
								user = null;
								login = null;
							} finally {
								setResponsePage(MainPage.class);
							}
						} else
							feedbackPanel.warn("Pred deaktiv·ciou musÌte zadaù a potvrdiù svoje prihlasovacie heslo");
					else
						feedbackPanel.warn("Pred deaktiv·ciou musÌte zadaù a potvrdiù svoje prihlasovacie heslo");
				}
			});
			deactivateButton.add(new SimpleAttributeModifier("onclick",
					"return confirm('Chcete skutoËne deaktivovaù svoj profil? Po deaktiv·cii nebude moûnÈ sa prihl·siù do aplik·cie.');"));

		}

		private boolean checkPassword(String password, String passwordAgain) {
			return password.equals(passwordAgain);
		}

	}

}
