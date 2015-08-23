package sk.stuba.fiit.ztpPortal.module.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ImageButton;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.PageRenderer;
import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.EventController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class EventList extends CorePage{

	private PageRenderer pageRenderer;
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private EventController eventController;
	private String login;
	private RegisteredUser user;
	private long userID;

	private CmsContentController cmsControler;
	private CmsContent pageContent;
	
	private final DefaultDataTable table;
	
	private final EventProvider eventProvider;
	
	public EventList() {
		// nastavim CSS
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userID = ((CoreSession) getSession()).getUserId();
		userController = new RegisteredUserController();
		cmsControler = new CmsContentController();
		
		eventProvider = new EventProvider();
		
		if (login != null){
			user = userController.getRegisteredUserByLogin(login);
			eventProvider.setUserPreferTown(user.isPreferRegion());
			eventProvider.setUserPreferredTownFilter(user.getTown());
			eventProvider.setEventOwner(user);
		}
		else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		pageRenderer = new PageRenderer();
		setPageHeaderPanel();
		setPageNavigationPanel();
		setPageLeftNavigation();
		
		add(pageRenderer.getSearchForm());
		
		pageContent = cmsControler.getContentByName("event");

		add(new Label("cmsContent", pageContent.getContent())
				.setEscapeModelStrings(false));
		
////robim tabulku + filtre
		
		final FilterForm form = new FilterForm("filter-form", eventProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (user!=null)
					eventProvider.setUserPreferTown(false);
				table.setCurrentPage(0);
			}

		};
				
		table = new DefaultDataTable("datatable", createColumns(), eventProvider,
				10);

		table.addTopToolbar(new FilterToolbar(table, form, eventProvider));
		form.add(table);
		add(form);
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
		add(pageRenderer.getEventLink());
		add(pageRenderer.getForumLink());
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());
	}
	
	private void setPageLeftNavigation() {
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
	
	private List<IColumn> createColumns() {
		
		final List<String> TOWN_LIST = Arrays
		.asList(new CountyController().getCountyNameList());

		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new PropertyColumn(new Model("Dátum"),
				"createDate", "createDate") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "createDate";
			}

		});
		columns.add(new TextFilteredPropertyColumn(new Model("Názov"),
				"name", "name") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "name";
			}

		});

		columns.add(new ChoiceFilteredPropertyColumn(new Model("Okres"),
				"town.name", new LoadableDetachableModel() {
					/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					protected List<String> load() {
						List<String> uniqueLastNames = TOWN_LIST;
						return uniqueLastNames;
					}
				}) {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "town";
			}

		});
		
		columns.add(new TextFilteredPropertyColumn(new Model("Organizátor"),
				"owner.name", "owner.name") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "owner";
			}

		});
		
		
		columns.add(new FilteredAbstractColumn(new Model("")) {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			// return the go-and-clear filter for the filter toolbar
			public Component getFilter(String componentId, FilterForm form) {
				
				// FIXME zmienic Model na ResourceModel
				return new GoAndClearFilter(componentId, form,
						new ResourceModel("filter"), new ResourceModel("clear")){
					
					
				};

			}

			// add the UserActionsPanel to the cell item
			public void populateItem(Item cellItem, String componentId,
					IModel model) {
				cellItem.add(new ActionPanel(componentId, model));
			}
		});

		// columns.add(new AbstractColumn(new Model("Náh¾ad"))
		// {
		// public void populateItem(Item cellItem, String componentId, IModel
		// model)
		// {
		// cellItem.add(new ActionPanel(componentId, model));
		// }
		//				
		// @Override
		// public String getCssClass()
		// {
		// return "seeDetail";
		// }
		//				
		// });

		return columns;
	}

	class ActionPanel extends Panel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * @param id
		 *            component id
		 * @param model
		 *            model for contact
		 */
		public ActionPanel(String id, IModel model) {
			super(id, model);

			eventController = new EventController();

			
			
			add(new Link("Detail") {
				/**
					 * 
					 */
				private static final long serialVersionUID = 1L;

				public void onClick() {
					long eventId = (((Event) getParent().getModelObject()).getId());
					if (login != null) {
						if (eventController.isEventCreator(eventId, userID))
							//TODO
						setResponsePage(new EventViewDetail(eventId));
						else
						setResponsePage(new EventView(eventId));
					} else
						setResponsePage(new EventView(eventId));
				}
			});


		}
	}
	
}
