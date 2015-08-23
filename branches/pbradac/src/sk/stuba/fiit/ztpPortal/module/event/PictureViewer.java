package sk.stuba.fiit.ztpPortal.module.event;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Resource;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.PageRenderer;
import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.EventController;
import sk.stuba.fiit.ztpPortal.databaseController.PictureController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.Picture;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.ImageResource;

public class PictureViewer extends CorePage {

	private PageRenderer pageRenderer;
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private long userID;

	private CmsContentController cmsControler;
	private CmsContent pageContent;
	private EventController eventController;
	private PictureController pictureController;
	private Event event;
	private PageableListView pictureListView;

	public PictureViewer(long id) {
		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userID = ((CoreSession) getSession()).getUserId();
		userController = new RegisteredUserController();
		eventController = new EventController();
		pictureController = new PictureController();

		event = eventController.getEventByEventId(id);

		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		pageRenderer = new PageRenderer();
		setPageHeaderPanel();
		setPageNavigationPanel();
		setPageLeftNavigation();

		add(pageRenderer.getSearchForm());
		
		add(getPictureListView(event));

		ModalWindowPage modal = new ModalWindowPage(event);

		add(modal.getLabel());
		add(modal.getModal());
		add(modal.getLink());
		if (event.getOwner().getLogin()!=login) modal.setLinkVisible(false);
	}

	private void setPageHeaderPanel() {
		pageRenderer.setUser(user);
		Form loginForm = pageRenderer.getLoginForm();
		add(loginForm);
		Form statusForm = pageRenderer.getStatusForm();
		add(statusForm);

		if (user == null)
			pageRenderer.disableStatusForm();
		else
			pageRenderer.disableLoginForm();

	}

	private void setPageNavigationPanel() {
		add(pageRenderer.getEventLink());
		add(pageRenderer.getForumLink());
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());
	}

	private void setPageLeftNavigation() {
		Link eventListLink = new Link("eventListLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new EventList());
			}
		};
		add(eventListLink);
		
		add(new Link("pictureView") {

			@Override
			public void onClick() {
				setResponsePage(new PictureViewer(event.getId()));
			}
		});
		
		Link eventDetailLink = new Link("newEventLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new EventDetail());
			}
		};
		add(eventDetailLink);
		
	}

	private ListView getPictureListView(final Event event) {

		List <Picture> pictureList = null;
		
		if (login!=null) pictureList = pictureController.getActivePicture(event);
		else pictureList = pictureController.getActiveStatePicture(event);
		
		pictureListView = new PageableListView("pictureListView",
				pictureList, 10) {
	
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final Picture picture = (Picture) item.getModelObject();

				Link deactivateLink = new Link("deactivateLink", item
						.getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						picture.setState(false);
						pictureController.updatePicture(picture);
					}

				};

				Link activateLink = new Link("activateLink", item.getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						picture.setState(true);
						pictureController.updatePicture(picture);
					}

				};

				byte[] bytes = picture.getImage();
				new ImageResource(bytes, "png");

				NonCachingImage image = new NonCachingImage("eventPicture",
						new AbstractReadOnlyModel() {
							private static final long serialVersionUID = 1L;

							@Override
							public Object getObject() {
								byte[] bytes = picture.getImage();

								Object oo = new ImageResource(bytes, "png");
								return oo;
							}
						});

				item.add(image);
				item.add(deactivateLink);
				item.add(activateLink);

				if (!picture.getOwner().getLogin().equals(login)) {
					deactivateLink.setVisible(false);
					activateLink.setVisible(false);
				}

				if (picture.isState())
					activateLink.setVisible(false);
				else
					deactivateLink.setVisible(false);

			}
		};
		add((new PagingNavigator("navigator", pictureListView)));
		return pictureListView;
	}

}
