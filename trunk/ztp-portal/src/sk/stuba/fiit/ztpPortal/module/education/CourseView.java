package sk.stuba.fiit.ztpPortal.module.education;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.databaseController.CourseController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Course;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.Style;

public class CourseView extends CorePage{
	
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private CourseController courseController;
	private Course course;
	
	public CourseView(long id){
	
		if (getSession().getClass()==CoreSession.class){
			login = ((CoreSession) getSession()).getLoged();
			add(HeaderContributor.forCss(((CoreSession) getSession())
					.getUserStyle()));
		}	//toto robim kvoli prekliku admina
		else add(HeaderContributor.forCss(Style.NORMAL));
		
		userController = new RegisteredUserController();
		courseController = new CourseController();
		
		course = courseController.getCourseByCourseId(id);
		
		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);
		
		setPageHeaderPanel();
		setPageLeftNavigation();
		
		
		//form
		
		add(new CourseViewForm("courseViewForm"));
		
	}
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}

	private void setPageLeftNavigation() {
		Link eventDetailLink = new Link("schoolListLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new EducationList());
			}
		};
		add(eventDetailLink);
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
	
	public final class CourseViewForm extends Form{
		
		private static final long serialVersionUID = 1L;
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		public CourseViewForm(String id) {
			super(id);
			
			add(new Label("courseTypeLabel","Typ"));
			add(new Label("courseType",course.getCourseType().getName()));
			
			add(new Label("couserNameLabel","Názov"));
			add(new Label("courseName",course.getName()));
			
			add(new Label("courseContactPersonLabel","Kontaktná osoba"));
			add(new Label("courseContactPerson",course.getContactPerson()));
			
			add(new Label("courseEmailContactLabel","Kontaktný email"));
			add(new Label("courseEmailContact",course.getEmail()));
			
			add(new Label("coursePhoneLabel","Telefónny kontakt"));
			add(new Label("coursePhone",course.getPhone()));
			
			add(new Label("coursePriceLabel","Cena (eur)"));
			add(new Label("coursePrice",String.valueOf(course.getPrice())));
			
			add(new Label("courseStartDateLabel", "Zaèiatok"));
			add(new Label("courseStartDate", dateFormat.format(course.getStartDate())));
			
			add(new Label("courseAddressLabel","Adresa"));
			add(new Label("courseAddress",course.getAddress()));
			
			if (course.getCounty()!=null){
				add(new Label("courseCountryLabel","Kraj"));
				add(new Label("courseCountry", course.getCounty().getCountry().getName()));
				
				add(new Label("courseCountyLabel","Okres"));
				add(new Label("courseCounty", course.getCounty().getName()));
			} else {
				add(new Label("courseCountryLabel","Kraj"));
				add(new Label("courseCountry", "Neuvedené"));
				
				add(new Label("courseCountyLabel","Okres"));
				add(new Label("courseCounty", "Neuvedené"));
			}
			
			add(new Label("courseTownLabel","Mesto"));
			add(new Label("courseTown", course.getTown()));
			
			add(new Label("cmsContent",course.getCmsContent()).setEscapeModelStrings(false));
			
		}
		
		
	}
	

}
