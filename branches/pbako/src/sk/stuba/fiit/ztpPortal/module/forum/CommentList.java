package sk.stuba.fiit.ztpPortal.module.forum;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.StringValidator;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.PageRenderer;
import sk.stuba.fiit.ztpPortal.databaseController.CommentController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.ThemeController;
import sk.stuba.fiit.ztpPortal.databaseController.ThreadController;
import sk.stuba.fiit.ztpPortal.databaseModel.Comment;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.Thread;

public class CommentList extends CorePage {

	private static final long serialVersionUID = 1L;

	private PageRenderer pageRenderer;
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private ThemeController themeController;
	private ThreadController threadController;
	private CommentController commentController;
	private PageableListView commentListView;

	private String login;
	private RegisteredUser user;
	
	private DateFormat dateFormat;

	public CommentList(Thread thread) {
		dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		
		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		commentController = new CommentController();

		pageRenderer = new PageRenderer();
		setPageHeaderPanel();
		setPageNavigationPanel();

		add(pageRenderer.getSearchForm());
		
		add(new ThemeList().themeListRepeater());
		add(getCommentListView(thread));
		add(new CommentForm("commentForm",thread));
		
		//navigacia kde som
		Label themeLabel = new Label("themeLabel",thread.getTheme().getName());
		Label threadLabel = new Label("threadLabel", thread.getName());
		Label threadLabelName = new Label("threadLabelName", thread.getName());
		
		add(themeLabel);
		add(threadLabel);
		add(threadLabelName);
		
	}

	private void setPageHeaderPanel() {
		pageRenderer.setUser(user);
		Form loginForm = pageRenderer.getLoginForm();
		add(loginForm);
		Form statusForm = pageRenderer.getStatusForm();
		add(statusForm);
		
		if (user==null) pageRenderer.disableStatusForm();
		else pageRenderer.disableLoginForm();
	}

	private void setPageNavigationPanel() {
		add(pageRenderer.getForumLink());
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());
	}
	
	
	private ListView getCommentListView(final Thread thread) {

		commentListView = new PageableListView("commentListView",
				commentController.getActiveComment(thread), 20) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final Comment comment = (Comment) item.getModelObject();
				item.add(new Label ("commentAuthor", comment.getOwner().getLogin()));
				item.add(new Label ("commentCreateDate", dateFormat.format(comment.getCreateDate())));
				item.add(new MultiLineLabel ("commentName", comment.getName()));

				Link noticeLink = new Link("noticeLink", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						commentController.noticeUp(comment);
						feedbackPanel.info("Koment·r bol upozornen˝");
					}
				};
				
				noticeLink.add(new SimpleAttributeModifier("onclick",
	            "return confirm('Chcete upozorniù na koment·r?');"));
				item.add(noticeLink);
				
				Link deactivateLink = new Link("deactivate", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Comment selectedComment = (Comment) getModelObject();
						selectedComment.setState(false);
						commentController.updateComment(selectedComment);
						setResponsePage(new CommentList(thread));
						feedbackPanel.info("Koment·r bol ˙speöne zmazan˝");
					}
				};

				deactivateLink.add(new SimpleAttributeModifier("onclick",
	            "return confirm('Chcete zmazaù dan˝ koment·r?');"));
				item.add(deactivateLink);
				
				
				
				if (!comment.getOwner().getLogin().equals(login)){
					deactivateLink.setVisible(false);
				}

			}
		};
		add((new PagingNavigator("navigator",commentListView)));
		return commentListView;
	}

	private class CommentForm extends Form{

		private static final long serialVersionUID = 1L;
		
		private TextArea name;
		
		private Button submit;
		private ValueMap properties = new ValueMap();
		
		public CommentForm(String id, final Thread thread) {
			super(id);
			
			name = new TextArea("name", new PropertyModel(
					properties, "name"));
			name.setRequired(true);
			name.add(StringValidator.maximumLength(500));
			name.setLabel(new Model("Text"));
			add(name);
			add(new Label("nameLabel", "Text"));
			
			
			add(submit = new Button("submit",
					new ResourceModel("button.submit")){
				
				public void onSubmit() {
					
					Comment comment = new Comment();
					
					comment.setName(name.getModelObjectAsString());
					comment.setThread(thread);
					comment.setOwner(user);
					
					//not user input
					
					comment.setActive(true);
					comment.setState(true);
					
					commentController.saveNewComment(comment);
					name.setModelValue("");
					setResponsePage(new CommentList(thread));
					feedbackPanel.info("VloûenÈ");
				}
			});
			
			
		}
		
		
	}
	

}
