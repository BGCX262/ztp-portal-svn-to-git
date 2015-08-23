package sk.stuba.fiit.ztpPortal.admin.forum;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import sk.stuba.fiit.ztpPortal.admin.AdminNavigation;
import sk.stuba.fiit.ztpPortal.admin.AdminPage;
import sk.stuba.fiit.ztpPortal.admin.AdminSession;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.ThreadController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.Theme;
import sk.stuba.fiit.ztpPortal.databaseModel.Thread;

public class ThreadList extends AdminPage{
	
	private ThreadController threadController;
	private String login;
	private RegisteredUser user;
	private static FeedbackPanel feedbackPanel;
	
	private ListView threadListView;
	
	public ThreadList(final Theme theme){
		
		login = ((AdminSession) getSession()).getLoged();
		user = new RegisteredUserController().getRegisteredUserByLogin(((AdminSession) getSession()).getLoged());
		
		Label loginNameInfoLabel= new Label("profileNameInfo", "Ste prihlásený ako "+login);
		add(loginNameInfoLabel);
	    
		setNavigation();
         
         threadController = new ThreadController();
         
         threadListView = new ListView("threadListView", threadController
 				.getAllThread(theme)) {
        	 
        	 private static final long serialVersionUID = 1L;

 			@Override
 			protected void populateItem(ListItem item) {
 				final Thread thread = (Thread) item.getModelObject();
 				item.add(new Label("threadName", thread.getName()));

 				
 				Link deactivateLink = new Link("deactivate", item.getModel()) {

 					private static final long serialVersionUID = 1L;

 					@Override
 					public void onClick() {
 						Thread selectedThread = (Thread) getModelObject();
 						selectedThread.setActive(false);
 						threadController.updateThread(selectedThread);
 					}
 				};
 				
 				Link activateLink = new Link("activate", item.getModel()) {

 					private static final long serialVersionUID = 1L;

 					@Override
 					public void onClick() {
 						Thread selectedThread = (Thread) getModelObject();
 						selectedThread.setActive(true);
 						threadController.updateThread(selectedThread);
 					}
 				};
 				
 				Link threadLink = new Link("threadDetail", item.getModel()) {

 					private static final long serialVersionUID = 1L;

 					@Override
 					public void onClick() {
 						setResponsePage(new CommentList(thread));
 					}
 				};
 				
 				item.add(activateLink);
 				item.add(deactivateLink);
 				item.add(threadLink);
 				
 				if (thread.isActive()) activateLink.setVisible(false);
 				else deactivateLink.setVisible(false);

 			}
 		};
         
 		add(threadListView);
		add(new ThreadForm("threadForm",theme));
		
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
	
	private class ThreadForm extends Form{

		private static final long serialVersionUID = 1L;
		
		private TextField name;
		
		private Button submit;
		private ValueMap properties = new ValueMap();
		
		public ThreadForm(String id, final Theme theme) {
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
					
					Thread thread = new Thread();
					
					thread.setName(name.getModelObjectAsString());
					thread.setTheme(theme);
					thread.setOwner(user);
					
					threadController.saveNewThread(thread);	
					setResponsePage(new ThreadList(theme));
					feedbackPanel.info("Vložené");
				}
			});
			
			
		}
		
		
	}
	

}
