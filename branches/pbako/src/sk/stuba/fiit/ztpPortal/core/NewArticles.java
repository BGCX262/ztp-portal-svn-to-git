package sk.stuba.fiit.ztpPortal.core;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import sk.stuba.fiit.ztpPortal.databaseController.CommentController;
import sk.stuba.fiit.ztpPortal.databaseController.EventController;
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseModel.Comment;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.module.forum.CommentList;
import sk.stuba.fiit.ztpPortal.module.job.JobView;

public class NewArticles extends CorePage {

	private static final long serialVersionUID = 1L;
	private JobController jobController;
	private CommentController commentController;
	private EventController eventController;
	
	public ListView getNewJobArticle() {
		jobController = new JobController();
		
		ListView listView = new ListView("jobNewArticles",jobController.getNewJob()) {
			
			@Override
			protected void populateItem(ListItem item) {
				Job job = (Job) item.getModelObject();
				item.add(new Label("jobType", job.getAdvertType().getName()));
				item.add(new Label("specification", job.getSpecification()));
				item.add(new Label("town", job.getTown()));
				item.add(new Link("detail", item.getModel()) {
					
					@Override
					public void onClick() {
						Job selected = (Job) getModelObject();
						setResponsePage(new JobView(selected.getId()));
					}
				});

			}
		};
		
		return listView;
	}
	
	public ListView getNewForumComment() {
		commentController = new CommentController();
		
		ListView listView = new ListView("forumNewComment",commentController.getNewComment()) {
			
			@Override
			protected void populateItem(ListItem item) {
				Comment comment = (Comment) item.getModelObject();
				item.add(new Label("commentOwner", comment.getOwner().getLogin()));
				item.add(new Label("commentTheme", comment.getThread().getTheme().getName()));
				item.add(new Label("commentThread", comment.getThread().getName()));
				item.add(new Label("commentContent", comment.getName()));
				
				item.add(new Link("detail", item.getModel()) {
					
					@Override
					public void onClick() {
						Comment selected = (Comment) getModelObject();
						setResponsePage(new CommentList(selected.getThread()));
					}
				});

			}
		};
		
		return listView;
	}
	
	public ListView getNewEvent() {
		eventController = new EventController();
		
		ListView listView = new ListView("eventNewArticles",eventController.getNewEvent()) {
			
			@Override
			protected void populateItem(ListItem item) {
				Event event = (Event) item.getModelObject();
				item.add(new Label("eventOwner", event.getOwner().getLogin()));
				item.add(new Label("eventTown", event.getTown().getName()));
				item.add(new Label("eventName", event.getName()));
				
				item.add(new Link("detail", item.getModel()) {
					
					@Override
					public void onClick() {
						Event selected = (Event) getModelObject();
	//TODO					setResponsePage(new EventList());
					}
				});

			}
		};
		
		return listView;
	}
	
	
}
