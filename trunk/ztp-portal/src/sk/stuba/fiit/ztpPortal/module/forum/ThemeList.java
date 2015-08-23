package sk.stuba.fiit.ztpPortal.module.forum;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.ThemeController;
import sk.stuba.fiit.ztpPortal.databaseController.ThreadController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.Theme;

public class ThemeList extends CorePage {
	
	private static final long serialVersionUID = 1L;

	private static CoreFeedBackPanel feedbackPanel;
	
	private RegisteredUserController userController;
	private CmsContentController cmsController;
	private CmsContent cmsContent;
	private ThemeController themeController;
	private ListView listView;
	
	private String login;
	private RegisteredUser user;
	
	public ThemeList(){
		
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		cmsController = new CmsContentController();

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;
		
		 feedbackPanel = new CoreFeedBackPanel("feedback");
		 	add(feedbackPanel);

		setPageHeaderPanel();
		
		cmsContent = cmsController.getContentByName("forum");
		add(new Label("cmsContent",cmsContent.getContent()).setEscapeModelStrings(false));
		
		add(themeListRepeater());

	}
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}
	
	protected ListView themeListRepeater(){
		
		themeController = new ThemeController();

		listView = new ListView("themeListView", themeController
				.getAllActiveTheme()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final Theme theme = (Theme) item.getModelObject();
				item.add(new Label("themeName", theme.getName()));
				
				item.add(new Label("threadCount", String.valueOf(themeController.getThemeThreadCount(theme))));
				
				Link threadLink = new Link("threadLink", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new ThreadList(theme));
					}
				};
				
				item.add(threadLink);

			}
		};
		return listView;
		
	}

}
