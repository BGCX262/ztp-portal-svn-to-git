package sk.stuba.fiit.ztpPortal.module.job;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import sk.stuba.fiit.ztpPortal.databaseController.JobContractTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseController.JobSectorController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class JobDetail extends CorePage {

	private static final long serialVersionUID = 1L;

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;

	public JobDetail() {
		
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

		add(new JobDetailForm("jobDetailForm"));
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}

	private void setPageLeftNavigation() {
		Link jobListLink = new Link("jobListLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new JobList());
			}
		};
		add(jobListLink);
	}

	public final class JobDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		private DateTextField dateTextField;

		private TextField specification;
		private TextField hourPayment;
		private TextField workDuration;
		private TextField town;
		
		private DropDownChoice sector;
		private DropDownChoice contract;
		private DropDownChoice advertType;

		private TextArea textArea;

		private TinyMceBehavior tinyMCE;
		
		RegionSelector region = RegionSelector.getSingletonObject();

		private final List<String> SECTOR_LIST = Arrays
				.asList(new JobSectorController().getJobSectorNameList());

		private final List<String> CONTRACT_LIST = Arrays
				.asList(new JobContractTypeController()
						.getJobContractNameList());

		private final List<String> ADVERTTYPE_LIST = Arrays
				.asList(new AdvertTypeController().getAdvertTypeNameList());
		
		private Button submit;
		private ValueMap properties = new ValueMap();

		IValidator numberValidate = NumberValidator.minimum(0);
	
		public JobDetailForm(final String id) {
			super(id);

			advertType = new DropDownChoice("advertType", new PropertyModel(
					properties, "name"), ADVERTTYPE_LIST);
			advertType.setRequired(true);
			advertType.setLabel(new Model("Typ inzerátu"));
			add(advertType);
			add(new Label("advertTypeLabel", "Typ inzerátu *"));
			add(new FormComponentFeedbackBorder("advertTypeAsterix").add(advertType));

			specification = new TextField("specification", new PropertyModel(
					properties, "specification"));
			specification.setRequired(true);
			specification.setLabel(new Model("Popis"));
			specification.add(StringValidator.maximumLength(30));
			add(specification);
			add(new Label("specificationLabel", "Popis *"));
			add(new FormComponentFeedbackBorder("specificationAsterix").add(specification));

			dateTextField = new DateTextField("startDate", new PropertyModel(
					properties, "propertyName"), "dd.MM.yyyy");
			dateTextField.add(new DatePicker());
			dateTextField.setLabel(new Model("Zaèiatok"));
			add(dateTextField);
			add(new Label("startDateLabel", "Zaèiatok"));

			hourPayment = new TextField("hourPayment", new PropertyModel(
					properties, "hourPayment"), Float.class);
			hourPayment.setLabel(new Model("Mzda (EUR/hod)"));
			hourPayment.add(numberValidate);
			add(hourPayment);
			add(new Label("hourPaymentLabel", "Mzda (EUR/hod)"));
			add(new FormComponentFeedbackBorder("hourPaymentAsterix").add(hourPayment));
			
			workDuration = new TextField("workDuration", new PropertyModel(
					properties, "workDuration"), Float.class);
			workDuration.setLabel(new Model("Trvanie práce (dni)"));
			workDuration.add(numberValidate);
			add(workDuration);
			add(new Label("workDurationLabel", "Trvanie práce (dni)"));
			add(new FormComponentFeedbackBorder("workDurationAsterix").add(workDuration));
			
			/////
			
			add(region.getCountryDropDownChoice());
			add(region.getCountyDropDownChoice());
			
			add(new Label("countyLabel", "Kraj *"));			
			add(new Label("countryLabel", "Okres"));
			
			////
			
			town = new TextField("town", new PropertyModel(properties,
					"town"));
			town.setRequired(true);
			town.setLabel(new Model("Mesto"));
			add(town);
			add(new Label("townLabel", "Mesto *"));
			add(new FormComponentFeedbackBorder("townAsterix").add(town));
			
			sector = new DropDownChoice("sector", new PropertyModel(properties,
					"sector"), SECTOR_LIST);
			sector.setRequired(true);
			sector.setLabel(new Model("Sektor"));
			add(sector);
			add(new Label("sectorLabel", "Sektor *"));
			add(new FormComponentFeedbackBorder("sectorAsterix").add(sector));

			contract = new DropDownChoice("contract", new PropertyModel(
					properties, "contract"), CONTRACT_LIST);
			contract.setRequired(true);
			contract.setLabel(new Model("Typ úväzku"));
			add(contract);
			add(new Label("contractLabel", "Typ úväzku *"));
			add(new FormComponentFeedbackBorder("contractAsterix").add(contract));

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
				dateFormat.parse(datum);
				dat = dateFormat.parse(datum);

				java.sql.Date date = new java.sql.Date(dat.getTime());
				
				
				boolean validator = true;
				
				if (date.getTime()<new Date().getTime()) {
					feedbackPanel.warn("Dátum môže by najskôr nasledujúci deò.");
					validator = false;
				}
				
				if (validator){
				
				Job job = new Job();
				job.setSpecification(specification.getModelObjectAsString());

				job.setStartDate(date);
				job.setTown(town.getModelObjectAsString());
				job.setWorkDuration(Integer.parseInt(workDuration
						.getModelObjectAsString()));
				job.setHourPayment(Double.parseDouble(hourPayment
						.getModelObjectAsString()));

				job.setAdvertType(new AdvertTypeController()
						.getAdvertTypeByName(advertType
								.getModelObjectAsString()));
				job.setCounty(new CountyController().getCountyByName(region.getMyCounty()));
				job
						.setContract(new JobContractTypeController()
								.getJobContractByName(contract
										.getModelObjectAsString()));
				job.setJobSector(new JobSectorController()
						.getJobSectorByName(sector.getModelObjectAsString()));

				job.setCmsContent(textArea.getValue());

				// not-user entered data
				job.setState(true);
				job.setActive(true);
				job.setCreator(user);

				JobController jobC = new JobController();

				if (jobC.saveNewJob(job)){
					submit.setVisible(false);
					feedbackPanel.info("Inzerát úspešne vložený");
				}
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
