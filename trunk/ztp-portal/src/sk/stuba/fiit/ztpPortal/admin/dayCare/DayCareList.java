package sk.stuba.fiit.ztpPortal.admin.dayCare;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import sk.stuba.fiit.ztpPortal.admin.AdminNavigation;
import sk.stuba.fiit.ztpPortal.admin.AdminPage;
import sk.stuba.fiit.ztpPortal.admin.AdminSession;
import sk.stuba.fiit.ztpPortal.admin.NavigationPanel;
import sk.stuba.fiit.ztpPortal.databaseController.DayCareController;
import sk.stuba.fiit.ztpPortal.databaseModel.DayCare;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.module.dayCare.DayCareView;

public class DayCareList extends AdminPage {

	private static final long serialVersionUID = 1L;

	private DayCareController healthAidController;
	private String login;
	private RegisteredUser user;

	final DayCareProvider infoProvider = new DayCareProvider();

	private final DefaultDataTable table;

	public DayCareList() {

		login = ((AdminSession) getSession()).getLoged();

		Label loginNameInfoLabel = new Label("profileNameInfo",
				"Ste prihl·sen˝ ako " + login);
		add(loginNameInfoLabel);

		setNavigation();

		// //robim tabulku + filtre

		final FilterForm form = new FilterForm("filter-form", infoProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (user != null)
					infoProvider.setUserPreferTown(false);
				table.setCurrentPage(0);
			}

		};

		table = new DefaultDataTable("datatable", createColumns(),
				infoProvider, 10);

		table.addTopToolbar(new FilterToolbar(table, form, infoProvider));
		form.add(table);
		add(form);

	}

	private void setNavigation() {
		AdminNavigation adminNavigation = new AdminNavigation();

		add(adminNavigation.getUserLogOutLink());

		add(new NavigationPanel("panel"));

	}

	private List<IColumn> createColumns() {

		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new PropertyColumn(new Model("D·tum"), "creationDate",
				"creationDate") {
			private static final long serialVersionUID = 1L;

		});
		columns.add(new TextFilteredPropertyColumn(new Model("Opis"), "shortDesc",
				"shortDesc") {
			private static final long serialVersionUID = 1L;

		});


		columns.add(new TextFilteredPropertyColumn(new Model("VlastnÌk"),
				"creator.name", "creator.name") {
			private static final long serialVersionUID = 1L;

		});

		columns.add(new PropertyColumn(new Model("AktÌvne"), "state", "state") {

			public void populateItem(Item cellItem, String componentId,
					IModel model) {

				DayCare selectedLiving = ((DayCare) model.getObject());

				cellItem.add(new ActivePanel(componentId, selectedLiving));
			}

		});

		columns.add(new PropertyColumn(new Model(">>"), "detail") {

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
				return new GoAndClearFilter(componentId, form,
						new ResourceModel("filter"), new ResourceModel("clear")) {

				};

			}

			// add the UserActionsPanel to the cell item
			public void populateItem(Item cellItem, String componentId,
					IModel model) {

				DayCare selectedLiving = ((DayCare) model.getObject());
				cellItem.add(new ActionPanel(componentId, selectedLiving));
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

		public ActivePanel(String id, DayCare living) {
			super(id);

			Label aktivny = new Label("aktivny", "aktÌvny");

			Label neaktivny = new Label("neaktivny", "neaktÌvny");

			add(aktivny);
			add(neaktivny);

			if (living.isState())
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
					DayCare selectedDayCare = ((DayCare) getParent().getModelObject());
					setResponsePage(new DayCareView(selectedDayCare.getId()));
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
		public ActionPanel(String id, final DayCare living) {
			super(id);

			healthAidController = new DayCareController();

			Link deactivationLink = new Link("Deactivation") {
				private static final long serialVersionUID = 1L;

				public void onClick() {
					DayCare selectedLiving = living;// ((Job)
												// getParent().getModelObject());
					selectedLiving.setState(false);
					healthAidController.updateDayCare(selectedLiving);
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
					DayCare selectedLiving = living;
					selectedLiving.setState(true);
					healthAidController.updateDayCare(selectedLiving);
				}
			};

			activationLink.add(new SimpleAttributeModifier("onclick",
					"return confirm('éel·te si aktivovaù inzer·t ?');"));

			add(activationLink);

			activationLink.setVisible(true);
			deactivationLink.setVisible(true);

			if (living.isState())
				activationLink.setVisible(false);
			else
				deactivationLink.setVisible(false);
		}
	}

}
