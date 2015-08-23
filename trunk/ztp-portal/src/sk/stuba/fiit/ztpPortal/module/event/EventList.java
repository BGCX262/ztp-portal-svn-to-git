package sk.stuba.fiit.ztpPortal.module.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
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
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.EventController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.Information;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class EventList extends CorePage{

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
			eventProvider.setUserPreferredTownFilter(user.getCounty());
			eventProvider.setEventOwner(user);
		}
		else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();
		setPageLeftNavigation();
		
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
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
		
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
	}
	
	private List<IColumn> createColumns() {
		
		final List<String> COUNTY_LIST = Arrays
		.asList(new CountyController().getCountyNameList());

		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new PropertyColumn(new Model("D·tum"),
				"createDate", "createDate") {
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "createDate";
			}

		});
		columns.add(new TextFilteredPropertyColumn(new Model("N·zov"),
				"name", "name") {
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "name";
			}

		});

		columns.add(new ChoiceFilteredPropertyColumn(new Model("Okres"),
				"county.name", new LoadableDetachableModel() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<String> load() {
						List<String> uniqueLastNames = COUNTY_LIST;
						return uniqueLastNames;
					}
				}) {

			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "town";
			}

		});
		
		columns.add(new TextFilteredPropertyColumn(new Model("Organiz·tor"),
				"owner.login", "owner.login") {

			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "owner";
			}

		});
		
		
		columns.add(new AbstractColumn(new Model("")) {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			// add the UserActionsPanel to the cell item
			public void populateItem(Item cellItem, String componentId,
					IModel model) {
				cellItem.add(new ActionDeletePanel(componentId, model));
			}
		});
		
		columns.add(new FilteredAbstractColumn(new Model("")) {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			// return the go-and-clear filter for the filter toolbar
			public Component getFilter(String componentId, FilterForm form) {
			
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

		// columns.add(new AbstractColumn(new Model("N·hæad"))
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

		class ActionDeletePanel extends Panel {
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
		public ActionDeletePanel(String id, final IModel model) {
			super(id, model);

			eventController = new EventController();

			Link deleteLink =new Link("Delete") {
				/**
					 * 
					 */
				private static final long serialVersionUID = 1L;

				 @Override
				  public void onClick() {
					 ((Event)model.getObject()).setActive(false);
					 eventController.updateEvent((Event) model.getObject());
				  }
				};	
			
			deleteLink.add(new SimpleAttributeModifier("onclick", "return confirm('Chcete odstr·niù inzer·t?');"));
			
			add(deleteLink);
			
			if (!((Event)model.getObject()).getOwner().getLogin().equals(login) || !((Event)model.getObject()).isActive()){
				deleteLink.setVisible(false);
			}
		}
		}
	}

		
		
