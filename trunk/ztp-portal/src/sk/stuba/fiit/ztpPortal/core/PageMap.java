package sk.stuba.fiit.ztpPortal.core;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.protocol.http.WebResponse;

import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.module.accomodation.LivingDetail;
import sk.stuba.fiit.ztpPortal.module.dayCare.DayCareDetail;
import sk.stuba.fiit.ztpPortal.module.education.CourseDetail;
import sk.stuba.fiit.ztpPortal.module.education.SchoolDetail;
import sk.stuba.fiit.ztpPortal.module.event.EventDetail;
import sk.stuba.fiit.ztpPortal.module.healthAid.HealthAidDetail;
import sk.stuba.fiit.ztpPortal.module.information.InfoDetail;
import sk.stuba.fiit.ztpPortal.module.job.JobDetail;

public class PageMap extends CorePage {

	private static final long serialVersionUID = 1L;

	private static PageRenderer pageRenderer;
	private static CoreFeedBackPanel feedbackPanel;
	
	private RegisteredUserController userController;

	
	private String login;
	private RegisteredUser user;

	// TODO neviem co to nefunguje
	protected void setHeaders(WebResponse response)
    {
			response.setHeader("Cache-Control", "no-cache, max-age=0, must-revalidate"); // no-store
	        //response.setHeader("Cache-Control", "no-store");
	        response.setDateHeader("Expires", 0);
	        response.setHeader("Pragma", "no-cache"); 
    } 
	
	public PageMap() {

		// nastavim CSS
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
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
		
		setInformationPanel();
		setEventPanel();
		setJobPanel();
		setEducationPanel();
		setLivingPanel();
		setHealthAidPanel();
		setCarePanel();
		setForumPanel();
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel", user);
		add(panel);
	}
	
	private void setInformationPanel() {
		add(pageRenderer.getInformationLink());
		
		Link eventDetailLink = new Link("newInfoLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new InfoDetail());
			}
		};
		add(eventDetailLink);
	}
	
	private void setEventPanel() {
		add(pageRenderer.getEventLink());
		
		Link eventDetailLink = new Link("newEventLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new EventDetail());
			}
		};
		add(eventDetailLink);
	}
	
	private void setJobPanel() {
		add(pageRenderer.getJobListLink());
		
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
	
	private void setEducationPanel() {
		add(pageRenderer.getEducationLink());
		
		Link schoolDetailLink = new Link("newSchoolLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new SchoolDetail());
			}
		};
		add(schoolDetailLink);
		
		Link courseDetailLink = new Link("newCourseLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new CourseDetail());
			}
		};
		add(courseDetailLink);
	}
	
	private void setLivingPanel() {
		add(pageRenderer.getLivingLink());
		
		Link livingDetailLink = new Link("newLivingLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new LivingDetail());
			}
		};
		add(livingDetailLink);
	}
	
	private void setHealthAidPanel() {
		add(pageRenderer.getHealthAidLink());
		
		Link healthDetailLink = new Link("newHealthAidLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new HealthAidDetail());
			}
		};
		add(healthDetailLink);
	}
	
	private void setCarePanel() {
		add(pageRenderer.getDayCareLink());
		
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
	
	private void setForumPanel() {
		add(pageRenderer.getForumLink());
	}
}
