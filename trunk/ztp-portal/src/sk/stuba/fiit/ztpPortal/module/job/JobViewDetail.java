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
import org.apache.wicket.validation.validator.StringValidator;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.core.RegionSelector;
import sk.stuba.fiit.ztpPortal.core.RegionSelectorView;
import sk.stuba.fiit.ztpPortal.databaseController.AdvertTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.JobContractTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseController.JobSectorController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.AdvertType;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.JobContractType;
import sk.stuba.fiit.ztpPortal.databaseModel.JobSector;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class JobViewDetail extends CorePage {

	private static final long serialVersionUID = 1L;
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
		
		setPageHeaderPanel();
		setPageLeftNavigation();

		
		add(new JobDetailForm("jobDetailForm"));
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
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
		private TextField town;
		
		private DropDownChoice sector;
		private DropDownChoice contract;
		private DropDownChoice advertType;
		
		private TextArea textArea;
		
		private CheckBox active;
		
		private TinyMceBehavior tinyMCE;
		
		private RegionSelectorView regionSelector;
		private RegionSelector region;
		
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
			advertType.setLabel(new Model("Typ inzer�tu"));
			add(advertType);
			add(new Label("advertTypeLabel","Typ inzer�tu *"));	
			
			specification = new TextField("specification", new PropertyModel(
					properties, "specification"));
			specification.setRequired(true);
			specification.setLabel(new Model("Popis"));
			value[0] = job.getSpecification();
			specification.setModelValue(value);
			add(specification);
			add(new Label("specificationLabel", "Popis *"));

			
			value[0]=dateFormat.format(job.getStartDate());
//			Date datum = job.getStartDate();//new Date();
			//datum.setTime(job.getStartDate().getTime());
			dateTextField= new DateTextField("startDate", new PropertyModel(this, "startDate"),"dd.MM.yyyy"); 
		    dateTextField.add(new DatePicker()); 
		   
		    //dateTextField.setModelValue(value);
			add(dateTextField);
			add(new Label("startDateLabel", "Za�iatok"));
			
			hourPayment = new TextField("hourPayment", new PropertyModel(
					properties, "hourPayment"));
			hourPayment.setLabel(new Model("Mzda (EUR/hod)"));
			value[0]=job.getHourPayment().toString();
			hourPayment.setModelValue(value);
			add(hourPayment);
			add(new Label("hourPaymentLabel", "Mzda (EUR/hod)"));

			workDuration = new TextField("workDuration", new PropertyModel(
					properties, "workDuration"), Integer.class);
			workDuration.setLabel(new Model("Trvanie pr�ce (dni)"));
			value[0]=job.getWorkDuration().toString();
			workDuration.setModelValue(value);
			add(workDuration);
			add(new Label("workDurationLabel", "Trvanie pr�ce (dni)"));			
			
			if (job.getCounty() != null) {
				regionSelector = new RegionSelectorView(job.getCounty());
				add(regionSelector.getCountryDropDownChoice());
				add(regionSelector.getCountyDropDownChoice());
			} else {
				region = RegionSelector.getSingletonObject();
				add(region.getCountryDropDownChoice());
				add(region.getCountyDropDownChoice());
			}
			add(new Label("countryLabel", "Kraj *"));
			add(new Label("countyLabel", "Okres"));
			
			town=new TextField("town", 
		            new PropertyModel(properties, "town"));
			value[0]=job.getTown();
			town.setModelValue(value);
			town.setRequired(true);	
			add(town);
			add(new Label("townLabel","Mesto *"));	
			
			JobSector sektor = new JobSector();
			sector= new DropDownChoice("sector", new PropertyModel(sektor,
			"name"),SECTOR_LIST);
			sektor.setName(job.getJobSector().getName());
			sector.setRequired(true);
			add(sector);
			add(new Label("sectorLabel","Sektor *"));
			
			JobContractType kontrakt = new JobContractType();
			contract= new DropDownChoice("contract", new PropertyModel(kontrakt,
			"name"),CONTRACT_LIST);
			kontrakt.setName(job.getContract().getName());
			contract.setRequired(false);
			add(contract);
			add(new Label("contractLabel","Typ �v�zku *"));	
			
			value[0]=job.getCmsContent();
			textArea = new TextArea("richTextInput",new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(StringValidator.maximumLength(5000));
			textArea.add(tinyMCE);
			textArea.setModelValue(value);
			textArea.setEscapeModelStrings(false);
			add(textArea);
			
			value[0]=String.valueOf(job.isActive());
			active = new CheckBox("active", new PropertyModel(properties,"active"));
			active.setModelValue(value);
			add(active);
			add(new Label("activeLabel","Akt�vny"));
			
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
					feedbackPanel.warn("D�tum m��e by� najsk�r nasleduj�ci de�.");
					validator = false;
				}
				
				if (validator){
				
				job.setSpecification(specification.getModelObjectAsString());
				
				job.setHourPayment(Double.parseDouble(hourPayment
						.getModelObjectAsString()));

				job.setAdvertType(new AdvertTypeController()
						.getAdvertTypeByName(advertType
								.getModelObjectAsString()));
				if (job.getCounty()!=null)
					job.setCounty(new CountyController().getCountyByName(regionSelector.getMyCounty()));
				else 
					job.setCounty(new CountyController().getCountyByName(region.getMyCounty()));
				job.setContract(new JobContractTypeController()
								.getJobContractByName(contract
										.getModelObjectAsString()));
				job.setJobSector(new JobSectorController()
						.getJobSectorByName(sector.getModelObjectAsString()));

				job.setCmsContent(textArea.getValue());

				// not-user entered data
				job.setActive(Boolean.valueOf(active.getModelObjectAsString()));

				JobController jobC = new JobController();

				if (jobC.updateJob(job))
					feedbackPanel.info("Inzer�t �spe�ne aktualizovan�");
				else
					feedbackPanel
							.warn("Vyskytla sa chyba pri aktualiz�cii inzer�tu");
				}
			} catch (NumberFormatException e) {
				feedbackPanel
						.warn("Vyskytla sa chyba pri aktualiz�cii inzer�tu");
				feedbackPanel
						.warn("�daj Mzda mus� by� ��slo");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
