package sk.stuba.fiit.ztpPortal.admin;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import sk.stuba.fiit.ztpPortal.databaseController.GlobalSettingController;
import sk.stuba.fiit.ztpPortal.databaseController.InformationController;
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseModel.GlobalSetting;
import sk.stuba.fiit.ztpPortal.server.IndexPortal;

public class GlobalSettingPage extends AdminPage {

	private static final long serialVersionUID = 1L;
	private String login;
	private GlobalSettingController globalSettingController;
	private GlobalSetting globalSettingJob;
	private GlobalSetting globalSettingOther;

	private static FeedbackPanel feedbackPanel;

	public GlobalSettingPage() {

		login = ((AdminSession) getSession()).getLoged();

		Label loginNameInfoLabel = new Label("profileNameInfo",
				"Ste prihl·sen˝ ako " + login);
		add(loginNameInfoLabel);

		Link logoutLink = new Link("logoutLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				((AdminSession) getSession()).invalidateNow();
				setResponsePage(LoginPage.class);
			}
		};

		feedbackPanel = new FeedbackPanel("feedback");
		add(feedbackPanel);

		setNavigation();

		add(new SettingsForm("globalSettingForm"));

	}

	private void setNavigation() {
		AdminNavigation adminNavigation = new AdminNavigation();

		add(adminNavigation.getUserLogOutLink());

		add(new NavigationPanel("panel"));

	}

	private class SettingsForm extends Form {

		private static final long serialVersionUID = 1L;

		TextField jobDeactivation;
		TextField otherDeactivation;

		Button submit;
		private ValueMap properties = new ValueMap();
		String[] value = new String[1];

		private Button indexButton;

		private Button deleteAllDeactivatedContent;

		public SettingsForm(String id) {
			super(id);

			globalSettingController = new GlobalSettingController();

			globalSettingJob = globalSettingController
					.getSettingByName("jobDeactivation");

			value[0] = globalSettingJob.getValue();
			jobDeactivation = new TextField("jobDeactivation",
					new PropertyModel(properties, "jobDeactivation"),
					Integer.class);
			jobDeactivation.setModelValue(value);
			jobDeactivation.setRequired(true);
			jobDeactivation
					.setLabel(new Model(
							"Deaktiv·cia zamestnanÌ, kurzov, ubytovanÌ, opatrovateæov"));
			add(jobDeactivation);
			add(new Label("jobDeactivationLabel",
					"Deaktiv·cia zamestnanÌ, kurzov, ubytovanÌ, opatrovateæov"));

			globalSettingOther = globalSettingController
					.getSettingByName("otherDeactivation");

			value[0] = globalSettingOther.getValue();
			otherDeactivation = new TextField("otherDeactivation",
					new PropertyModel(properties, "otherDeactivation"),
					Integer.class);
			otherDeactivation.setModelValue(value);
			otherDeactivation.setRequired(true);
			otherDeactivation.setLabel(new Model(
					"Deaktiv·cia info, pomÙcok, udalostÌ"));
			add(otherDeactivation);
			add(new Label("otherDeactivationLabel",
					"Deaktiv·cia info, pomÙcok, udalostÌ"));

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));

			indexButton = new Button("indexButton", new ResourceModel(
					"button.submit")) {

				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					IndexPortal indexPortal = new IndexPortal();
					indexPortal.indexCommentData();
					indexPortal.indexPortalContent();
					indexPortal.indexJobData();
					indexPortal.indexEventData();
					indexPortal.indexLivingData();
					indexPortal.indexInformationData();
					indexPortal.indexHealthAidData();
					indexPortal.indexSchoolData();
					indexPortal.indexCourseData();
					indexPortal.indexDayCareData();
					feedbackPanel.info("Index·cia dokonËen·");
				}
			};
			add(indexButton);

			deleteAllDeactivatedContent = new Button("deactivateButton",
					new ResourceModel("button.submit")) {

				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					new InformationController()
							.deleteAllDeacivatedInformation();
					new JobController().deleteAllDeactivatedJob();

					feedbackPanel.info("Deaktivova˝ obsah bol zmazan˝");
				}
			};
			deleteAllDeactivatedContent.add(new SimpleAttributeModifier("onclick", "return confirm('SkutoËne chcete vymazaù vöetok deaktivovan˝ obsah? Oper·ciu nie je moûnÈ vr·tiù.');"));
			add(deleteAllDeactivatedContent);

		}

		protected void onSubmit() {

			try {

				globalSettingJob.setValue(jobDeactivation
						.getModelObjectAsString());

				globalSettingOther.setValue(otherDeactivation
						.getModelObjectAsString());

				globalSettingController.updateGlobalSetting(globalSettingJob); // aktualizoval
				// som
				// JOB
				// nastavenie
				globalSettingController.updateGlobalSetting(globalSettingOther); // ostatne

				feedbackPanel.info("⁄speöne uloûenÈ");
			} catch (Exception e) {
				// TODO treba zobrazil alebo nieco pod.
				feedbackPanel.info("Vyskytla sa chyba");
			}
		}

	}

}
