package sk.stuba.fiit.ztpPortal.admin;

import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import org.apache.wicket.markup.html.basic.Label;

public class ProfilePage extends AdminPage {

	private static final long serialVersionUID = 1L;
	
	public ProfilePage(RegisteredUser user) {
		
		String loginName= user.getLogin();
		String name = user.getName();
		String surname = user.getSurname();
		
		Label loginNameLabel= new Label("profileName", loginName);
		add(loginNameLabel);

		Label nameLabel= new Label("name", name);
		add(nameLabel);
		
		Label surNameLabel= new Label("surname", surname);
		add(surNameLabel);
		
	}
}
