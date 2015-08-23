package sk.stuba.fiit.ztpPortal.module.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.Style;

public class JobView extends CorePage {

	private static final long serialVersionUID = 1L;

	private static CoreFeedBackPanel feedbackPanel;
	
	private RegisteredUserController userController;
	private JobController jobController;
	private String login;
	private RegisteredUser user;
	private Job job;

	public JobView(PageParameters parameters) {
		
		jobController = new JobController();
		
		if(parameters.containsKey("id")) {
			Long id = parameters.getLong("id"); 
			System.out.println(">> "+id);
			job = jobController.getJobByJobId(id);
		}
		
		if (getSession().getClass()==CoreSession.class){
			login = ((CoreSession) getSession()).getLoged();
			add(HeaderContributor.forCss(((CoreSession) getSession())
					.getUserStyle()));
		}	//toto robim kvoli prekliku admina
		else add(HeaderContributor.forCss(Style.NORMAL));
		
		
		userController = new RegisteredUserController();

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
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		public JobDetailForm(final String id) {
			super(id);

			add(new Label("advertTypeLabel", "Typ"));
			add(new Label("advertType", job.getAdvertType().getName()));
			
			add(new Label("specificationLabel", "Popis"));
			add(new Label("specification", job.getSpecification()));

			add(new Label("startDateLabel", "Zaèiatok"));
			add(new Label("startDate", dateFormat.format(job.getStartDate())));
			
			add(new Label("hourPaymentLabel", "Mzda (EUR)"));
			add(new Label("hourPayment", job.getHourPayment().toString()));
			
			add(new Label("workDurationLabel", "Trvanie práce (dni)"));
			add(new Label("workDuration", job.getWorkDuration().toString()));
			
			add(new Label("sectorLabel","Sektor:"));
			add(new Label("sector", job.getJobSector().getName()));
			
			add(new Label("contractLabel","Typ úväzku:"));
			add(new Label("contract", job.getContract().getName()));
			
			if (job.getCounty() != null){
				add(new Label("countryLabel","Kraj:"));
				add(new Label("country", job.getCounty().getCountry().getName()));
				
				add(new Label("countyLabel","Okres:"));
				add(new Label("county", job.getCounty().getName()));
			} else {
				add(new Label("countryLabel","Kraj:"));
				add(new Label("country", "Neuvedené"));
				
				add(new Label("countyLabel","Okres:"));
				add(new Label("county", "Neuvedené"));
			}
			
			add(new Label("townLabel","Mesto:"));
			add(new Label("town", job.getTown()));
			
			add(new Label("cmsContent",job.getCmsContent()).setEscapeModelStrings(false));
		}

	}
}
