package sk.stuba.fiit.ztpPortal.module.healthAid;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.HealthAidController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.HealthAid;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class HealthAidList extends CorePage{

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private HealthAidController healthAidController;
	private String login;
	private RegisteredUser user;
	private long userID;

	private CmsContentController cmsControler;
	private CmsContent pageContent;
	
	private final DefaultDataTable table;
	
	private final HealthAidProvider infoProvider;
	
	public HealthAidList() {
		// nastavim CSS
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userID = ((CoreSession) getSession()).getUserId();
		userController = new RegisteredUserController();
		cmsControler = new CmsContentController();
		
		infoProvider = new HealthAidProvider();
		
		if (login != null){
			user = userController.getRegisteredUserByLogin(login);
			infoProvider.setLivingOwner(user);
		}
		else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();
		setPageLeftNavigation();
		
		pageContent = cmsControler.getContentByName("information");

		add(new Label("cmsContent", pageContent.getContent())
				.setEscapeModelStrings(false));
		
////robim tabulku + filtre
		
		final FilterForm form = new FilterForm("filter-form", infoProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				table.setCurrentPage(0);
			}

		};
				
		table = new DefaultDataTable("datatable", createColumns(), infoProvider,
				10);

		table.addTopToolbar(new FilterToolbar(table, form, infoProvider));
		form.add(table);
		add(form);
	}
	
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}
	
	private void setPageLeftNavigation() {
		Link eventDetailLink = new Link("newHealthAidLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new HealthAidDetail());
			}
		};
		add(eventDetailLink);
	}
	
	private List<IColumn> createColumns() {

		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new PropertyColumn(new Model("D·tum"),
				"createDate", "createDate") {
			private static final long serialVersionUID = 1L;
		});
		
		
		columns.add(new TextFilteredPropertyColumn(new Model("N·zov"),
				"name", "name") {
			private static final long serialVersionUID = 1L;
		});
		
		columns.add(new TextFilteredPropertyColumn(new Model("Vloûil"),
				"owner.login", "owner.login") {
			private static final long serialVersionUID = 1L;
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

			healthAidController = new HealthAidController();

			add(new Link("Detail") {
				/**
					 * 
					 */
				private static final long serialVersionUID = 1L;

				public void onClick() {
					long informationId = (((HealthAid) getParent().getModelObject()).getId());
					if (login != null) {
						if (healthAidController.isHealthAidCreator(informationId, userID))
							setResponsePage(new HealthAidViewDetail(informationId));
						else
							setResponsePage(new HealthAidView(informationId));
					} else
						setResponsePage(new HealthAidView(informationId));
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

			healthAidController = new HealthAidController();

			Link deleteLink =new Link("Delete") {
				/**
					 * 
					 */
				private static final long serialVersionUID = 1L;

				 @Override
				  public void onClick() {
					 ((HealthAid)model.getObject()).setActive(false);
					 healthAidController.updateHealthAid((HealthAid) model.getObject());
				  }
				};	
			
			deleteLink.add(new SimpleAttributeModifier("onclick", "return confirm('Chcete odstr·niù inzer·t?');"));
			
			add(deleteLink);
			
			if (!((HealthAid)model.getObject()).getOwner().getLogin().equals(login) || !((HealthAid)model.getObject()).isActive()){
				deleteLink.setVisible(false);
			}
		}
	}
	
}
