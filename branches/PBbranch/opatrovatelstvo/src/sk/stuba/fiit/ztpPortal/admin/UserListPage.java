package sk.stuba.fiit.ztpPortal.admin;

import java.util.List;

import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

public class UserListPage extends AdminPage {

	private static final long serialVersionUID = 1L;
	private String login;
	
	private List<RegisteredUser> registeredUserList;
	
	public UserListPage (){
		((AdminSession) getSession()).bind();
		
		System.out.println("LLLL "+((AdminSession)getSession()).getLoged());
		System.out.println(")))) "+((AdminSession) getSession()).isTemporary());
		System.out.println("LLLL "+((AdminSession)getSession()));
		
	login = ((AdminSession) getSession()).getLoged();
	
	Label loginNameInfoLabel= new Label("profileNameInfo", "Ste prihlásený ako "+login);
	add(loginNameInfoLabel);
    
     AdminNavigation adminNavigation = new AdminNavigation();
     
     add(adminNavigation.getUserLogOutLing());
     
     add(adminNavigation.getUserListLink());
	
     
     add(new ListView("userList", getRegisteredUserList()) {  

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem item) {   
				RegisteredUser user = (RegisteredUser) item.getModelObject(); 
	             item.add(new Label("userName", user.getName()));          
	             item.add(new Label("userSurname", user.getSurname())); 	
	             item.add(new Label("userLogin", user.getLogin())); 
	             item.add(new Label("userTown", user.getTown())); 
	             item.add(new Label("userRegion", user.getRegion())); 
	             item.add(new Label("userHandicapType", user.getHandicapType()));
	             
	             item.add(new Link("detail", item.getModel()) {           
	                                                 
	                 @Override                                         
	                 public void onClick() {       
	                	 RegisteredUser selected = (RegisteredUser) getModelObject(); 
	                	 setResponsePage(new UserDetail(selected.getLogin()));
	                	 selected = null;
	                 }                                                 
	             });                                                   
	          }
	      });
	
	}
	
	private List<RegisteredUser> getRegisteredUserList(){
		RegisteredUserController userController = new RegisteredUserController();
		return userController.getAllRegisteredUser();
	}
}
