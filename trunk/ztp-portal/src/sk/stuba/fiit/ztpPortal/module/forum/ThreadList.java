package sk.stuba.fiit.ztpPortal.module.forum;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.ThemeController;
import sk.stuba.fiit.ztpPortal.databaseController.ThreadController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.Theme;
import sk.stuba.fiit.ztpPortal.databaseModel.Thread;

public class ThreadList extends CorePage {

	private static final long serialVersionUID = 1L;
	private static CoreFeedBackPanel feedbackPanel;	
	private RegisteredUserController userController;
	private ThreadController threadController;
	private PageableListView threadListView;
	
	private String login;
	private RegisteredUser user;
	
	
	public ThreadList(Theme theme){
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;
		
		 feedbackPanel = new CoreFeedBackPanel("feedback");
		 	add(feedbackPanel);

		setPageHeaderPanel();
		
		add(themeListRepeater());
		add(getThreadListView(theme));
		
		ThreadForm newThread = new ThreadForm("threadForm",theme);
		add(newThread);
		
		//schovam form pre vlozenie novej ak nie som prihlaseny
		if(login==null)
			newThread.setVisible(false);
		
		//navigacia kde som
		Label themeLabel = new Label("themeLabel",theme.getName());
		
		add(themeLabel);

	}
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}
	
	private ListView getThreadListView(final Theme theme){
		threadController = new ThreadController();
        
        threadListView = new PageableListView("threadListView", threadController
				.getAllActiveThread(theme),15) {
       	 
       	 private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final Thread thread = (Thread) item.getModelObject();
				if (thread.getName().length()>31)
					item.add(new Label("threadName", thread.getName().substring(0,30)));
				else item.add(new Label("threadName", thread.getName()));
				item.add(new Label("commentCount", String.valueOf(threadController.getThreadCommentCount(thread))));


				Link deactivateLink = new Link("deactivate", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Thread selectedThread = (Thread) getModelObject();
						selectedThread.setState(false);
						threadController.updateThread(selectedThread);
					}
				};
				
				Link activateLink = new Link("activate", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Thread selectedThread = (Thread) getModelObject();
						selectedThread.setState(true);
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
				
				if (thread.isState()) activateLink.setVisible(false);
				else deactivateLink.setVisible(false);
				
				if (!thread.getOwner().getLogin().equals(login)){
					deactivateLink.setVisible(false);
					activateLink.setVisible(false);
				}

			}
		};
        
		add((new PagingNavigator("navigator",threadListView)));
		return threadListView;
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
					thread.setState(true);
					thread.setActive(true);
					
					threadController.saveNewThread(thread);	
					setResponsePage(new ThreadList(theme));
					feedbackPanel.info("Vložené");
				}
			});
			
			
		}
		
		
	}
	
	protected ListView themeListRepeater(){
		
		ThemeController themeController = new ThemeController();

		ListView listView = new ListView("themeListView", themeController
				.getAllActiveTheme()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final Theme theme = (Theme) item.getModelObject();
				
				Link threadLink = new Link("threadLink", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new ThreadList(theme));
					}
				};
				
				threadLink.add(new Label("themeName", theme.getName()));
				
				item.add(threadLink);

			}
		};
		return listView;
		
	}
	
}
