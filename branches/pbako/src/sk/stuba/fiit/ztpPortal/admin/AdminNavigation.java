package sk.stuba.fiit.ztpPortal.admin;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.Link;

import sk.stuba.fiit.stuba.ztpPortal.admin.event.EventList;
import sk.stuba.fiit.ztpPortal.admin.forum.ThemeList;
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
	

}
