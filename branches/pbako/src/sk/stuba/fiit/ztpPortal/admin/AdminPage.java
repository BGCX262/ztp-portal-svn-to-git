package sk.stuba.fiit.ztpPortal.admin;

import org.apache.wicket.markup.html.WebPage;

/**
 * 
 * @author 
 */
public abstract class AdminPage extends WebPage {
	public AdminSession getWicketSession() {
		return (AdminSession) getSession();
	}
	
}