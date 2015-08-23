package sk.stuba.fiit.ztpPortal.module.dayCare;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.AdvertType;
import sk.stuba.fiit.ztpPortal.databaseModel.County;
import sk.stuba.fiit.ztpPortal.databaseModel.DayCare;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.Style;
import wicket.contrib.tinymce.TinyMceBehavior;

public class DayCareViewDetail extends CorePage {

	private static final long serialVersionUID = 1L;

	String path;

	private PageRenderer pageRenderer;
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private DayCareController dayCareController;
	private String login;
	private RegisteredUser user;
	private DayCare dayCare;

	public DayCareViewDetail(long id) {

		if (getSession().getClass()==CoreSession.class){
			login = ((CoreSession) getSession()).getLoged();
			add(HeaderContributor.forCss(((CoreSession) getSession())
					.getUserStyle()));
		}	//toto robim kvoli prekliku admina
		else add(HeaderContributor.forCss(Style.NORMAL));


		userController = new RegisteredUserController();
		dayCareController = new DayCareController();

		dayCare = dayCareController.getDayCareByDayCareId(id);

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
		add(pageRenderer.getDayCareListLink());
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());
	}

	private void setPageLeftNavigation(){
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

		Link dayCareDetailLink = new Link("newDayCareLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new DayCareDetail());
			}
		};
		add(dayCareDetailLink);

	}

	public final class DayCareDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		private DateTextField dateTextField;
		private DateTextField dateTextField2;
		private DropDownChoice advertType;
		private DropDownChoice handicapType;
		private TextField town;	
		private DropDownChoice county;
		private Date startDate;
		private Date endDate;
		private TextField shortDesc;

		private TextArea textArea;

		private TinyMceBehavior tinyMCE;

		private final List<String> COUNTY_LIST = Arrays.asList(new CountyController().getCountyNameList());
		private final List<String> TOWN_LIST = Arrays.asList(new CountyController().getCountyNameList());
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
		
		public Date getStartDate(){
			return dayCare.getStartDate();
		}
		
		public void setStartDate(Date startDate){
			this.startDate = startDate;
		}
		public Date getEndDate(){
			return dayCare.getEndDate();
		}
		
		public void setEndDate(Date endDate){
			this.endDate = endDate;
		}

		public DayCareDetailForm(final String id) {
			super(id);
			String[] tempStr=new String[1];
			String[] tempStr2=new String[1];

			AdvertType at = new AdvertType();
			advertType = new DropDownChoice("advertType",  new PropertyModel(at, "name"), ADVERTTYPE_LIST);
			at.setName(dayCare.getAdvertType().getName());
			advertType.setRequired(true);
			advertType.setLabel(new Model("Typ inzerátu"));
			add(advertType);
			add(new Label("advertTypeLabel", "Typ inzerátu"));
			add(new FormComponentFeedbackBorder("advertTypeAsterix").add(advertType));

			shortDesc = new TextField("shortDesc", new PropertyModel(
					properties, "shortDesc"));
			shortDesc.setRequired(true);
			shortDesc.setLabel(new Model("Názov"));
			tempStr[0]=dayCare.getShortDesc();
			shortDesc.setModelValue(tempStr);
			shortDesc.add(StringValidator.maximumLength(30));
			add(shortDesc);
			add(new Label("shortDescLabel", "Názov"));
			add(new FormComponentFeedbackBorder("shortDescAsterix").add(shortDesc));

			tempStr[0]=dateFormat.format(dayCare.getStartDate());
			dateTextField= new DateTextField("startDate", new PropertyModel(this, "startDate"),"dd.MM.yyyy"); 
		    dateTextField.add(new DatePicker()); 
		    dateTextField.setModelValue(tempStr);
			add(dateTextField);
			add(new Label("startDateLabel", "Zaèiatok"));
			
			tempStr2[0]=dateFormat.format(dayCare.getEndDate());
			dateTextField2= new DateTextField("endDate", new PropertyModel(this, "endDate"),"dd.MM.yyyy"); 
		    dateTextField2.add(new DatePicker()); 
		    dateTextField2.setModelValue(tempStr2);
			add(dateTextField2);
			add(new Label("endDateLabel", "Trvanie - do"));
			
			County okres = new County();
			county=new DropDownChoice("county", 
		            new PropertyModel(okres, "name"),TOWN_LIST);
			okres.setName(dayCare.getCounty().getName());
			county.setRequired(true);	
			add(county);
			add(new Label("countyLabel", "Okres"));
		

			town = new TextField("town", new PropertyModel(properties,
			"town"));
			town.setRequired(true);
			town.setLabel(new Model("Mesto"));
			tempStr[0]=dayCare.getTown();
			town.setModelValue(tempStr);
			add(town);
			add(new Label("townLabel", "Mesto"));
			add(new FormComponentFeedbackBorder("townAsterix").add(town));

			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.setEscapeModelStrings(false);
			tempStr[0]=dayCare.getDescription();
			textArea.setModelValue(tempStr);
			add(textArea);

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}
		protected void onSubmit() {
			try {				
				String datum = dateTextField.getInput();
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
				datum=dateTextField2.getInput();
				
				dat=null;
				dat=dateFormat.parse(datum);
				java.sql.Date date2 = new java.sql.Date(dat.getTime());
				if (date2.getTime()<new Date().getTime()) {
					feedbackPanel.warn("Dátum môže by najskôr nasledujúci deò.");
					validator = false;
				}
				

				if (validator){

					
					dayCare.setAdvertType(new AdvertTypeController().getAdvertTypeByName(advertType.getModelObjectAsString()));
					dayCare.setChangeDate(new java.sql.Date(new Date().getTime()));
					dayCare.setShortDesc(shortDesc.getModelObjectAsString());
					dayCare.setStartDate(date);
					dayCare.setEndDate(date2);
					
					dayCare.setCounty(new CountyController().getCountyByName(county.getModelObjectAsString()));
					dayCare.setTown(town.getModelObjectAsString());
					
					dayCare.setDescription(textArea.getValue());

					
					// not-user entered data
					DayCareController dcc = new DayCareController();

					if (dcc.updateDayCare(dayCare)) feedbackPanel.info("Inzerát úspešne aktualizovaný");
					else feedbackPanel.warn("Chyba pri aktualizácii.");
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

