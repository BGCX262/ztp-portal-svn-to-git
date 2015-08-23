package sk.stuba.fiit.ztpPortal.admin;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class MainPage extends AdminPage{

	private static final long serialVersionUID = 1L;
	private String login;

	public MainPage() {
		
		login = ((AdminSession) getSession()).getLoged();
		
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
         
        
        add(new NavigationPanel("panel"));
         
         
//         AdminNavigation adminNavigation = new AdminNavigation();
         
         add(logoutLink);
         
//         add(adminNavigation.getUserListLink());
//         
//         add(adminNavigation.getContentListLink());
//         
//         add(adminNavigation.getJobListLink());
//         
//         add(adminNavigation.getGlobalSettingLink());
//         
//         add(adminNavigation.getForumListLink());
//         
//         add(adminNavigation.getEventListLink());
//         
//         add(adminNavigation.getEducationListLink());
//         
//         add(adminNavigation.getLivingListLink());
//         
//         add(adminNavigation.getInformationListLink());
//         
//         add(adminNavigation.getHealthAidListLink());
//         
//         add(adminNavigation.getDayCareListLink());
         
	}
	
	
}
