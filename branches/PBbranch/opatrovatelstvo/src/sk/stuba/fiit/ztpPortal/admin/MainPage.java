package sk.stuba.fiit.ztpPortal.admin;


import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class MainPage extends AdminPage{

	private static final long serialVersionUID = 1L;
	private String login;

	public MainPage() {
		
//		WicketSession wicketSession = (WicketSession) getSession();
		
		login = ((AdminSession) getSession()).getLoged();
		System.out.println("MP je login"+login);
		
		Label loginNameInfoLabel= new Label("profileNameInfo", "Ste prihlásený ako "+login);
		add(loginNameInfoLabel);
		
		 Link logoutLink = new Link("logoutLink")
         {
             private static final long serialVersionUID = 1L;

             public void onClick()
             {
            	 ((AdminSession) getSession()).invalidateNow();
                 setResponsePage(LoginPage.class);
             }
         };
         
        
         AdminNavigation adminNavigation = new AdminNavigation();
         
         add(logoutLink);
         
         add(adminNavigation.getUserListLink());
	}
	
	
}
