package sk.stuba.fiit.ztpPortal.admin;

import org.apache.wicket.markup.html.panel.Panel;

public class NavigationPanel extends Panel{

	public NavigationPanel(String id) {
		super(id);
		
		AdminNavigation adminNavigation = new AdminNavigation();
        
        add(adminNavigation.getUserListLink());
        
        add(adminNavigation.getContentListLink());
        
        add(adminNavigation.getJobListLink());
        
        add(adminNavigation.getGlobalSettingLink());
        
        add(adminNavigation.getForumListLink());
        
        add(adminNavigation.getEventListLink());
        
        add(adminNavigation.getEducationListLink());
        
        add(adminNavigation.getLivingListLink());
        
        add(adminNavigation.getInformationListLink());
        
        add(adminNavigation.getHealthAidListLink());
        
        add(adminNavigation.getDayCareListLink());
	}

}
