package sk.stuba.fiit.ztpPortal.module.dayCare;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.NumberValidator;
import org.apache.wicket.validation.validator.StringValidator;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.PageRenderer;
import sk.stuba.fiit.ztpPortal.databaseController.AdvertTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.DayCareController;
import sk.stuba.fiit.ztpPortal.databaseController.JobContractTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.DayCare;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class DayCareDetail extends CorePage {

	private static final long serialVersionUID = 1L;

	String path;

	private PageRenderer pageRenderer;
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;

	public DayCareDetail() {
		
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

		pageRenderer = new PageRenderer();
		setPageHeaderPanel();
		setPageNavigationPanel();
		setPageLeftNavigation();
		
		add(pageRenderer.getSearchForm());

		add(new DayCareDetailForm("dayCareDetailForm"));
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
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getDayCareListLink());
		add(pageRenderer.getHomePageLink());
	}

	private void setPageLeftNavigation() {
		Link dayCareListLink = new Link("dayCareListLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new DayCareList());
			}
		};
		add(dayCareListLink);
	}

	public final class DayCareDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		private DateTextField dateTextField;
		private DateTextField dateTextField2;
		private DropDownChoice advertType;
		private DropDownChoice handicapType;
		private TextField town;	
		private DropDownChoice county;
		private TextField startDate;
		private TextField description;
		private TextArea textArea;
		private TinyMceBehavior tinyMCE;

		private final List<String> COUNTY_LIST = Arrays
				.asList(new CountyController().getCountyNameList());

		private final List<String> ADVERTTYPE_LIST = Arrays
				.asList(new AdvertTypeController().getAdvertTypeNameList());
		
		private Button submit;
		private ValueMap properties = new ValueMap();

		IValidator numberValidate = NumberValidator.minimum(0);

		/// ajax previazane combo vybery
		
		private Map countyMap = new HashMap(); // map:company->model
		private String selectedCountry;
		public String getSelectedCountry()
		{
			return selectedCountry;
		}
		
		public void setSelectedCountry(String selectedCountry)
		{
			this.selectedCountry = selectedCountry;
		}
		
		public DayCareDetailForm(final String id) {
			super(id);
			advertType = new DropDownChoice("advertType", new PropertyModel(
					properties, "name"), ADVERTTYPE_LIST);
			advertType.setRequired(true);
			advertType.setLabel(new Model("Typ inzerátu"));
			add(advertType);
			add(new Label("advertTypeLabel", "Typ inzerátu"));
			add(new FormComponentFeedbackBorder("advertTypeAsterix").add(advertType));

			description = new TextField("shortDesc", new PropertyModel(
					properties, "shortDesc"));
			description.setRequired(true);
			description.setLabel(new Model("Krátky popis"));
			description.add(StringValidator.maximumLength(30));
			add(description);
			add(new Label("shortDescLabel", "Krátky popis"));
			add(new FormComponentFeedbackBorder("shortDescAsterix").add(description));

			dateTextField = new DateTextField("startDate", new PropertyModel(
					properties, "propertyName"), "dd.MM.yyyy");
			dateTextField.add(new DatePicker());
			dateTextField.setLabel(new Model("Zaèiatok"));
			add(dateTextField);
			add(new Label("startDateLabel", "Zaèiatok"));
			
			dateTextField2 = new DateTextField("endDate", new PropertyModel(
					properties, "propertyName"), "dd.MM.yyyy");
			dateTextField2.add(new DatePicker());
			dateTextField2.setLabel(new Model("Trvanie - do"));
			add(dateTextField2);
			add(new Label("endDateLabel", "Trvanie - do"));

			CountyController countyController = new CountyController();
			countyMap.put("Nitriansky kraj", Arrays.asList(countyController.getCountyListByCountry("Nitriansky kraj")));
			countyMap.put("Bratislavský kraj", Arrays.asList(countyController.getCountyListByCountry("Bratislavský kraj")));
			countyMap.put("Trnavský kraj", Arrays.asList(countyController.getCountyListByCountry("Trnavský kraj")));
			countyMap.put("Trenèiansky kraj", Arrays.asList(countyController.getCountyListByCountry("Trenèiansky kraj")));
			countyMap.put("Banskobystrický kraj", Arrays.asList(countyController.getCountyListByCountry("Banskobystrický kraj")));
			countyMap.put("Žilinský kraj", Arrays.asList(countyController.getCountyListByCountry("Žilinský kraj")));
			countyMap.put("Prešovský kraj", Arrays.asList(countyController.getCountyListByCountry("Prešovský kraj")));
			countyMap.put("Košický kraj", Arrays.asList(countyController.getCountyListByCountry("Košický kraj")));
			
			IModel countryChoices = new AbstractReadOnlyModel()
			{
				public Object getObject()
				{
					Set keys = countyMap.keySet();
					List list = new ArrayList(keys);
					return list;
				}

			};
			
			IModel countyChoices = new AbstractReadOnlyModel()
			{
				public Object getObject()
				{
					List models = (List)countyMap.get(selectedCountry);
					if (models == null)
					{
						models = Collections.EMPTY_LIST;
					}
					return models;
				}
			};
			
			final DropDownChoice country = new DropDownChoice("country", new PropertyModel(this,"selectedCountry"), countryChoices);
			county = new DropDownChoice("county", new Model(), countyChoices);
			country.setOutputMarkupId(true);
			county.setOutputMarkupId(true);
			add(country);
			add(county);
		
			country.add(new AjaxFormComponentUpdatingBehavior("onchange")
			{
				protected void onUpdate(AjaxRequestTarget target)
				{
					target.addComponent(county);
				}
			});
			
			add(new Label("countyLabel", "Kraj"));
			add(new Label("countryLabel", "Okres"));
			
			
			////
			town = new TextField("town", new PropertyModel(properties,"town"));
			town.setRequired(true);
			town.setLabel(new Model("Mesto"));
			add(town);
			add(new Label("townLabel", "Mesto"));
			add(new FormComponentFeedbackBorder("townAsterix").add(town));
			
			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.setEscapeModelStrings(false);
			add(textArea);

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}

		protected void onSubmit() {
			try {
				String datum = dateTextField.getModelObjectAsString();
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				Date dat = null;
				dateFormat.parse(datum);
				dat = dateFormat.parse(datum);
				java.sql.Date date = new java.sql.Date(dat.getTime());
				boolean validator = true;
				if (date.getTime()<new Date().getTime()) {
					feedbackPanel.warn("Dátum môže by najskôr nasledujúci deò.");
					validator = false;
				}
				datum=dateTextField2.getModelObjectAsString();
				dat=null;
				dat=dateFormat.parse(datum);
				java.sql.Date date2 = new java.sql.Date(dat.getTime());
				if (date2.getTime()<new Date().getTime()) {
					feedbackPanel.warn("Dátum môže by najskôr nasledujúci deò.");
					validator = false;
				}
				if (validator){
					DayCare dayCare = new DayCare();
					dayCare.setAdvertType(new AdvertTypeController().getAdvertTypeByName(advertType.getModelObjectAsString()));
					dayCare.setShortDesc(description.getModelObjectAsString());
					dayCare.setCreationDate(new java.sql.Date(new Date().getTime()));
					dayCare.setChangeDate(new java.sql.Date(new Date().getTime()));
					dayCare.setStartDate(date);
					dayCare.setEndDate(date2);
					dayCare.setCounty(new CountyController().getCountyByName(county.getModelObjectAsString()));
					dayCare.setTown(town.getModelObjectAsString());
					dayCare.setDescription(textArea.getModelObjectAsString());


					//dayCare.setCmsContent(textArea.getValue());

					// not-user entered data
					dayCare.setState(true);
					dayCare.setActive(true);
					dayCare.setCreator(user);

					DayCareController dcc = new DayCareController();

					if (dcc.saveNewDayCare(dayCare))
						feedbackPanel.info("Inzerát úspešne vložený");
					else
						feedbackPanel.warn("Chyba pri vkladaní");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				feedbackPanel
				.warn("Vyskytla sa chyba pri aktualizácii inzerátu");
				feedbackPanel.warn("Údaj Mzda musí by èíslo");
			}
		}

	}
}
