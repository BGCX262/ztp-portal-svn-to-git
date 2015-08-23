package sk.stuba.fiit.ztpPortal.admin;

import java.util.ArrayList;
import java.util.List;

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

import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class UserListPage extends AdminPage {

	private static final long serialVersionUID = 1L;
	private String login;

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

		add(new NavigationPanel("panel"));

	}


	public DefaultDataTable dataTablePage() {
		final UserProvider userProvider = new UserProvider();

		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new PropertyColumn(new Model("Meno"), "name", "name"));
		columns.add(new PropertyColumn(new Model("Priezvisko"), "surname",
				"surname"));
		columns.add(new PropertyColumn(new Model("Mesto"), "town",
				"town"));
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

			add(new Link("Detail") {
				public void onClick() {
					long userId = (((RegisteredUser) getParent()
							.getModelObject()).getId());
					setResponsePage(new UserDetail(
							((RegisteredUser) getParent().getModelObject())
									.getLogin()));
				}
			});
		}
	}

}
