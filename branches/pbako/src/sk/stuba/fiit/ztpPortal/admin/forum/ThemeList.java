package sk.stuba.fiit.ztpPortal.admin.forum;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.StringValidator;

import sk.stuba.fiit.ztpPortal.admin.AdminNavigation;
import sk.stuba.fiit.ztpPortal.admin.AdminPage;
import sk.stuba.fiit.ztpPortal.admin.AdminSession;
import sk.stuba.fiit.ztpPortal.databaseController.ThemeController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.Theme;
import sk.stuba.fiit.ztpPortal.databaseModel.Thread;

public class ThemeList extends AdminPage {

	private ThemeController themeController;
	private String login;
	private RegisteredUser user;
	private static FeedbackPanel feedbackPanel;
	
	private ListView listView;
	
	public ThemeList() {
		login = ((AdminSession) getSession()).getLoged();
		
		Label loginNameInfoLabel= new Label("profileNameInfo", "Ste prihlásený ako "+login);
		add(loginNameInfoLabel);
	    
		setNavigation();
		
		themeController = new ThemeController();

		listView = new ListView("themeListView", themeController
				.getAllTheme()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final Theme theme = (Theme) item.getModelObject();
				item.add(new Label("themeName", theme.getName()));

				Link deactivateLink = new Link("deactivate", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Theme selectedTheme = (Theme) getModelObject();
						selectedTheme.setState(false);
						themeController.updateTheme(selectedTheme);
					}
				};
				
				Link activateLink = new Link("activate", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Theme selectedTheme = (Theme) getModelObject();
						selectedTheme.setState(true);
						themeController.updateTheme(selectedTheme);
					}
				};
				
				Link threadLink = new Link("threadLink", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new ThreadList(theme));
					}
				};
				
				item.add(activateLink);
				item.add(deactivateLink);
				item.add(threadLink);
				
				if (theme.isState()) activateLink.setVisible(false);
				else deactivateLink.setVisible(false);

			}
		};

		add(listView);
		add(new ThemeForm("themeForm"));
		
		feedbackPanel = new FeedbackPanel("feedback");
	     add(feedbackPanel);

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
	
	private class ThemeForm extends Form{

		private static final long serialVersionUID = 1L;
		
		private TextField name;
		
		private Button submit;
		private ValueMap properties = new ValueMap();
		
		public ThemeForm(String id) {
			super(id);
			
			name = new TextField("name", new PropertyModel(
					properties, "name"));
			name.setRequired(true);
			name.setLabel(new Model("Názov"));
			add(name);
			add(new Label("nameLabel", "Názov"));
			
			
			add(submit = new Button("submit",
					new ResourceModel("button.submit")){
				
				public void onSubmit() {
					
					Theme theme = new Theme();
					
					theme.setName(name.getModelObjectAsString());
					themeController.saveNewTheme(theme);	
					setResponsePage(new ThemeList());
					feedbackPanel.info("Vložené");
				}
			});
			
			
		}
		
		
	}

}
