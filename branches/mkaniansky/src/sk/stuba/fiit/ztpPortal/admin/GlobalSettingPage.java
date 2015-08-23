package sk.stuba.fiit.ztpPortal.admin;

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
import sk.stuba.fiit.ztpPortal.databaseModel.GlobalSetting;
import sk.stuba.fiit.ztpPortal.server.IndexPortal;

public class GlobalSettingPage extends AdminPage {

	private static final long serialVersionUID = 1L;
	private String login;
	private GlobalSettingController globalSettingController;
	private GlobalSetting globalSettingJob;

	private static FeedbackPanel feedbackPanel;

	public GlobalSettingPage() {

		login = ((AdminSession) getSession()).getLoged();

		Label loginNameInfoLabel = new Label("profileNameInfo",
				"Ste prihlásený ako " + login);
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

		add(adminNavigation.getUserListLink());

		add(adminNavigation.getContentListLink());

		add(adminNavigation.getJobListLink());

		add(adminNavigation.getGlobalSettingLink());

		add(adminNavigation.getEventListLink());

		add(adminNavigation.getForumListLink());

	}

	private class SettingsForm extends Form {

		TextField jobDeactivation;

		Button submit;
		private ValueMap properties = new ValueMap();
		String[] value = new String[1];
		
		private Button indexButton;

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
			jobDeactivation.setLabel(new Model("Deaktivácia zamestnaní"));
			add(jobDeactivation);
			add(new Label("jobDeactivationLabel", "Deaktivácia zamestnaní"));

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
			
			indexButton = new Button("indexButton",
					new ResourceModel("button.submit")){

						private static final long serialVersionUID = 1L;

						public void onSubmit(){
							IndexPortal indexPortal = new IndexPortal();
							indexPortal.indexCommentData();
							indexPortal.indexPortalContent();
						}
			};
			add(indexButton);

		}

		protected void onSubmit() {

			try {

				globalSettingJob.setValue(jobDeactivation
						.getModelObjectAsString());

				globalSettingController.updateGlobalSetting(globalSettingJob); // aktualizoval
				// som
				// JOB
				// nastavenie

				feedbackPanel.info("Úspešne uložené");
			} catch (Exception e) {
				// TODO treba zobrazil alebo nieco pod.
				feedbackPanel.info("Vyskytla sa chyba");
			}
		}

	}

}
