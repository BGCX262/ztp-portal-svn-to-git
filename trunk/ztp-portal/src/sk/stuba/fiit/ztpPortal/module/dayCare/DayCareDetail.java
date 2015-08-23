package sk.stuba.fiit.ztpPortal.module.dayCare;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
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
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.core.PageNoAdmitance;
import sk.stuba.fiit.ztpPortal.core.RegionSelector;
import sk.stuba.fiit.ztpPortal.databaseController.AdvertTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.DayCareController;
import sk.stuba.fiit.ztpPortal.databaseController.HandicapTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.DayCare;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class DayCareDetail extends CorePage {

	private static final Log log = LogFactory.getLog(DayCareDetail.class);
	private static final long serialVersionUID = 1L;

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
			throw new RestartResponseAtInterceptPageException(
				      new PageNoAdmitance(this));

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();
		setPageLeftNavigation();

		add(new DayCareDetailForm("dayCareDetailForm"));
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
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
		private TextField description;
		private TextArea textArea;
		private TinyMceBehavior tinyMCE;

		private final List<String> ADVERTTYPE_LIST = Arrays
				.asList(new AdvertTypeController().getAdvertTypeNameList());
		
		private final List<String> HANDICAPTYPE_LIST = Arrays
		.asList(new HandicapTypeController().getHandicapNameList());
		
		private Button submit;
		private ValueMap properties = new ValueMap();

		IValidator numberValidate = NumberValidator.minimum(0);

		RegionSelector region = RegionSelector.getSingletonObject();
		
		public DayCareDetailForm(final String id) {
			super(id);
			advertType = new DropDownChoice("advertType", new PropertyModel(
					properties, "name"), ADVERTTYPE_LIST);
			advertType.setRequired(true);
			advertType.setLabel(new Model("Typ inzerátu"));
			add(advertType);
			add(new Label("advertTypeLabel", "Typ inzerátu *"));
			add(new FormComponentFeedbackBorder("advertTypeAsterix").add(advertType));

			handicapType = new DropDownChoice("handicapType",
					new PropertyModel(properties, "handicapType"),
					HANDICAPTYPE_LIST);
			handicapType.setRequired(true);
			handicapType.setLabel(new Model("Typ postihnutia"));
			add(handicapType);
			add(new Label("handicapTypeLabel", "Typ postihnutia *"));
			add(new FormComponentFeedbackBorder("handicapTypeAsterix").add(handicapType));
			
			description = new TextField("shortDesc", new PropertyModel(
					properties, "shortDesc"));
			description.setRequired(true);
			description.setLabel(new Model("Krátky popis"));
			description.add(StringValidator.maximumLength(30));
			add(description);
			add(new Label("shortDescLabel", "Krátky popis *"));
			add(new FormComponentFeedbackBorder("shortDescAsterix").add(description));

			dateTextField = new DateTextField("startDate", new PropertyModel(
					properties, "startDate"), "dd.MM.yyyy");
			dateTextField.add(new DatePicker());
			dateTextField.setRequired(true);
			dateTextField.setLabel(new Model("Zaèiatok"));
			add(dateTextField);
			add(new Label("startDateLabel", "Zaèiatok *"));
			add(new FormComponentFeedbackBorder("startDateAsterix").add(dateTextField));
			
			dateTextField2 = new DateTextField("endDate", new PropertyModel(
					properties, "endDate"), "dd.MM.yyyy");
			dateTextField2.add(new DatePicker());
			dateTextField2.setRequired(true);
			dateTextField2.setLabel(new Model("Trvanie - do"));
			add(dateTextField2);
			add(new Label("endDateLabel", "Trvanie - do *"));
			add(new FormComponentFeedbackBorder("endDateAsterix").add(dateTextField2));

			//
			add(region.getCountryDropDownChoice());
			add(region.getCountyDropDownChoice());
			
			add(new Label("countryLabel", "Kraj *"));
			add(new Label("countyLabel", "Okres"));
			
			
			////
			town = new TextField("town", new PropertyModel(properties,"town"));
			town.setRequired(true);
			town.setLabel(new Model("Mesto"));
			add(town);
			add(new Label("townLabel", "Mesto *"));
			add(new FormComponentFeedbackBorder("townAsterix").add(town));
			
			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.add(StringValidator.maximumLength(5000));
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
					dayCare.setHandicapType(new HandicapTypeController().getHandicapByName(handicapType.getModelObjectAsString()));
					dayCare.setShortDesc(description.getModelObjectAsString());
					dayCare.setCreationDate(new java.sql.Date(new Date().getTime()));
					dayCare.setChangeDate(new java.sql.Date(new Date().getTime()));
					dayCare.setStartDate(date);
					dayCare.setEndDate(date2);
					dayCare.setCounty(new CountyController().getCountyByName(region.getMyCounty()));
					dayCare.setTown(town.getModelObjectAsString());
					dayCare.setDescription(textArea.getValue());

					// not-user entered data
					dayCare.setState(true);
					dayCare.setActive(true);
					dayCare.setCreator(user);

					DayCareController dcc = new DayCareController();

					if (dcc.saveNewDayCare(dayCare)){
						submit.setVisible(false);
						feedbackPanel.info("Inzerát úspešne vložený");
					}
					else{
						log.warn("Chyba pri vkladaní");
						feedbackPanel.warn("Chyba pri vkladaní");
					}
				}
			} catch (ParseException e) {
				log.warn(e.getMessage());
			} catch (NumberFormatException e) {
				log.warn(e.getMessage());
				feedbackPanel
				.warn("Vyskytla sa chyba pri aktualizácii inzerátu");
				feedbackPanel.warn("Údaj Mzda musí by èíslo");
			}
		}

	}
}
