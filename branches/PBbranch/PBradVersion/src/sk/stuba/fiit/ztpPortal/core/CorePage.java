package sk.stuba.fiit.ztpPortal.core;


import org.apache.wicket.markup.html.WebPage;

/**
 * 
 * @author ligocky Stranka od ktorej dedia ostatne
 */
public class CorePage extends WebPage {
	public CoreSession getWicketSession() {
		return (CoreSession) getSession();
	}
}