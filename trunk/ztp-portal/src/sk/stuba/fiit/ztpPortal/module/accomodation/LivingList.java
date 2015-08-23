package sk.stuba.fiit.ztpPortal.module.accomodation;

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
import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.LivingController;
import sk.stuba.fiit.ztpPortal.databaseController.LivingTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.Living;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class LivingList extends CorePage{

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private LivingController livingController;
	private String login;
	private RegisteredUser user;
	private long userID;

	private CmsContentController cmsControler;
	private CmsContent pageContent;
	
	private final DefaultDataTable table;
	
	private final LivingProvider livingProvider;
	
	public LivingList() {
		// nastavim CSS
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userID = ((CoreSession) getSession()).getUserId();
		userController = new RegisteredUserController();
		cmsControler = new CmsContentController();
		
		livingProvider = new LivingProvider();
		
		if (login != null){
			user = userController.getRegisteredUserByLogin(login);
			livingProvider.setUserPreferredCounty(user.isPreferRegion());
			livingProvider.setUserPreferredCountyFilter(user.getCounty());
			livingProvider.setLivingOwner(user);
		}
		else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();
		setPageLeftNavigation();

		
		pageContent = cmsControler.getContentByName("living");

		add(new Label("cmsContent", pageContent.getContent())
				.setEscapeModelStrings(false));
		
////robim tabulku + filtre
		
		final FilterForm form = new FilterForm("filter-form", livingProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (user!=null)
					livingProvider.setUserPreferredCounty(false);
				table.setCurrentPage(0);
			}

		};
				
		table = new DefaultDataTable("datatable", createColumns(), livingProvider,
				10);

		table.addTopToolbar(new FilterToolbar(table, form, livingProvider));
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
				setResponsePage(new LivingDetail());
			}
		};
		add(eventDetailLink);
	}
	
	private List<IColumn> createColumns() {
		
		final List<String> LIVING_TYPE_LIST =Arrays
		.asList(new LivingTypeController().getLivingTypeNameList());

		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new PropertyColumn(new Model("D·tum"),
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
		
		columns.add(new ChoiceFilteredPropertyColumn(new Model("Typ"),
		"livingType.name", new LoadableDetachableModel() {
			/**
		 * 
		 */
			private static final long serialVersionUID = 1L;

			@Override
			protected List<String> load() {
				List<String> uniqueLastNames = LIVING_TYPE_LIST;
				return uniqueLastNames;
			}
		}) {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getCssClass() {
		return "county";
	}

});
		
		columns.add(new TextFilteredPropertyColumn(new Model("Mesto"),
				"town", "town") {

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
                filter.getFilter().add(new AttributeModifier("size", true, new Model("15")));
                return filter;
            } 
			
		});

		columns.add(new TextFilteredPropertyColumn(new Model("Rozloha"),
				"size", "size") { 
			
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "size";
			}

			@Override
            public Component getFilter(final String varComponentId, final FilterForm varForm) {
                TextFilter filter = (TextFilter) super.getFilter(varComponentId, varForm);
                filter.getFilter().add(new AttributeModifier("size", true, new Model("4")));
                return filter;
            } 
			
		});
		
		columns.add(new TextFilteredPropertyColumn(new Model("Cena"),
				"price", "price") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "price";
			}

			@Override
            public Component getFilter(final String varComponentId, final FilterForm varForm) {
                TextFilter filter = (TextFilter) super.getFilter(varComponentId, varForm);
                filter.getFilter().add(new AttributeModifier("size", true, new Model("4")));
                return filter;
            } 
			
		});
		
		columns.add(new TextFilteredPropertyColumn(new Model("PoËet izieb"),
				"roomCount", "roomCount") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "roomCount";
			}

			@Override
            public Component getFilter(final String varComponentId, final FilterForm varForm) {
                TextFilter filter = (TextFilter) super.getFilter(varComponentId, varForm);
                filter.getFilter().add(new AttributeModifier("size", true, new Model("7")));
                return filter;
            } 
			
		});
		
		columns.add(new TextFilteredPropertyColumn(new Model("Vytvoril"),
				"owner.login", "owner.login") {
			private static final long serialVersionUID = 1L;
			
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
						new ResourceModel("filter"), new ResourceModel("clear")){

							private static final long serialVersionUID = 1L;

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

			livingController = new LivingController();

			add(new Link("Detail") {
				/**
					 * 
					 */
				private static final long serialVersionUID = 1L;

				public void onClick() {
					long livingId = (((Living) getParent().getModelObject()).getId());
					if (login != null) {
						if (livingController.isLivingCreator(livingId, userID))
							//TODO
						setResponsePage(new LivingViewDetail(livingId));
						else
						setResponsePage(new LivingView(livingId));
					} else
						setResponsePage(new LivingView(livingId));
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

			livingController = new LivingController();

			Link deleteLink =new Link("Delete") {
				/**
					 * 
					 */
				private static final long serialVersionUID = 1L;

				 @Override
				  public void onClick() {
					 ((Living)model.getObject()).setActive(false);
					 livingController.updateLiving((Living) model.getObject());
				  }
				};	
			
			deleteLink.add(new SimpleAttributeModifier("onclick", "return confirm('Chcete odstr·niù inzer·t?');"));
			
			add(deleteLink);
			
			if (!((Living)model.getObject()).getOwner().getLogin().equals(login) || !((Living)model.getObject()).isActive()){
				deleteLink.setVisible(false);
			}
		}
	}
	
}
