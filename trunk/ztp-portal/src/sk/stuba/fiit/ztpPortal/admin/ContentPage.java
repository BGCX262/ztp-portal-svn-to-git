package sk.stuba.fiit.ztpPortal.admin;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.StringValidator;

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

        add(new NavigationPanel("panel"));

	}

	public final class ContentDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		private CmsContent mainCmsContent;
		private CmsContent jobCmsContent;
		private CmsContent registrationContent;
		private CmsContent eventCmsContent;
		private CmsContent educationCmsContent;
		private CmsContent livingCmsContent;
		private CmsContent informationCmsContent;
		private CmsContent healthAidCmsContent;
		private CmsContent forumCmsContent;
		private CmsContent dayCareCmsContent;
		
		private CmsContentController controller;

		private TextArea mainContentTextArea;
		private TextArea jobContentTextArea;
		private TextArea eventContentTextArea;
		private TextArea educationContentTextArea;
		private TextArea livingContentTextArea;
		private TextArea informationContentTextArea;
		private TextArea healthAidContentTextArea;
		private TextArea forumContentTextArea;
		private TextArea registrationContentTextArea;
		private TextArea dayCareContentTextArea;
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
			mainContentTextArea.add(StringValidator.maximumLength(5000));
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
			registrationContentTextArea.add(StringValidator.maximumLength(5000));
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

			// JOB ponuky
			jobCmsContent = controller.getContentByName("job");
			value[0] = jobCmsContent.getContent();
			jobContentTextArea = new TextArea("jobCmsContent", new Model());
			jobContentTextArea.add(StringValidator.maximumLength(5000));
			tinyMCE = new TinyMceBehavior(settings);
			jobContentTextArea.add(tinyMCE);
			jobContentTextArea.setModelValue(value);
			jobContentTextArea.setEscapeModelStrings(false);
			add(jobContentTextArea);

			add(submit = new Button("submitJobContent", new ResourceModel(
					"button.submit")) {

				public void onSubmit() {
					jobCmsContent.setContent(jobContentTextArea.getValue());
					if (controller.updateCmsContent(jobCmsContent))
					feedbackPanel
							.info("Obsah pre modul Zamestnanie bol úspešne vložený");
				}
			});
			
			// Udalosti
			eventCmsContent = controller.getContentByName("event");
			value[0] = eventCmsContent.getContent();
			eventContentTextArea = new TextArea("eventCmsContent", new Model());
			eventContentTextArea.add(StringValidator.maximumLength(5000));
			tinyMCE = new TinyMceBehavior(settings);
			eventContentTextArea.add(tinyMCE);
			eventContentTextArea.setModelValue(value);
			eventContentTextArea.setEscapeModelStrings(false);
			add(eventContentTextArea);

			add(submit = new Button("submitEventContent", new ResourceModel(
					"button.submit")) {

				public void onSubmit() {
					eventCmsContent.setContent(eventContentTextArea.getValue());
					controller.updateCmsContent(eventCmsContent);
					feedbackPanel
							.info("Obsah pre modul Udalosti bol úspešne vložený");
				}
			});

			// Vzdelavanie
			educationCmsContent = controller.getContentByName("education");
			value[0] = educationCmsContent.getContent();
			educationContentTextArea = new TextArea("educationCmsContent", new Model());
			educationContentTextArea.add(StringValidator.maximumLength(5000));
			tinyMCE = new TinyMceBehavior(settings);
			educationContentTextArea.add(tinyMCE);
			educationContentTextArea.setModelValue(value);
			educationContentTextArea.setEscapeModelStrings(false);
			add(educationContentTextArea);

			add(submit = new Button("submitEducationContent", new ResourceModel(
					"button.submit")) {

				public void onSubmit() {
					educationCmsContent.setContent(educationContentTextArea.getValue());
					controller.updateCmsContent(educationCmsContent);
					feedbackPanel
							.info("Obsah pre modul Vzdelávanie bol úspešne vložený");
				}
			});
			
			// Ubytovanie
			livingCmsContent = controller.getContentByName("living");
			value[0] = livingCmsContent.getContent();
			livingContentTextArea = new TextArea("livingCmsContent", new Model());
			livingContentTextArea.add(StringValidator.maximumLength(5000));
			tinyMCE = new TinyMceBehavior(settings);
			livingContentTextArea.add(tinyMCE);
			livingContentTextArea.setModelValue(value);
			livingContentTextArea.setEscapeModelStrings(false);
			add(livingContentTextArea);

			add(submit = new Button("submitLivingContent", new ResourceModel(
					"button.submit")) {

				public void onSubmit() {
					livingCmsContent.setContent(livingContentTextArea.getValue());
					controller.updateCmsContent(livingCmsContent);
					feedbackPanel
							.info("Obsah pre modul Ubytovanie bol úspešne vložený");
				}
			});
			
			// Informacie
			informationCmsContent = controller.getContentByName("information");
			value[0] = informationCmsContent.getContent();
			informationContentTextArea = new TextArea("informationCmsContent", new Model());
			informationContentTextArea.add(StringValidator.maximumLength(5000));
			tinyMCE = new TinyMceBehavior(settings);
			informationContentTextArea.add(tinyMCE);
			informationContentTextArea.setModelValue(value);
			informationContentTextArea.setEscapeModelStrings(false);
			add(informationContentTextArea);

			add(submit = new Button("submitInformationContent", new ResourceModel(
					"button.submit")) {

				public void onSubmit() {
					informationCmsContent.setContent(informationContentTextArea.getValue());
					controller.updateCmsContent(informationCmsContent);
					feedbackPanel
							.info("Obsah pre modul Informacie bol úspešne vložený");
				}
			});
			
			// Pomocky
			healthAidCmsContent = controller.getContentByName("healthAid");
			value[0] = healthAidCmsContent.getContent();
			healthAidContentTextArea = new TextArea("healthAidCmsContent", new Model());
			healthAidContentTextArea.add(StringValidator.maximumLength(5000));
			tinyMCE = new TinyMceBehavior(settings);
			healthAidContentTextArea.add(tinyMCE);
			healthAidContentTextArea.setModelValue(value);
			healthAidContentTextArea.setEscapeModelStrings(false);
			add(healthAidContentTextArea);

			add(submit = new Button("submitHealthAidContent", new ResourceModel(
					"button.submit")) {

				public void onSubmit() {
					healthAidCmsContent.setContent(healthAidContentTextArea.getValue());
					controller.updateCmsContent(healthAidCmsContent);
					feedbackPanel
							.info("Obsah pre modul Pomôcky bol úspešne vložený");
				}
			});
			
			// Opatrovatelstvo
			dayCareCmsContent = controller.getContentByName("dayCare");
			value[0] = dayCareCmsContent.getContent();
			dayCareContentTextArea = new TextArea("dayCareCmsContent", new Model());
			dayCareContentTextArea.add(StringValidator.maximumLength(5000));
			tinyMCE = new TinyMceBehavior(settings);
			dayCareContentTextArea.add(tinyMCE);
			dayCareContentTextArea.setModelValue(value);
			dayCareContentTextArea.setEscapeModelStrings(false);
			add(dayCareContentTextArea);

			add(submit = new Button("submitDayCareContent", new ResourceModel(
					"button.submit")) {

				public void onSubmit() {
					dayCareCmsContent.setContent(dayCareContentTextArea.getValue());
					if (controller.updateCmsContent(dayCareCmsContent))
					feedbackPanel
							.info("Obsah pre modul Opatrovate¾stvo bol úspešne vložený");
				}
			});
			
			
			// Forum
			forumCmsContent = controller.getContentByName("forum");
			value[0] = forumCmsContent.getContent();
			forumContentTextArea = new TextArea("forumCmsContent", new Model());
			forumContentTextArea.add(StringValidator.maximumLength(5000));
			tinyMCE = new TinyMceBehavior(settings);
			forumContentTextArea.add(tinyMCE);
			forumContentTextArea.setModelValue(value);
			forumContentTextArea.setEscapeModelStrings(false);
			add(forumContentTextArea);

			add(submit = new Button("submitForumContent", new ResourceModel(
					"button.submit")) {

				public void onSubmit() {
					forumCmsContent.setContent(forumContentTextArea.getValue());
					if (controller.updateCmsContent(forumCmsContent))
					feedbackPanel
							.info("Obsah pre modul Fórum bol úspešne vložený");
				}
			});
			
		}
	}

}
