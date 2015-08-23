package sk.stuba.fiit.stuba.ztpPortal.admin.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import sk.stuba.fiit.ztpPortal.admin.AdminNavigation;
import sk.stuba.fiit.ztpPortal.admin.AdminPage;
import sk.stuba.fiit.ztpPortal.admin.AdminSession;
import sk.stuba.fiit.ztpPortal.databaseController.EventController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.module.event.EventView;

public class EventList extends AdminPage {

	private static final long serialVersionUID = 1L;

	private EventController eventController;
	private String login;
	private RegisteredUser user;

	final EventProvider eventProvider = new EventProvider();

	private final DefaultDataTable table;

	public EventList() {

		login = ((AdminSession) getSession()).getLoged();

		Label loginNameInfoLabel = new Label("profileNameInfo",
				"Ste prihl·sen˝ ako " + login);
		add(loginNameInfoLabel);

		setNavigation();

		// //robim tabulku + filtre

		final FilterForm form = new FilterForm("filter-form", eventProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (user != null)
					eventProvider.setUserPreferTown(false);
				table.setCurrentPage(0);
			}

		};

		table = new DefaultDataTable("datatable", createColumns(),
				eventProvider, 10);

		table.addTopToolbar(new FilterToolbar(table, form, eventProvider));
		form.add(table);
		add(form);

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

	private List<IColumn> createColumns() {

		final List<String> TOWN_LIST = Arrays.asList(new CountyController()
				.getCountyNameList());

		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new PropertyColumn(new Model("D·tum"), "createDate",
				"createDate") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "createDate";
			}

		});
		columns.add(new TextFilteredPropertyColumn(new Model("N·zov"), "name",
				"name") {

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

		columns.add(new TextFilteredPropertyColumn(new Model("Organiz·tor"),
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

		columns.add(new PropertyColumn(new Model("AktÌvne"), "state", "state") {

			public void populateItem(Item cellItem, String componentId,
					IModel model) {

				Event selectedEvent = ((Event) model.getObject());

				cellItem.add(new ActivePanel(componentId, selectedEvent));
			}

		});

		columns.add(new PropertyColumn(new Model(">>"), "detail", "detail") {

			public void populateItem(Item cellItem, String componentId,
					IModel model) {

				cellItem.add(new DetailPanel(componentId, model));
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
						new ResourceModel("filter"), new ResourceModel("clear")) {

				};

			}

			// add the UserActionsPanel to the cell item
			public void populateItem(Item cellItem, String componentId,
					IModel model) {

				Event selectedEvent = ((Event) model.getObject());
				cellItem.add(new ActionPanel(componentId, selectedEvent));
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

	class ActivePanel extends Panel {
		private static final long serialVersionUID = 1L;

		public ActivePanel(String id, Event event) {
			super(id);

			Label aktivny = new Label("aktivny", "aktÌvny");

			Label neaktivny = new Label("neaktivny", "neaktÌvny");

			add(aktivny);
			add(neaktivny);

			if (event.isState())
				neaktivny.setVisible(false);
			else
				aktivny.setVisible(false);

		}

	}

	class DetailPanel extends Panel {

		private static final long serialVersionUID = 1L;

		public DetailPanel(String id, IModel model) {
			super(id, model);

			Link eventViewLink = new Link("EventViewLink") {

				private static final long serialVersionUID = 1L;

				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					tag.put("target", "_blank");
				}

				public void onClick() {
					Event selectedEvent = ((Event) getParent().getModelObject());
					setResponsePage(new EventView(selectedEvent.getId()));
				}

			};

			add(eventViewLink);
		}
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
		public ActionPanel(String id, final Event event) {
			super(id);

			eventController = new EventController();

			Link deactivationLink = new Link("Deactivation") {
				private static final long serialVersionUID = 1L;

				public void onClick() {
					Event selectedEvent = event;// ((Job)
												// getParent().getModelObject());
					selectedEvent.setState(false);
					eventController.updateEvent(selectedEvent);
				}
			};

			deactivationLink.add(new SimpleAttributeModifier("onclick",
					"return confirm('éel·te si deaktivovaù inzer·t ?');"));

			// if (((Job) getParent().getModelObject()).isState())
			deactivationLink.setAutoEnable(false);
			add(deactivationLink);

			Link activationLink = new Link("Activation") {
				/**
					 * 
					 */
				private static final long serialVersionUID = 1L;

				public void onClick() {
					Event selectedJob = event;
					selectedJob.setState(true);
					eventController.updateEvent(selectedJob);
				}
			};

			activationLink.add(new SimpleAttributeModifier("onclick",
					"return confirm('éel·te si aktivovaù inzer·t ?');"));

			add(activationLink);

			activationLink.setVisible(true);
			deactivationLink.setVisible(true);

			if (event.isState())
				activationLink.setVisible(false);
			else
				deactivationLink.setVisible(false);

			// jobController = null;
			// SubmitLink removeLink = new SubmitLink("remove", form)
			// {
			// public void onSubmit()
			// {};
			// };
			// removeLink.setDefaultFormProcessing(false);
			// add(removeLink);
		}
	}

}
