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
import sk.stuba.fiit.ztpPortal.databaseController.CommentController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.ThreadController;
import sk.stuba.fiit.ztpPortal.databaseModel.Comment;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.Theme;
import sk.stuba.fiit.ztpPortal.databaseModel.Thread;

public class CommentList extends AdminPage{
	
	private CommentController commentController;
	private String login;
	private RegisteredUser user;
	private static FeedbackPanel feedbackPanel;
	
	private ListView commentListView;
	
	public CommentList(Thread thread){
		
		login = ((AdminSession) getSession()).getLoged();
		user = new RegisteredUserController().getRegisteredUserByLogin(((AdminSession) getSession()).getLoged());
		
		Label loginNameInfoLabel= new Label("profileNameInfo", "Ste prihlásený ako "+login);
		add(loginNameInfoLabel);
         
		setNavigation();
		
         commentController = new CommentController();
         
         commentListView = new ListView("threadListView", commentController
 				.getAllComment(thread)) {
        	 
        	 private static final long serialVersionUID = 1L;

 			@Override
 			protected void populateItem(ListItem item) {
 				Comment comment = (Comment) item.getModelObject();
 				item.add(new Label("themeName", comment.getName()));

 				Link deactivateLink = new Link("deactivate", item.getModel()) {

 					private static final long serialVersionUID = 1L;

 					@Override
 					public void onClick() {
 						Comment selectedComment = (Comment) getModelObject();
 						selectedComment.setState(false);
 						commentController.updateComment(selectedComment);
 					}
 				};
 				
 				Link activateLink = new Link("activate", item.getModel()) {

 					private static final long serialVersionUID = 1L;

 					@Override
 					public void onClick() {
 						Comment selectedComment = (Comment) getModelObject();
 						selectedComment.setState(true);
 						commentController.updateComment(selectedComment);
 					}
 				};
 				
 				Label noticeCountLabel = new Label ("noticeCountLabel",String.valueOf(comment.getNotice())); 
 				
 				item.add(activateLink);
 				item.add(deactivateLink);
 				item.add(noticeCountLabel);
 				
 				if (comment.isState()) activateLink.setVisible(false);
 				else deactivateLink.setVisible(false);

 			}
 		};
         
 		add(commentListView);
		
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

}
