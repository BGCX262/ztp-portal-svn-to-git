package sk.stuba.fiit.ztpPortal.core;

import org.apache.wicket.markup.html.link.Link;

public class PageExpiration extends CorePage {
	
	public PageExpiration(){
		
	Link homeLink = new Link("homeLink"){
		public void onClick() {
			setResponsePage(MainPage.class);
		}
	};
	
	add(homeLink);
	}

}
