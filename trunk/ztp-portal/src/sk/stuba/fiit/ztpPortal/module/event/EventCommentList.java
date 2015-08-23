package sk.stuba.fiit.ztpPortal.module.event;

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
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.databaseController.CommentController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Comment;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class EventCommentList extends CorePage {

	private static final long serialVersionUID = 1L;

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private CommentController commentController;
	private PageableListView commentListView;

	private String login;
	private RegisteredUser user;
	
	private Event event;
	
	private DateFormat dateFormat;

	public EventCommentList(Event event) {
		this.event=event;
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

		setPageHeaderPanel();
		setPageLeftNavigation();
		
		add(getCommentListView(event));
		
		CommentForm commentForm = new CommentForm("commentForm",event);
		add(commentForm);
		
		if (login==null)
			commentForm.setVisible(false);
		
		//navigacia kde som
		Label themeLabel = new Label("themeLabel",event.getName());
		Label threadLabel = new Label("threadLabel", "Diskusia a koment·re");
		Label threadLabelName = new Label("threadLabelName", event.getName());
		
		add(themeLabel);
		add(threadLabel);
		add(threadLabelName);
		
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}
	
	private void setPageLeftNavigation() {
		Link eventListLink = new Link("eventListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new EventList());
			}
		};
		add(eventListLink);
		
		Link newEventLink = new Link("newEventLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new EventDetail());
			}
		};
		add(newEventLink);
		
		
		add(new Link("pictureView"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new PictureViewer(event.getId()));
			} 
		});
		
	}
	
	
	private ListView getCommentListView(final Event event) {

		commentListView = new PageableListView("commentListView",
				commentController.getActiveEventComment(event), 20) {

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
						setResponsePage(new EventCommentList(event));
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
		
		public CommentForm(String id, final Event event) {
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
					comment.setEvent(event);
					comment.setOwner(user);
					
					//not user input
					
					comment.setActive(true);
					comment.setState(true);
					
					commentController.saveNewComment(comment);
					name.setModelValue("");
					setResponsePage(new EventCommentList(event));
					feedbackPanel.info("VloûenÈ");
				}
			});
			
			
		}
		
		
	}
	

}
