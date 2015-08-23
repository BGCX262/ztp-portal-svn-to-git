package sk.stuba.fiit.ztpPortal.module.accomodation;

import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.AbstractReadOnlyModel;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.databaseController.LivingController;
import sk.stuba.fiit.ztpPortal.databaseController.PictureController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Living;
import sk.stuba.fiit.ztpPortal.databaseModel.Picture;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.ImageResource;

public class PictureViewer extends CorePage {

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;

	private LivingController livingController;
	private PictureController pictureController;
	private Living living;
	private PageableListView pictureListView;

	public PictureViewer(long id) {
		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		livingController = new LivingController();
		pictureController = new PictureController();

		living = livingController.getLivingByLivingId(id);

		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();
		setPageLeftNavigation();
		
		add(getPictureListView(living));

		ModalWindowPage modal = new ModalWindowPage(living);

		add(modal.getLabel());
		add(modal.getModal());
		add(modal.getLink());
		if (living.getOwner().getLogin()!=login) modal.setLinkVisible(false);
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}

	private void setPageLeftNavigation() {
		Link eventListLink = new Link("eventListLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new LivingList());
			}
		};
		add(eventListLink);
		
		Link eventDetailLink = new Link("newEventLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new LivingDetail());
			}
		};
		add(eventDetailLink);
		
	}

	private ListView getPictureListView(final Living living) {

		List <Picture> pictureList = null;
		
		if (login!=null) pictureList = pictureController.getActiveLivingPicture(living);
		else pictureList = pictureController.getActiveLivingStatePicture(living);
		
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
