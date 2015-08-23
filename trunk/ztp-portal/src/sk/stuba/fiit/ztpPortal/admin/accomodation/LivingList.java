package sk.stuba.fiit.ztpPortal.admin.accomodation;

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
import sk.stuba.fiit.ztpPortal.databaseController.LivingController;
import sk.stuba.fiit.ztpPortal.databaseModel.Living;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.module.accomodation.LivingView;

public class LivingList extends AdminPage {

	private static final long serialVersionUID = 1L;

	private LivingController livingController;
	private String login;
	private RegisteredUser user;

	final LivingProvider livingProvider = new LivingProvider();

	private final DefaultDataTable table;

	public LivingList() {

		login = ((AdminSession) getSession()).getLoged();

		Label loginNameInfoLabel = new Label("profileNameInfo",
				"Ste prihl·sen˝ ako " + login);
		add(loginNameInfoLabel);

		setNavigation();

		// //robim tabulku + filtre

		final FilterForm form = new FilterForm("filter-form", livingProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (user != null)
					livingProvider.setUserPreferTown(false);
				table.setCurrentPage(0);
			}

		};

		table = new DefaultDataTable("datatable", createColumns(),
				livingProvider, 10);

		table.addTopToolbar(new FilterToolbar(table, form, livingProvider));
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

				Living selectedLiving = ((Living) model.getObject());

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

				Living selectedLiving = ((Living) model.getObject());
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

		public ActivePanel(String id, Living living) {
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
					Living selectedLiving = ((Living) getParent().getModelObject());
					setResponsePage(new LivingView(selectedLiving.getId()));
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
		public ActionPanel(String id, final Living living) {
			super(id);

			livingController = new LivingController();

			Link deactivationLink = new Link("Deactivation") {
				private static final long serialVersionUID = 1L;

				public void onClick() {
					Living selectedLiving = living;// ((Job)
												// getParent().getModelObject());
					selectedLiving.setState(false);
					livingController.updateLiving(selectedLiving);
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
					Living selectedLiving = living;
					selectedLiving.setState(true);
					livingController.updateLiving(selectedLiving);
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
