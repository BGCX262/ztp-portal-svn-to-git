package sk.stuba.fiit.ztpPortal.core;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import sk.stuba.fiit.ztpPortal.databaseController.CommentController;
import sk.stuba.fiit.ztpPortal.databaseController.CourseController;
import sk.stuba.fiit.ztpPortal.databaseController.DayCareController;
import sk.stuba.fiit.ztpPortal.databaseController.EventController;
import sk.stuba.fiit.ztpPortal.databaseController.HealthAidController;
import sk.stuba.fiit.ztpPortal.databaseController.InformationController;
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseController.LivingController;
import sk.stuba.fiit.ztpPortal.databaseModel.Comment;
import sk.stuba.fiit.ztpPortal.databaseModel.Course;
import sk.stuba.fiit.ztpPortal.databaseModel.DayCare;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.HealthAid;
import sk.stuba.fiit.ztpPortal.databaseModel.Information;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.Living;
import sk.stuba.fiit.ztpPortal.module.accomodation.LivingView;
import sk.stuba.fiit.ztpPortal.module.dayCare.DayCareView;
import sk.stuba.fiit.ztpPortal.module.education.CourseView;
import sk.stuba.fiit.ztpPortal.module.event.EventView;
import sk.stuba.fiit.ztpPortal.module.forum.CommentList;
import sk.stuba.fiit.ztpPortal.module.healthAid.HealthAidView;
import sk.stuba.fiit.ztpPortal.module.information.InfoView;
import sk.stuba.fiit.ztpPortal.module.job.JobView;

public class NewArticles extends CorePage {

	private static final long serialVersionUID = 1L;
	private static JobController jobController;
	private static CommentController commentController;
	private static EventController eventController;
	private static LivingController livingController;
	private static CourseController courseController;
	private static InformationController informationController;
	private static HealthAidController healthAidController;
	private static DayCareController dayCareController;
	
	public static ListView getNewJobArticle() {
		//jobController = new JobController();
		
		ListView listView = new ListView("jobNewArticles",jobController.getNewJob()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				Job job = (Job) item.getModelObject();
				item.add(new Label("specification", job.getSpecification()));
				item.add(new Label("town", job.getTown()));
				
				PageParameters pars = new PageParameters();
				pars.add("id", String.valueOf(job.getId()));
				item.add (new BookmarkablePageLink("detail", JobView.class,pars));
//				item.add(new Link("detail", item.getModel()) {
//
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public void onClick() {
//						Job selected = (Job) getModelObject();
//						setResponsePage(new JobView(selected.getId()));
//					}
//				});

			}
		};
		
		return listView;
	}
	
	public static ListView getNewForumComment() {
		//commentController = new CommentController();
		
		ListView listView = new ListView("forumNewComment",commentController.getNewComment()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				Comment comment = (Comment) item.getModelObject();
				item.add(new Label("commentOwner", comment.getOwner().getLogin()));
				item.add(new Label("commentTheme", comment.getThread().getTheme().getName()));
				item.add(new Label("commentThread", comment.getThread().getName()));
				item.add(new Label("commentContent", comment.getName()));
				
				item.add(new Link("detail", item.getModel()) {

					private static final long serialVersionUID = 1L;

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
	
	public static ListView getNewEvent() {
		//eventController = new EventController();
		
		ListView listView = new ListView("eventNewArticles",eventController.getNewEvent()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem item) {
				Event event = (Event) item.getModelObject();
				item.add(new Label("eventOwner", event.getOwner().getLogin()));
				item.add(new Label("eventTown", event.getTown()));
				item.add(new Label("eventName", event.getName()));
				
				item.add(new Link("detail", item.getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new EventView(((Event) getModelObject()).getId()));
					}
				});

			}
		};
		
		return listView;
	}
	
	public static ListView getNewLiving() {
		//livingController = new LivingController();
		
		ListView listView = new ListView("livingNewArticles",livingController.getNewLiving()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem item) {
				Living living = (Living) item.getModelObject();
				item.add(new Label("livingType", living.getStuffType().getName()));
				item.add(new Label("livingTown", living.getTown()));
				item.add(new Label("livingPrice", String.valueOf(living.getPrice())));
				
				item.add(new Link("detail", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new LivingView(((Living) getModelObject()).getId()));
					}
				});

			}
		};
		
		return listView;
	}
	
	public static ListView getNewEducation() {
		//courseController = new CourseController();
		
		ListView listView = new ListView("educationNewArticles",courseController.getNewCourse()) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem item) {
				Course course = (Course) item.getModelObject();
				item.add(new Label("courseName", course.getName()));
				item.add(new Label("courseTown", course.getTown()));
				item.add(new Label("coursePrice", String.valueOf(course.getPrice())));
				
				item.add(new Link("detail", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new CourseView(((Course) getModelObject()).getId()));
					}
				});

			}
		};
		
		return listView;
	}
	
	public static ListView getNewInformation() {
		//informationController = new InformationController();
		
		ListView listView = new ListView("informationNewArticles",informationController.getNewInformation()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem item) {
				Information information = (Information) item.getModelObject();
				item.add(new Label("informationName", information.getName()));
				item.add(new Label("informationOwner", information.getOwner().getLogin()));
				
				item.add(new Link("detail", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new InfoView(((Information) getModelObject()).getId()));
					}
				});

			}
		};
		
		return listView;
	}
	
	public static ListView getNewHealthAid() {
		//healthAidController = new HealthAidController();
		
		ListView listView = new ListView("healthAidNewArticles",healthAidController.getNewHealthAid()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem item) {
				HealthAid healthAid = (HealthAid) item.getModelObject();
				item.add(new Label("healthAidName", healthAid.getName()));
				item.add(new Label("healthAidHandicapType", healthAid.getHandicapType().getName()));
				
				item.add(new Link("detail", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new HealthAidView(((HealthAid) getModelObject()).getId()));
					}
				});

			}
		};
		
		return listView;
	}
	
	public static ListView getNewDayCare() {
		//dayCareController = new DayCareController();
		
		ListView listView = new ListView("dayCareNewArticles",dayCareController.getNewDayCare()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem item) {
				DayCare dayCare = (DayCare) item.getModelObject();
				item.add(new Label("dayCareName", dayCare.getShortDesc()));
				item.add(new Label("dayCareHandicapType", dayCare.getTown()));
				
				item.add(new Link("detail", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new DayCareView(((DayCare) getModelObject()).getId()));
					}
				});

			}
		};
		
		return listView;
	}
	
}
