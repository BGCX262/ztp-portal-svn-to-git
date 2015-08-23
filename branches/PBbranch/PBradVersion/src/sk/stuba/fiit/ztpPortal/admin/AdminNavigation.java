package sk.stuba.fiit.ztpPortal.admin;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class AdminNavigation extends AdminPage{

	private static final long serialVersionUID = 1L;

	public Link getUserLogOutLing(){
		
		Link logoutLink = new Link("logoutLink")
	     {
	         private static final long serialVersionUID = 1L;

	         public void onClick()
	         {
	        	 
	        	 ((AdminSession) getSession()).invalidateNow();
	             setResponsePage(LoginPage.class);
	         }
	     };
	    return logoutLink;
	}
	
	public Link getUserListLink (){
	
	Link userListLink = new Link("userListLink")
    {
        private static final long serialVersionUID = 1L;

        public void onClick()
        {
    //   	 ((WicketSession) getSession()).invalidateNow();
//         UserListPage page = new UserListPage();   
//       	MainPage page = new MainPage();
       	
       	System.out.println("AAA "+((AdminSession)getSession()).getLoged());
		System.out.println("AAAA "+((AdminSession)getSession()));
       	
       	
//       	((WicketSession) getSession()).bind();
		
       	 setResponsePage(new UserListPage());
//       	setResponsePage(new DataTablePage());
        }
    };
	
    return userListLink;
    
	}
	
}
