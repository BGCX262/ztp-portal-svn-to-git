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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.SearchResultList;
import sk.stuba.fiit.ztpPortal.server.SearchCommentStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchContext;
import sk.stuba.fiit.ztpPortal.server.SearchCourseStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchDayCareStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchEventStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchHealthAidStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchJobStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchLivingStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchPortalContentStrategy;
import sk.stuba.fiit.ztpPortal.server.SearchSchoolStrategy;

public class SearchPage extends CorePage {

	private static final long serialVersionUID = 1L;

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;

	private String login;
	private RegisteredUser user;

	private ListView searchResultListView = null;
	private List<SearchResultList> resultList;
	
	private Label searchResult;

	public SearchPage(List<SearchResultList> resultList) {
		if (resultList != null)
			this.resultList = resultList;
		else
			this.resultList = new ArrayList<SearchResultList>();
		// nastavim CSS
		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();
		add(new SearchForm("searchFormInput"));

		if (this.resultList != null)
			searchResultListView = getSearchListResultListView(resultList);
		add(searchResultListView);
		
		if (resultList.size()>0) searchResult = new Label("searchResult","Nájdené záznamy");
		else searchResult = new Label("searchResult","Nenájdené záznamy");
		add(searchResult);

	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel", user);
		add(panel);
	}

	private ListView getSearchListResultListView(
			List<SearchResultList> resultList) {
		ListView listView = new ListView("searchResultList", resultList) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				SearchResultList searchResultList = (SearchResultList) item
						.getModelObject();
				item.add(new Label("module", searchResultList.getModule()));
				item.add(new Label("text", searchResultList.getText()));
				// item.add(new Label("town", job.getTown().getName()));
				item.add(new Link("detail", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						SearchResultList selected = (SearchResultList) getModelObject();
						System.out.println("TU TOTO  "
								+ selected.getResponsePage());
						setResponsePage(selected.getResponsePage());
					}
				});

			}
		};

		return listView;

	}

	public final class SearchForm extends Form {

		private static final long serialVersionUID = 1L;
		private TextField searchTextField;

		private CheckBox portalContentCheckBox;
		private Label portalContentLabel;

		private CheckBox commentCheckBox;
		private Label commentLabel;

		private CheckBox eventCheckBox;
		private Label eventLabel;

		private CheckBox jobCheckBox;
		private Label jobLabel;

		private CheckBox livingCheckBox;
		private Label livingLabel;

		private CheckBox educationCheckBox;
		private Label educationLabel;

		private CheckBox informationCheckBox;
		private Label informationLabel;

		private CheckBox healthAidCheckBox;
		private Label healthAidLabel;

		private CheckBox dayCareCheckBox;
		private Label dayCareLabel;

		private ValueMap properties = new ValueMap();

		public SearchForm(final String id) {
			super(id);

			searchTextField = new TextField("searchTextField",
					new PropertyModel(properties, "searchTextField"));
			searchTextField.setLabel(new Model("H¾adaný text"));
			searchTextField.setRequired(true);
			add(searchTextField);

			portalContentLabel = new Label("portalContentLabel",
					"Obsah portálu");
			add(portalContentLabel);

			portalContentCheckBox = new CheckBox("portalContentCheckBox",
					new PropertyModel(properties, "portalContentCheckBox"));
			add(portalContentCheckBox);

			commentLabel = new Label("commentLabel", "Komentáre");
			add(commentLabel);

			commentCheckBox = new CheckBox("commentCheckBox",
					new PropertyModel(properties, "commentCheckBox"));
			add(commentCheckBox);

			jobCheckBox = new CheckBox("jobCheckBox", new PropertyModel(
					properties, "jobCheckBox"));
			add(jobCheckBox);

			jobLabel = new Label("jobLabel", "Pracovné ponuky");
			add(jobLabel);

			livingCheckBox = new CheckBox("livingCheckBox", new PropertyModel(
					properties, "livingCheckBox"));
			add(livingCheckBox);

			livingLabel = new Label("livingLabel", "Ubytovania");
			add(livingLabel);

			eventCheckBox = new CheckBox("eventCheckBox", new PropertyModel(
					properties, "eventCheckBox"));
			add(eventCheckBox);

			eventLabel = new Label("eventLabel", "Udalosti");
			add(eventLabel);

			educationLabel = new Label("educationLabel", "Vzdelávanie");
			add(educationLabel);

			educationCheckBox = new CheckBox("educationCheckBox",
					new PropertyModel(properties, "educationCheckBox"));
			add(educationCheckBox);

			informationLabel = new Label("informationLabel", "Informácie");
			add(informationLabel);

			informationCheckBox = new CheckBox("informationCheckBox",
					new PropertyModel(properties, "informationCheckBox"));
			add(informationCheckBox);

			healthAidLabel = new Label("healthAidLabel", "Pomôcky");
			add(healthAidLabel);

			healthAidCheckBox = new CheckBox("healthAidCheckBox",
					new PropertyModel(properties, "healthAidCheckBox"));
			add(healthAidCheckBox);

			dayCareLabel = new Label("dayCareLabel", "Opatrovate¾stvo");
			add(dayCareLabel);

			dayCareCheckBox = new CheckBox("dayCareCheckBox",
					new PropertyModel(properties, "dayCareCheckBox"));
			add(dayCareCheckBox);

			add(new Button("submit", new ResourceModel("button.submit")));

		}

		protected void onSubmit() {
			String searchString = searchTextField.getModelObjectAsString();

			if (searchString == "")
				searchString = "//t";

			List<SearchResultList> resultList = new ArrayList<SearchResultList>();

			SearchContext searchContext;

			if (commentCheckBox.getValue().equals("true")) {
				searchContext = new SearchContext(new SearchCommentStrategy());
				resultList.addAll(searchContext.execute(searchString));
			}
			if (portalContentCheckBox.getValue().equals("true")) {
				searchContext = new SearchContext(
						new SearchPortalContentStrategy());
				resultList.addAll(searchContext.execute(searchString));
			}

			if (jobCheckBox.getValue().equals("true")) {
				searchContext = new SearchContext(new SearchJobStrategy());
				resultList.addAll(searchContext.execute(searchString));
			}

			if (livingCheckBox.getValue().equals("true")) {
				searchContext = new SearchContext(new SearchLivingStrategy());
				resultList.addAll(searchContext.execute(searchString));
			}

			if (eventCheckBox.getValue().equals("true")) {
				searchContext = new SearchContext(new SearchEventStrategy());
				resultList.addAll(searchContext.execute(searchString));
			}

			if (educationCheckBox.getValue().equals("true")) {
				searchContext = new SearchContext(new SearchSchoolStrategy());
				resultList.addAll(searchContext.execute(searchString));

				searchContext = new SearchContext(new SearchCourseStrategy());
				resultList.addAll(searchContext.execute(searchString));
			}
			
			if (dayCareCheckBox.getValue().equals("true")) {
				searchContext = new SearchContext(new SearchDayCareStrategy());
				resultList.addAll(searchContext.execute(searchString));
			}
			
			if (healthAidCheckBox.getValue().equals("true")) {
				searchContext = new SearchContext(new SearchHealthAidStrategy());
				resultList.addAll(searchContext.execute(searchString));
			}

			setResponsePage(new SearchPage(resultList));

		}

	}
}
