package sk.stuba.fiit.ztpPortal.module.job;

import java.util.Date;

import sk.stuba.fiit.ztpPortal.core.PageRenderer;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.value.ValueMap;

public class JobView extends CorePage {

	private static final long serialVersionUID = 1L;

	String path;

	private PageRenderer pageRenderer;
	private static FeedbackPanel feedbackPanel;
	
	private RegisteredUserController userController;
	private JobController jobController;
	private String login;
	private RegisteredUser user;
	private Job job;

	public JobView(long id) {
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		jobController = new JobController();
		
		job = jobController.getJobByJobId(id);

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

		TextField startDate;
		TextField specification;
		TextField hourPayment;
		TextField workDuration;
		
		Button submit;
		private ValueMap properties = new ValueMap();

		public JobDetailForm(final String id) {
			super(id);

			specification = new TextField("specification", new PropertyModel(
					properties, "specification"));
			specification.setRequired(true);
			specification.setModelValue(job.getSpecification());
			add(specification);
			add(new Label("specificationLabel", "Popis"));

			startDate = new TextField("startDate", new PropertyModel(
					properties, "startDate"), Date.class);
			startDate.setRequired(true);
	//		startDate.setModelValue(job.getStartDate())
			add(startDate);
			add(new Label("startDateLabel", "Zaèiatok"));

			hourPayment = new TextField("hourPayment", new PropertyModel(
					properties, "hourPayment"), Float.class);
			hourPayment.setRequired(true);
			hourPayment.setModelValue(job.getHourPayment().toString());
			add(hourPayment);
			add(new Label("hourPaymentLabel", "Mzda"));

			workDuration = new TextField("workDuration", new PropertyModel(
					properties, "workDuration"), Integer.class);
			workDuration.setRequired(true);
			workDuration.setModelValue(job.getWorkDuration().toString());
			add(workDuration);
			add(new Label("workDurationLabel", "Trvanie práce"));

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
			
			// not-user entered data
			job.setState(true);
			job.setCreator(user);

			JobController jobC = new JobController();

			// JobPageDeployment jPage = new JobPageDeployment();
			System.out.println("OKI" + path);
			// jPage.setPath(path);
			// String pageName = jPage.createNewPage(job.getSpecification());

			// job.setUrlLink(pageName); // FIXME toto treba inak ukladat ako
			// relativne adresy, nie absolutne
			jobC.saveNewJob(job);

		}

	}
}
