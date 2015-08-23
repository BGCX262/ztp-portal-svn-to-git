package sk.stuba.fiit.ztpPortal.module.dayCare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
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
import org.apache.wicket.markup.html.form.Form;
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
import sk.stuba.fiit.ztpPortal.databaseController.AdvertTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.DayCareController;
import sk.stuba.fiit.ztpPortal.databaseController.JobSectorController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.DayCare;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class DayCareList extends CorePage {

	private static final long serialVersionUID = 1L;

	private PageRenderer pageRenderer;
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

		if (login != null){
			user = userController.getRegisteredUserByLogin(login);
			dayCareProvider.setUserPreferTown(user.isPreferRegion());
			//dayCareProvider.setUserPreferredTownFilter(user.getTown());		TODO: getCountry
			dayCareProvider.setDayCareOwner(user);
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
		
		pageContent = cmsControler.getContentByName("dayCare");

		add(new Label("cmsContent", pageContent.getContent())
				.setEscapeModelStrings(false));

		////robim tabulku + filtre
		
		final FilterForm form = new FilterForm("filter-form", dayCareProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (user!=null)
					dayCareProvider.setUserPreferTown(false);
				table.setCurrentPage(0);
			}

		};

		table = new DefaultDataTable("datatable", createColumns(), dayCareProvider,
				10);

		table.addTopToolbar(new FilterToolbar(table, form, dayCareProvider));
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
		add(pageRenderer.getDayCareListLink());
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());
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
		
		final List<String> TOWN_LIST = Arrays
		.asList(new CountyController().getCountyNameList());

		final List<String> SECTOR_LIST = Arrays
		.asList(new JobSectorController().getJobSectorNameList());

		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new ChoiceFilteredPropertyColumn(new Model("Inzerát"),
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

		columns.add(new PropertyColumn(new Model("Dátum - od"),
				"startDate", "startDate") {
			private static final long serialVersionUID = 1L;
			@Override
			public String getCssClass() {
				return "startDate";
			}

		});
		columns.add(new TextFilteredPropertyColumn(new Model("Krátky opis"),
				"shortDesc", "shortDesc") {
			private static final long serialVersionUID = 1L;
			@Override
			public String getCssClass() {
				return "description";
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

		columns.add(new PropertyColumn(new Model("Mesto"),
				"town", "town") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "town";
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
				
				
//				// FIXME zmienic Model na ResourceModel
//				return new GoAndClearFilter(componentId, form){
//					
//					@Override
//                    protected Button getClearButton()
//                    {
//                            Button result = super.getClearButton();
//                            return result;
//                    } 
//					
//					@Override
//                    protected Button getGoButton()
//                    {
//                            Button result = super.getGoButton();
//                            return result;
//                    } 
//					
//					
//				};

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
			dayCareController = new DayCareController();
			add(new Link("Detail") {
				/**
					 * 
					 */
			private static final long serialVersionUID = 1L;
//TODO!!!
			public void onClick() {
					long dayCareId = (((DayCare) getParent().getModelObject()).getId());
					if (login != null) {
						if (dayCareController.isDayCareCreator(dayCareId, userID)) {
							System.out.println("dayCareViewDetail");
							setResponsePage(new DayCareViewDetail(dayCareId));
						}
						else
							setResponsePage(new DayCareView(dayCareId));
					} else
						setResponsePage(new DayCareView(dayCareId));
				}
			});

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
