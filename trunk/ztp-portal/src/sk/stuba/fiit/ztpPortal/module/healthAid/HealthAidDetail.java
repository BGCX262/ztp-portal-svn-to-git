package sk.stuba.fiit.ztpPortal.module.healthAid;

import java.sql.BatchUpdateException;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.StringValidator;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.core.PageNoAdmitance;
import sk.stuba.fiit.ztpPortal.databaseController.HandicapTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.HealthAidController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.HealthAid;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class HealthAidDetail extends CorePage {

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;

	public HealthAidDetail() {
		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();

		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
		} else
			throw new RestartResponseAtInterceptPageException(
					new PageNoAdmitance(this));

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();
		setPageLeftNavigation();

		// formular vlozim

		add(new EventDetailForm("eventDetailForm"));
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}

	private void setPageLeftNavigation() {
		Link eventDetailLink = new Link("healthAidListLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new HealthAidList());
			}
		};
		add(eventDetailLink);
	}

	public final class EventDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		private TextArea textArea;
		private TinyMceBehavior tinyMCE;
		private TextField name;
		private DropDownChoice handicapType;

		private Button submit;
		private ValueMap properties = new ValueMap();

		private final List<String> HANDICAPTYPE_LIST = Arrays
				.asList(new HandicapTypeController().getHandicapNameList());

		public EventDetailForm(final String id) {
			super(id);

			name = new TextField("name", new PropertyModel(properties, "name"));
			name.setRequired(true);
			name.setLabel(new Model("Popis"));
			name.add(StringValidator.maximumLength(30));
			add(name);
			add(new Label("nameLabel", "Popis *"));
			add(new FormComponentFeedbackBorder("nameAsterix").add(name));

			handicapType = new DropDownChoice("handicapType",
					new PropertyModel(properties, "handicapType"),
					HANDICAPTYPE_LIST);
			handicapType.setRequired(true);
			handicapType.setLabel(new Model("Typ postihnutia"));
			add(handicapType);
			add(new Label("handicapTypeLabel", "Typ postihnutia *"));
			add(new FormComponentFeedbackBorder("handicapTypeAsterix").add(handicapType));

			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.add(StringValidator.maximumLength(5000));
			textArea.setEscapeModelStrings(false);
			add(textArea);

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}

		protected void onSubmit() {
			try {

				HealthAid healthAid = new HealthAid();

				healthAid.setCmsContent(textArea.getValue());
				healthAid.setName(name.getModelObjectAsString());
				healthAid.setHandicapType(new HandicapTypeController().getHandicapByName(handicapType.getModelObjectAsString()));

				// not-user entered data
				healthAid.setState(true);
				healthAid.setActive(true);
				healthAid.setOwner(user);

				HealthAidController infoController = new HealthAidController();

				if (infoController.saveNewHealthAid(healthAid)) {
					submit.setVisible(false);
					feedbackPanel.info("Inzerát úspešne vložený");
				} else
					feedbackPanel.warn("Chyba pri vkladaní");

			} catch (BatchUpdateException e) {
				System.out.println(e.getMessage());
				System.out.println(e.getNextException());
			} catch (NumberFormatException e) {

			}
		}

	}

}