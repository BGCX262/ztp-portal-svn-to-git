package sk.stuba.fiit.ztpPortal.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.SearchResultList;
import sk.stuba.fiit.ztpPortal.server.SearchCommentStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchContext;
import sk.stuba.fiit.ztpPortal.server.SearchPortalContentStrategy;

public class SearchPage extends CorePage {

	private static final long serialVersionUID = 1L;

	private PageRenderer pageRenderer;
	private static CoreFeedBackPanel feedbackPanel;
	
	private RegisteredUserController userController;
	private CmsContentController cmsController;
	private CmsContent cmsContent;
	
	private String login;
	private RegisteredUser user;
	
	private ListView searchResultListView;
	private List resultList; 
	

	public SearchPage(List resultList) {
		if (resultList!=null) 
			this.resultList=resultList;
		else
			this.resultList=null;
		// nastavim CSS
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

		pageRenderer = new PageRenderer();
		setPageHeaderPanel();
		setPageNavigationPanel();
		
		add(pageRenderer.getSearchForm());
		
//		cmsContent = cmsController.getContentByName("main");
//		add(new Label("cmsContent",cmsContent.getContent()).setEscapeModelStrings(false));
//		
		add(new SearchForm("searchFormInput"));
		
		if (resultList!=null) searchResultListView = getSearchListResultListView(resultList);
		add(searchResultListView);

		
	}

	private void setPageHeaderPanel() {
		pageRenderer.setUser(user);
		Form loginForm = pageRenderer.getLoginForm();
		add(loginForm);
		Form statusForm = pageRenderer.getStatusForm();
		add(statusForm);
		
		if (user==null) pageRenderer.disableStatusForm();
		else pageRenderer.disableLoginForm();
		
	}

	private void setPageNavigationPanel() {
		add(pageRenderer.getEventLink());
		add(pageRenderer.getForumLink());
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());
	}
	
	
	private ListView getSearchListResultListView(List<SearchResultList> resultList){
	ListView listView = new ListView("searchResultList",resultList) {
	
	@Override
	protected void populateItem(ListItem item) {
		SearchResultList searchResultList = (SearchResultList) item.getModelObject();
		item.add(new Label("module", searchResultList.getModule()));
		item.add(new Label("text", searchResultList.getText()));
//		item.add(new Label("town", job.getTown().getName()));
		item.add(new Link("detail", item.getModel()) {
			
			@Override
			public void onClick() {
				SearchResultList selected = (SearchResultList) getModelObject();
				setResponsePage(selected.getResponsePage());
			}
		});

	}
};

return listView;


}
	
	
	public final class SearchForm extends Form {
	
		private TextField searchTextField;
		private Button submitButton;
		
		private CheckBox portalContentCheckBox;
		private Label portalContentLabel;
		
		private CheckBox commentCheckBox;
		private Label commentLabel;
		
		private ValueMap properties = new ValueMap();
		
		public SearchForm(final String id){
			super(id);
			
			searchTextField = new TextField("searchTextField", new PropertyModel(properties,
			"searchTextField"));
			searchTextField.setRequired(true);
			add(searchTextField);
		
			portalContentLabel = new Label ("portalContentLabel","Obsah portálu");
			add(portalContentLabel);
			
			portalContentCheckBox = new CheckBox("portalContentCheckBox", new PropertyModel(properties,
			"portalContentCheckBox"));
			add(portalContentCheckBox);
			
			commentLabel = new Label ("commentLabel","Komentáre");
			add(commentLabel);
			
			commentCheckBox = new CheckBox("commentCheckBox", new PropertyModel(properties,
			"commentCheckBox"));
			add(commentCheckBox);
		
			add(submitButton = new Button("submit",
					new ResourceModel("button.submit")));
			
			
		} 
		
		protected void onSubmit() {
			//SearchCommentStrategy commentStrategy = new SearchCommentStrategy();
			
			String searchString = searchTextField.getModelObjectAsString();
			System.out.println("WW"+searchString+"WW");
			if (searchString=="") searchString="//t"; 
			
			List<SearchResultList> resultList = new ArrayList<SearchResultList>(); 
			//= commentStrategy.searchData(searchText);
			
			SearchContext searchContext;
			
			if (commentCheckBox.isEnabled()){
				searchContext = new SearchContext(new SearchCommentStrategy());
				resultList.addAll(searchContext.execute(searchString));
			}
			if (portalContentCheckBox.isEnabled()){
				searchContext = new SearchContext(new SearchPortalContentStrategy());
				resultList.addAll(searchContext.execute(searchString));
			} 
			
			setResponsePage(new SearchPage(resultList));
			
		}
	
	
	}
}
