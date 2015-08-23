package sk.stuba.fiit.ztpPortal.module.healthAid;

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
import sk.stuba.fiit.ztpPortal.databaseModel.HealthAid;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class HealthAidCommentList extends CorePage {

	private static final long serialVersionUID = 1L;

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private CommentController commentController;
	private PageableListView commentListView;

	private String login;
	private RegisteredUser user;
	
	private DateFormat dateFormat;

	public HealthAidCommentList(HealthAid information) {
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
		
		add(getCommentListView(information));
		
		CommentForm commentForm = new CommentForm("commentForm",information);
		add(commentForm);
		
		if (login==null)
			commentForm.setVisible(false);
		
		//navigacia kde som
		Label themeLabel = new Label("themeLabel",information.getName());
		Label threadLabel = new Label("threadLabel", "Diskusia a koment·re");
		Label threadLabelName = new Label("threadLabelName", information.getName());
		
		add(themeLabel);
		add(threadLabel);
		add(threadLabelName);
		
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}

	private void setPageLeftNavigation() {
		Link eventDetailLink = new Link("healthAidListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new HealthAidList());
			}
		};
		add(eventDetailLink);
		
		Link newEventLink = new Link("newHealthAidLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new HealthAidDetail());
			}
		};
		add(newEventLink);
		
	}
	
	private ListView getCommentListView(final HealthAid information) {

		commentListView = new PageableListView("commentListView",
				commentController.getActiveHealthAidComment(information), 20) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final Comment comment = (Comment) item.getModelObject();
				System.out.println("WWWW "+comment);
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
						setResponsePage(new HealthAidCommentList(information));
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
		
		public CommentForm(String id, final HealthAid information) {
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
					comment.setHealthAid(information);
					comment.setOwner(user);
					
					//not user input
					comment.setActive(true);
					comment.setState(true);
					
					commentController.saveNewComment(comment);
					name.setModelValue("");
					setResponsePage(new HealthAidCommentList(information));
					feedbackPanel.info("VloûenÈ");
				}
			});
			
			
		}
		
		
	}
	

}
