package sk.stuba.fiit.ztpPortal.admin;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.Link;

import sk.stuba.fiit.ztpPortal.admin.accomodation.LivingList;
import sk.stuba.fiit.ztpPortal.admin.dayCare.DayCareList;
import sk.stuba.fiit.ztpPortal.admin.education.EducationList;
import sk.stuba.fiit.ztpPortal.admin.event.EventList;
import sk.stuba.fiit.ztpPortal.admin.forum.ThemeList;
import sk.stuba.fiit.ztpPortal.admin.healthAid.HealthAidList;
import sk.stuba.fiit.ztpPortal.admin.information.InfoList;
import sk.stuba.fiit.ztpPortal.admin.job.JobList;

public class AdminNavigation extends AdminPage {

	private static final long serialVersionUID = 1L;

	public Link getUserLogOutLink() {

		Link logoutLink = new Link("logoutLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {

				((AdminSession) getSession()).invalidateNow();
				setResponsePage(LoginPage.class);
			}
		};
		return logoutLink;
	}

	public Link getUserListLink() {

		Link userListLink = new Link("userListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {

				setResponsePage(new UserListPage());
			}
		};

		return userListLink;

	}
	public Link getContentListLink() {

		Link contentListLink = new Link("contentListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {

				setResponsePage(new ContentPage());
			}
		};

		return contentListLink;

	}
	
	public Link getJobListLink() {

		Link jobListLink = new Link("jobListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {

				setResponsePage(new JobList());
			}
		};

		return jobListLink;

	}

	public Component getGlobalSettingLink() {
		Link jobListLink = new Link("globalSettingLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {

				setResponsePage(new GlobalSettingPage());
			}
		};

		return jobListLink;
	}

	public Component getForumListLink() {
		Link jobListLink = new Link("forumListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {

				setResponsePage(new ThemeList());
			}
		};

		return jobListLink;
	}
	
	public Component getEventListLink() {
		Link jobListLink = new Link("eventListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {

				setResponsePage(new EventList());
			}
		};

		return jobListLink;
	}
	
	public Component getEducationListLink() {
		Link jobListLink = new Link("educationListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {

				setResponsePage(new EducationList());
			}
		};

		return jobListLink;
	}

	public Component getLivingListLink() {
		Link jobListLink = new Link("livingListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {

				setResponsePage(new LivingList());
			}
		};

		return jobListLink;
	}
	
	public Component getInformationListLink() {
		Link jobListLink = new Link("infoListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {

				setResponsePage(new InfoList());
			}
		};

		return jobListLink;
	}
	
	public Component getHealthAidListLink() {
		Link jobListLink = new Link("healthAidListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {

				setResponsePage(new HealthAidList());
			}
		};

		return jobListLink;
	}
	
	public Component getDayCareListLink() {
		Link jobListLink = new Link("dayCareListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {

				setResponsePage(new DayCareList());
			}
		};

		return jobListLink;
	}
	
}
