package sk.stuba.fiit.ztpPortal.admin;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import wicket.contrib.tinymce.TinyMceBehavior;
import wicket.contrib.tinymce.settings.TinyMCESettings;

public class ContentPage extends AdminPage {
	private static final long serialVersionUID = 1L;
	private String login;

	private static FeedbackPanel feedbackPanel;

	public ContentPage() {

		login = ((AdminSession) getSession()).getLoged();

		Label loginNameInfoLabel = new Label("profileNameInfo",
				"Ste prihlásený ako " + login);
		add(loginNameInfoLabel);

		setNavigation();

		feedbackPanel = new FeedbackPanel("feedback");
		add(feedbackPanel);

		add(new ContentDetailForm("contentForm"));

	}

	private void setNavigation() {
		AdminNavigation adminNavigation = new AdminNavigation();

		add(adminNavigation.getUserLogOutLink());

		add(adminNavigation.getUserListLink());

		add(adminNavigation.getContentListLink());

		add(adminNavigation.getJobListLink());

		add(adminNavigation.getGlobalSettingLink());

		add(adminNavigation.getEventListLink());

		add(adminNavigation.getForumListLink());

	}

	public final class ContentDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		private CmsContent mainCmsContent;
		private CmsContent jobCmsContent;
		private CmsContent registrationContent;

		private CmsContentController controller;

		private TextArea mainContentTextArea;
		private TextArea jobContentTextArea;
		private TextArea registrationContentTextArea;
		private TinyMceBehavior tinyMCE;
		private TinyMCESettings settings;

		private Button submit;

		private String[] value;

		public ContentDetailForm(final String id) {
			super(id);

			controller = new CmsContentController();

			settings = new TinyMCESettings(TinyMCESettings.Theme.advanced);

			value = new String[1];
			mainCmsContent = controller.getContentByName("main");
			jobCmsContent = controller.getContentByName("job");
			registrationContent = controller.getContentByName("registration");

			value[0] = mainCmsContent.getContent();
			mainContentTextArea = new TextArea("mainCmsContent", new Model());
			tinyMCE = new TinyMceBehavior(settings);
			mainContentTextArea.add(tinyMCE);
			mainContentTextArea.setModelValue(value);
			mainContentTextArea.setEscapeModelStrings(false);
			add(mainContentTextArea);

			add(submit = new Button("submitMainContent", new ResourceModel(
					"button.submit")) {

				public void onSubmit() {
					mainCmsContent.setContent(mainContentTextArea.getValue());
					controller.updateCmsContent(mainCmsContent);
					feedbackPanel
							.info("Obsah pre Úvodnú stránku bol úspešne vložený");
				}
			});

			value[0] = registrationContent.getContent();
			registrationContentTextArea = new TextArea("registrationContent",
					new Model());
			tinyMCE = new TinyMceBehavior(settings);
			registrationContentTextArea.add(tinyMCE);
			registrationContentTextArea.setModelValue(value);
			registrationContentTextArea.setEscapeModelStrings(false);
			add(registrationContentTextArea);

			add(submit = new Button("submitRegistrationContent",
					new ResourceModel("button.submit")) {

				public void onSubmit() {
					registrationContent.setContent(registrationContentTextArea
							.getValue());
					controller.updateCmsContent(registrationContent);
					feedbackPanel
							.info("Obsah pre stránku Registrácia bol úspešne vložený");
				}
			});

			jobCmsContent = controller.getContentByName("job");
			value[0] = jobCmsContent.getContent();
			jobContentTextArea = new TextArea("jobCmsContent", new Model());
			tinyMCE = new TinyMceBehavior(settings);
			jobContentTextArea.add(tinyMCE);
			jobContentTextArea.setModelValue(value);
			jobContentTextArea.setEscapeModelStrings(false);
			add(jobContentTextArea);

			add(submit = new Button("submitJobContent", new ResourceModel(
					"button.submit")) {

				public void onSubmit() {
					jobCmsContent.setContent(jobContentTextArea.getValue());
					controller.updateCmsContent(jobCmsContent);
					feedbackPanel
							.info("Obsah pre modul Zamestnanie bol úspešne vložený");
				}
			});

		}
	}

}
