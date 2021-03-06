package sk.stuba.fiit.ztpPortal.module.job;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import sk.stuba.fiit.ztpPortal.core.PageRenderer;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseController.JobSectorController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.TownController;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

public class JobDetail extends CorePage {

	private static final long serialVersionUID = 1L;

	String path;

	private PageRenderer pageRenderer;
	private static FeedbackPanel feedbackPanel;
	
	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;

	public JobDetail() {
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;

		 feedbackPanel = new FeedbackPanel("feedback",new ContainerFeedbackMessageFilter(this));
	        add(feedbackPanel);
		
		pageRenderer = new PageRenderer();
		setPageHeaderPanel();
		setPageNavigationPanel();
		setPageLeftNavigation();

		add(new JobDetailForm("jobDetailForm"));
	}

	private void setPageHeaderPanel() {
		pageRenderer.setUser(user);
		add(pageRenderer.getLoginForm());
		add(pageRenderer.getStatusForm());
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
	}
	

	public final class JobDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		TextField startDate;
		TextField specification;
		TextField hourPayment;
		TextField workDuration;
		
		DropDownChoice town;
		DropDownChoice sector;
		
		private final List TOWN_LIST = Arrays.asList(new TownController().getTownNameList());
		
		private final List SECTOR_LIST = Arrays.asList(new JobSectorController().getJobSectorNameList());
		
		Button submit;
		private ValueMap properties = new ValueMap();

		public JobDetailForm(final String id) {
			super(id);

			specification = new TextField("specification", new PropertyModel(
					properties, "specification"));
			specification.setRequired(true);
			add(specification);
			add(new Label("specificationLabel", "Popis"));

			startDate = new TextField("startDate", new PropertyModel(
					properties, "startDate"), Date.class);
			startDate.setRequired(true);
			add(startDate);
			add(new Label("startDateLabel", "Za�iatok"));

			hourPayment = new TextField("hourPayment", new PropertyModel(
					properties, "hourPayment"), Float.class);
			hourPayment.setRequired(true);
			add(hourPayment);
			add(new Label("hourPaymentLabel", "Mzda"));

			workDuration = new TextField("workDuration", new PropertyModel(
					properties, "workDuration"), Integer.class);
			workDuration.setRequired(true);
			add(workDuration);
			add(new Label("workDurationLabel", "Trvanie pr�ce"));
			
			town= new DropDownChoice("town", new PropertyModel(properties,
			"town"),TOWN_LIST);
			town.setRequired(false);
			add(town);
			add(new Label("townLabel","Okres"));	
			
			sector= new DropDownChoice("sector", new PropertyModel(properties,
			"sector"),SECTOR_LIST);
			sector.setRequired(false);
			add(sector);
			add(new Label("sectorLabel","Sektor"));	

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}

		protected void onSubmit() {
			System.out.println("OKI");

			Job job = new Job();
			job.setSpecification(specification.getModelObjectAsString());

			Date startDateJob = new Date();
			//job.setStartDate(startDateJob);
			job.setWorkDuration(Integer.parseInt(workDuration
					.getModelObjectAsString()));
			job.setHourPayment(Integer.parseInt(hourPayment
					.getModelObjectAsString()));
			
			job.setTown(new TownController().getTownByName(town.getModelObjectAsString()));
			
			// not-user entered data
			job.setState(true);
			job.setCreator(user);

			JobController jobC = new JobController();
			jobC.saveNewJob(job);

		}

	}
}
