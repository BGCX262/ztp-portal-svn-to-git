package sk.stuba.fiit.ztpPortal.module.dayCare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
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
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilter;
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
import sk.stuba.fiit.ztpPortal.databaseController.AdvertTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.DayCareController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.DayCare;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class DayCareList extends CorePage {

	private static final long serialVersionUID = 1L;

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private DayCareController dayCareController;
	private String login;
	private RegisteredUser user;
	private long userID;

	private CmsContentController cmsControler;
	private CmsContent pageContent;

	final DayCareProvider dayCareProvider = new DayCareProvider();

	private final DefaultDataTable table;

	public DayCareList() {

		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userID = ((CoreSession) getSession()).getUserId();
		userController = new RegisteredUserController();
		cmsControler = new CmsContentController();

		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
			dayCareProvider.setUserPreferTown(user.isPreferRegion());
			dayCareProvider.setUserPreferredTownFilter(user.getCounty());
			dayCareProvider.setDayCareOwner(user);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();

		setPageLeftNavigation();

		pageContent = cmsControler.getContentByName("dayCare");

		add(new Label("cmsContent", pageContent.getContent())
				.setEscapeModelStrings(false));

		// //robim tabulku + filtre

		final FilterForm form = new FilterForm("filter-form", dayCareProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (user != null)
					dayCareProvider.setUserPreferTown(false);
				table.setCurrentPage(0);
			}

		};

		table = new DefaultDataTable("datatable", createColumns(),
				dayCareProvider, 10);

		table.addTopToolbar(new FilterToolbar(table, form, dayCareProvider));
		form.add(table);
		add(form);

	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel", user);
		add(panel);
	}

	// tu nastavim navigacne tlacitka vlavo v menu
	private void setPageLeftNavigation() {
		Link dayCareDetailLink = new Link("newDayCareLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new DayCareDetail());
			}
		};
		add(dayCareDetailLink);
	}

	private List<IColumn> createColumns() {

		final List<String> ADVERTTYPE_LIST = Arrays
				.asList(new AdvertTypeController().getAdvertTypeNameList());

		final List<String> TOWN_LIST = Arrays.asList(new CountyController()
				.getCountyNameList());

		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new ChoiceFilteredPropertyColumn(new Model("Inzer·t"),
				"advertType.name", new LoadableDetachableModel() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<String> load() {
						List<String> uniqueLastNames = ADVERTTYPE_LIST;
						return uniqueLastNames;
					}
				}) {
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "advertType";
			}
		});

		columns.add(new PropertyColumn(new Model("D·tum - od "), "startDate",
				"startDate") {
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "startDate";
			}

		});
		columns.add(new TextFilteredPropertyColumn(new Model("Opis"),
				"shortDesc", "shortDesc") {
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "description";
			}
			
			@Override
            public Component getFilter(final String varComponentId, final FilterForm varForm) {
                TextFilter filter = (TextFilter) super.getFilter(varComponentId, varForm);
                filter.getFilter().add(new AttributeModifier("size", true, new Model("13")));
                return filter;
            } 
		});

		columns.add(new ChoiceFilteredPropertyColumn(new Model("Okres"),
				"county.name", new LoadableDetachableModel() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<String> load() {
						List<String> uniqueLastNames = TOWN_LIST;
						return uniqueLastNames;
					}
				}) {
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "town";
			}
		});

		columns.add(new TextFilteredPropertyColumn(new Model("Mesto"), "town",
				"town") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "town";
			}
			
			@Override
            public Component getFilter(final String varComponentId, final FilterForm varForm) {
                TextFilter filter = (TextFilter) super.getFilter(varComponentId, varForm);
                filter.getFilter().add(new AttributeModifier("size", true, new Model("10")));
                return filter;
            } 

		});

		columns.add(new TextFilteredPropertyColumn(new Model("Vytvoril"),
				"creator.login", "creator.login") {
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "owner";
			}
			
			@Override
            public Component getFilter(final String varComponentId, final FilterForm varForm) {
                TextFilter filter = (TextFilter) super.getFilter(varComponentId, varForm);
                filter.getFilter().add(new AttributeModifier("size", true, new Model("7")));
                return filter;
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
						new ResourceModel("filter"), new ResourceModel("clear")) {

				};

			}

			// add the UserActionsPanel to the cell item
			public void populateItem(Item cellItem, String componentId,
					IModel model) {
				cellItem.add(new ActionPanel(componentId, model));
			}
		});

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
			dayCareController = new DayCareController();
			add(new Link("Detail") {
				/**
					 * 
					 */
				private static final long serialVersionUID = 1L;

				public void onClick() {
					long dayCareId = (((DayCare) getParent().getModelObject())
							.getId());
					if (login != null) {
						if (dayCareController.isDayCareCreator(dayCareId,
								userID)) {
							System.out.println("dayCareViewDetail");
							setResponsePage(new DayCareViewDetail(dayCareId));
						} else
							setResponsePage(new DayCareView(dayCareId));
					} else
						setResponsePage(new DayCareView(dayCareId));
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

			dayCareController = new DayCareController();

			Link deleteLink = new Link("Delete") {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					((DayCare) model.getObject()).setActive(false);
					dayCareController.updateDayCare((DayCare) model.getObject());
				}
			};

			deleteLink.add(new SimpleAttributeModifier("onclick",
					"return confirm('Chcete odstr·niù inzer·t?');"));

			add(deleteLink);

			if (!((DayCare) model.getObject()).getCreator().getLogin()
					.equals(login)
					|| !((DayCare) model.getObject()).isActive()) {
				deleteLink.setVisible(false);
			}
		}
	}
}
