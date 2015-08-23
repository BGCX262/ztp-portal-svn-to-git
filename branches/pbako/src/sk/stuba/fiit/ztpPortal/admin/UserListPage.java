package sk.stuba.fiit.ztpPortal.admin;

import java.util.ArrayList;
import java.util.List;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class UserListPage extends AdminPage {

	private static final long serialVersionUID = 1L;
	private String login;

	private List<RegisteredUser> registeredUserList;
	private RegisteredUserController userController;

	public UserListPage() {

		login = ((AdminSession) getSession()).getLoged();

		Label loginNameInfoLabel = new Label("profileNameInfo",
				"Ste prihlásený ako " + login);
		add(loginNameInfoLabel);

		setNavigation();

		add(dataTablePage());

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

	private List<RegisteredUser> getRegisteredUserList() {
		RegisteredUserController userController = new RegisteredUserController();
		return userController.getAllRegisteredUser();
	}

	public DefaultDataTable dataTablePage() {
		final UserProvider userProvider = new UserProvider();

		List<IColumn> columns = new ArrayList<IColumn>();

		// columns.add(new PropertyColumn(new Model("Inzerát"),
		// "advertType.name", "advertType.name"){
		//        	
		// @Override
		// public String getCssClass()
		// {
		// return "advertType";
		// }
		//        	
		//        	
		// });
		columns.add(new PropertyColumn(new Model("Meno"), "name", "name"));
		columns.add(new PropertyColumn(new Model("Priezvisko"), "surname",
				"surname"));
		columns.add(new PropertyColumn(new Model("Mesto"), "town.name",
				"town.name"));
		columns.add(new PropertyColumn(new Model("Postihnutie"),
				"handicapType.name", "handicapType.name"));

		columns.add(new AbstractColumn(new Model("Editácia")) {
			public void populateItem(Item cellItem, String componentId,
					IModel model) {
				cellItem.add(new ActionPanel(componentId, model));
			}
		});

		DefaultDataTable table = new DefaultDataTable("datatable", columns,
				userProvider, 10);

		return table;
		// add(table);
	}

	class ActionPanel extends Panel {
		/**
		 * @param id
		 *            component id
		 * @param model
		 *            model for contact
		 */
		public ActionPanel(String id, IModel model) {
			super(id, model);

			userController = new RegisteredUserController();

			add(new Link("Detail") {
				public void onClick() {
					long userId = (((RegisteredUser) getParent()
							.getModelObject()).getId());
					setResponsePage(new UserDetail(
							((RegisteredUser) getParent().getModelObject())
									.getLogin()));
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
