package sk.stuba.fiit.ztpPortal.module.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.PageRenderer;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.Style;

import org.apache.wicket.behavior.HeaderContributor;
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
	private static CoreFeedBackPanel feedbackPanel;
	
	private RegisteredUserController userController;
	private JobController jobController;
	private String login;
	private RegisteredUser user;
	private Job job;

	public JobView(long id) {

		if (getSession().getClass()==CoreSession.class){
			login = ((CoreSession) getSession()).getLoged();
			add(HeaderContributor.forCss(((CoreSession) getSession())
					.getUserStyle()));
		}	//toto robim kvoli prekliku admina
		else add(HeaderContributor.forCss(Style.NORMAL));
		
		
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
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		public JobDetailForm(final String id) {
			super(id);

			add(new Label("advertTypeLabel", "Typ"));
			add(new Label("advertType", job.getAdvertType().getName()));
			
			add(new Label("specificationLabel", "Popis"));
			add(new Label("specification", job.getSpecification()));

			add(new Label("startDateLabel", "Zaèiatok"));
			add(new Label("startDate", dateFormat.format(job.getStartDate())));
			
			add(new Label("hourPaymentLabel", "Mzda"));
			add(new Label("hourPayment", job.getHourPayment().toString()));
			
			add(new Label("workDurationLabel", "Trvanie práce"));
			add(new Label("workDuration", job.getWorkDuration().toString()));
			
			add(new Label("sectorLabel","Sektor:"));
			add(new Label("sector", job.getJobSector().getName()));
			
			add(new Label("contractLabel","Typ úväzku:"));
			add(new Label("contract", job.getContract().getName()));
			
			add(new Label("townLabel","Okres:"));
			add(new Label("town", job.getCounty().getName()));
			
			add(new Label("cmsContent",job.getCmsContent()).setEscapeModelStrings(false));
		}

	}
}
