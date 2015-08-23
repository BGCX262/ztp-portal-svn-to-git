package sk.stuba.fiit.ztpPortal.module.job;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.PageRenderer;
import sk.stuba.fiit.ztpPortal.databaseController.AdvertTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.JobContractTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseController.JobSectorController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseModel.AdvertType;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.JobContractType;
import sk.stuba.fiit.ztpPortal.databaseModel.JobSector;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.County;
import wicket.contrib.tinymce.TinyMceBehavior;

public class JobViewDetail extends CorePage {

	private static final long serialVersionUID = 1L;

	String path;

	private PageRenderer pageRenderer;
	private static CoreFeedBackPanel feedbackPanel;
	
	private RegisteredUserController userController;
	private JobController jobController;
	private String login;
	private RegisteredUser user;
	private Job job;

	public JobViewDetail(long id) {
		
		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		jobController = new JobController();
		
		job = jobController.getJobByJobId(id);

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
		
		add(new JobDetailForm("jobDetailForm"));
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
		add(pageRenderer.getHomePageLink());
	}

	private void setPageLeftNavigation(){
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
		
		Link jobDetailLink = new Link("newJobLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new JobDetail());
			}
		};
		add(jobDetailLink);
		
	}
	
	public final class JobDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		private DateTextField dateTextField;
		
		private TextField specification;
		private TextField hourPayment;
		private TextField workDuration;
		
		private DropDownChoice town;
		private DropDownChoice sector;
		private DropDownChoice contract;
		private DropDownChoice advertType;
		
		private TextArea textArea;
		
		private CheckBox active;
		
		private TinyMceBehavior tinyMCE;
		
		private final List<String> TOWN_LIST = Arrays.asList(new CountyController().getCountyNameList());
		private final List<String> SECTOR_LIST = Arrays.asList(new JobSectorController().getJobSectorNameList());
		private final List<String> CONTRACT_LIST = Arrays.asList(new JobContractTypeController().getJobContractNameList());
		private final List<String> ADVERTTYPE_LIST = Arrays.asList(new AdvertTypeController().getAdvertTypeNameList());
		
		private Button submit;
		private ValueMap properties = new ValueMap();
		private String[] value = new String[1];
		private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		private Date startDate;
		
		public Date getStartDate(){
			return job.getStartDate();
		}
		
		public void setStartDate(Date startDate){
			this.startDate = startDate;
		}
		
		public JobDetailForm(final String id) {
			super(id);

			AdvertType typ = new AdvertType();
			advertType=new DropDownChoice("advertType", 
		            new PropertyModel(typ, "name"),ADVERTTYPE_LIST);
			typ.setName(job.getAdvertType().getName());
			advertType.setRequired(true);	
			advertType.setLabel(new Model("Typ inzerátu"));
			add(advertType);
			add(new Label("advertTypeLabel","Typ inzerátu"));	
			
			specification = new TextField("specification", new PropertyModel(
					properties, "specification"));
			specification.setRequired(true);
			specification.setLabel(new Model("Popis"));
			value[0] = job.getSpecification();
			specification.setModelValue(value);
			add(specification);
			add(new Label("specificationLabel", "Popis"));

			
			value[0]=dateFormat.format(job.getStartDate());
//			Date datum = job.getStartDate();//new Date();
			//datum.setTime(job.getStartDate().getTime());
			dateTextField= new DateTextField("startDate", new PropertyModel(this, "startDate"),"dd.MM.yyyy"); 
		    dateTextField.add(new DatePicker()); 
		   
		    //dateTextField.setModelValue(value);
			add(dateTextField);
			add(new Label("startDateLabel", "Zaèiatok"));
			
			hourPayment = new TextField("hourPayment", new PropertyModel(
					properties, "hourPayment"));
			hourPayment.setRequired(true);
			hourPayment.setLabel(new Model("Mzda"));
			value[0]=job.getHourPayment().toString();
			hourPayment.setModelValue(value);
			add(hourPayment);
			add(new Label("hourPaymentLabel", "Mzda"));

			workDuration = new TextField("workDuration", new PropertyModel(
					properties, "workDuration"), Integer.class);
			workDuration.setRequired(true);
			workDuration.setLabel(new Model("Trvanie práce"));
			value[0]=job.getWorkDuration().toString();
			workDuration.setModelValue(value);
			add(workDuration);
			add(new Label("workDurationLabel", "Trvanie práce"));			
			
			County okres = new County();
			town=new DropDownChoice("town", 
		            new PropertyModel(okres, "name"),TOWN_LIST);
			okres.setName(job.getCounty().getName());
			town.setRequired(true);	
			add(town);
			add(new Label("townLabel","Town"));	
			
			JobSector sektor = new JobSector();
			sector= new DropDownChoice("sector", new PropertyModel(sektor,
			"name"),SECTOR_LIST);
			sektor.setName(job.getJobSector().getName());
			sector.setRequired(true);
			add(sector);
			add(new Label("sectorLabel","Sektor"));
			
			JobContractType kontrakt = new JobContractType();
			contract= new DropDownChoice("contract", new PropertyModel(kontrakt,
			"name"),CONTRACT_LIST);
			kontrakt.setName(job.getContract().getName());
			contract.setRequired(false);
			add(contract);
			add(new Label("contractLabel","Typ úväzku"));	
			
			value[0]=job.getCmsContent();
			textArea = new TextArea("richTextInput",new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.setModelValue(value);
			textArea.setEscapeModelStrings(false);
			add(textArea);
			
			value[0]=String.valueOf(job.isState());
			active = new CheckBox("active", new PropertyModel(properties,"active"));
			active.setModelValue(value);
			add(active);
			add(new Label("activeLabel","Aktívny"));
			
			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}

		protected void onSubmit() {

			try {				
				String datum = dateTextField.getModelObjectAsString();

				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				Date dat = null;
				if (startDate != null)
					dat = startDate;
				else
					dateFormat.parse(datum);

				dat = dateFormat.parse(datum);
				
				boolean validator = true;
				
				if (dat.getTime()<new Date().getTime()) {
					feedbackPanel.warn("Dátum môže by najskôr nasledujúci deò.");
					validator = false;
				}
				
				if (validator){
				
				job.setSpecification(specification.getModelObjectAsString());
				
				job.setHourPayment(Double.parseDouble(hourPayment
						.getModelObjectAsString()));

				job.setAdvertType(new AdvertTypeController()
						.getAdvertTypeByName(advertType
								.getModelObjectAsString()));
				job.setCounty(new CountyController().getCountyByName(town
						.getModelObjectAsString()));
				job.setContract(new JobContractTypeController()
								.getJobContractByName(contract
										.getModelObjectAsString()));
				job.setJobSector(new JobSectorController()
						.getJobSectorByName(sector.getModelObjectAsString()));

				job.setCmsContent(textArea.getValue());

				// not-user entered data
				job.setState(Boolean.valueOf(active.getModelObjectAsString()));
				//job.setCreator(user);
				//job.setActive(true);

				JobController jobC = new JobController();

				if (jobC.updateJob(job))
					feedbackPanel.info("Inzerát úspešne aktualizovaný");
				else
					feedbackPanel
							.warn("Vyskytla sa chyba pri aktualizácii inzerátu");
				}
			} catch (NumberFormatException e) {
				feedbackPanel
						.warn("Vyskytla sa chyba pri aktualizácii inzerátu");
				feedbackPanel
						.warn("Údaj Mzda musí by èíslo");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
