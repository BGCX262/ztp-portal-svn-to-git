package sk.stuba.fiit.ztpPortal.module.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
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
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
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
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseController.JobSectorController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class JobList extends CorePage {

	private static final long serialVersionUID = 1L;

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private JobController jobController;
	private String login;
	private RegisteredUser user;
	private long userID;

	private CmsContentController cmsControler;
	private CmsContent pageContent;

	final JobProvider jobProvider = new JobProvider();

	private final DefaultDataTable table;

	public JobList() {

		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));

		login = ((CoreSession) getSession()).getLoged();
		userID = ((CoreSession) getSession()).getUserId();
		userController = new RegisteredUserController();
		cmsControler = new CmsContentController();

		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
			jobProvider.setUserPreferTown(user.isPreferRegion());
			jobProvider.setUserPreferredTownFilter(user.getCounty());
			jobProvider.setJobOwner(user);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();

		setPageLeftNavigation();

		pageContent = cmsControler.getContentByName("job");

		add(new Label("cmsContent", pageContent.getContent())
				.setEscapeModelStrings(false));

		// //robim tabulku + filtre

		final FilterForm form = new FilterForm("filter-form", jobProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (user != null)
					jobProvider.setUserPreferTown(false);
				table.setCurrentPage(0);
			}

		};

		table = new DefaultDataTable("datatable", createColumns(), jobProvider,
				10);

		table.addTopToolbar(new FilterToolbar(table, form, jobProvider));
		form.add(table);
		add(form);

	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel", user);
		add(panel);
	}

	// tu nastavim navigacne tlacitka vlavo v menu
	private void setPageLeftNavigation() {
		Link jobDetailLink = new Link("newJobLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new JobDetail());
			}
		};
		add(jobDetailLink);
		
		Link jobListLink = new Link("jobListLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new JobList());
			}
		};
		add(jobListLink);
	}

	private List<IColumn> createColumns() {

		final List<String> ADVERTTYPE_LIST = Arrays
				.asList(new AdvertTypeController().getAdvertTypeNameList());

		final List<String> TOWN_LIST = Arrays.asList(new CountyController()
				.getCountyNameList());

		final List<String> SECTOR_LIST = Arrays
				.asList(new JobSectorController().getJobSectorNameList());

		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new ChoiceFilteredPropertyColumn(new Model("Inzer·t"),
				"advertType.name", new LoadableDetachableModel() {
					/**
						 * 
						 */
					private static final long serialVersionUID = 1L;

					@Override
					protected List<String> load() {
						List<String> uniqueLastNames = ADVERTTYPE_LIST;
						return uniqueLastNames;
					}
				}) {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "advertType";
			}
		});

		columns.add(new PropertyColumn(new Model("D·tum"), "creationDate",
				"creationDate") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "creationDate";
			}

		});
		columns.add(new TextFilteredPropertyColumn(new Model("Opis"),
				"specification", "specification") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "specification";
			}

			@Override
			public Component getFilter(final String varComponentId,
					final FilterForm varForm) {
				TextFilter filter = (TextFilter) super.getFilter(
						varComponentId, varForm);
				filter.getFilter().add(
						new AttributeModifier("size", true, new Model("10")));
				return filter;
			}

		});

		columns.add(new ChoiceFilteredPropertyColumn(new Model("Okres"),
				"county.name", new LoadableDetachableModel() {
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
		columns.add(new ChoiceFilteredPropertyColumn(new Model("Oblasù"),
				"jobSector.name", new LoadableDetachableModel() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					protected List<String> load() {
						List<String> uniqueLastNames = SECTOR_LIST;
						return uniqueLastNames;
					}
				}) {

			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "jobSector";
			}
		});

		columns.add(new TextFilteredPropertyColumn(new Model("Vytvoril"),
				"creator.login", "creator.login") {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getFilter(final String varComponentId,
					final FilterForm varForm) {
				TextFilter filter = (TextFilter) super.getFilter(
						varComponentId, varForm);
				filter.getFilter().add(
						new AttributeModifier("size", true, new Model("7")));
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

			jobController = new JobController();

			final long jobId = (((Job) getModelObject()).getId());

			PageParameters pars = new PageParameters();
			pars.add("id", String.valueOf(jobId));

			if (login != null) {
				if (jobController.isJobCreator(jobId, userID)) {
					add(new Link("Detail") {

						public void onClick() {
							setResponsePage(new JobViewDetail(jobId));
						}
					});
				} else
					add(new BookmarkablePageLink("Detail", JobView.class, pars));
			} else
				add(new BookmarkablePageLink("Detail", JobView.class, pars));

			// add(new Link("Detail") {
			// /**
			// *
			// */
			// private static final long serialVersionUID = 1L;
			//
			// public void onClick() {
			// long jobId = (((Job) getParent().getModelObject()).getId());
			// if (login != null) {
			// if (jobController.isJobCreator(jobId, userID))
			// setResponsePage(new JobViewDetail(jobId));
			// else
			// setResponsePage(new JobView(jobId));
			// } else
			// setResponsePage(new JobView(jobId));
			// }
			// });

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

			jobController = new JobController();

			Link deleteLink = new Link("Delete") {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					((Job) model.getObject()).setActive(false);
					jobController.updateJob((Job) model.getObject());
				}
			};

			deleteLink.add(new SimpleAttributeModifier("onclick",
					"return confirm('Chcete odstr·niù inzer·t?');"));

			add(deleteLink);

			if (!((Job) model.getObject()).getCreator().getLogin()
					.equals(login)
					|| !((Job) model.getObject()).isActive()) {
				deleteLink.setVisible(false);
			}
		}
	}

}
