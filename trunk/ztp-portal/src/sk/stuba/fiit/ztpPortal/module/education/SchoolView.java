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
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.SchoolController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.School;
import sk.stuba.fiit.ztpPortal.server.Style;

public class SchoolView extends CorePage{
	
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private School school;
	
	public SchoolView(long schoolId){
		
		if (getSession().getClass()==CoreSession.class){
			login = ((CoreSession) getSession()).getLoged();
			add(HeaderContributor.forCss(((CoreSession) getSession())
					.getUserStyle()));
		}	//toto robim kvoli prekliku admina
		else add(HeaderContributor.forCss(Style.NORMAL));
		
		userController = new RegisteredUserController();
		
		school = new SchoolController().getSchoolBySchoolId(schoolId);
		
		if (login != null){
			user = userController.getRegisteredUserByLogin(login);
		}
		else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();
		setPageLeftNavigation();
		
		// formular vlozim
		
		add(new SchoolViewForm("schoolViewForm"));
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
	
	public final class SchoolViewForm extends Form {

		private static final long serialVersionUID = 1L;
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		public SchoolViewForm(final String id) {
			super(id);
			
			add(new Label("schoolTypeLabel","Typ"));
			add(new Label("schoolType",school.getSchoolType().getName()));
			
			add(new Label("schoolNameLabel","Názov"));
			add(new Label("schoolName",school.getName()));
			
			add(new Label("schoolContactPersonLabel","Kontaktná osoba"));
			add(new Label("schoolContactPerson",school.getContactPerson()));
			
			add(new Label("schoolEmailContactLabel","Kontaktný email"));
			add(new Label("schoolEmailContact",school.getEmail()));
			
			add(new Label("schoolPhoneLabel","Telefónny kontakt"));
			add(new Label("schoolPhone",school.getPhone()));
			
			add(new Label("schoolHomePageLabel", "Stránka www"));
			add(new Label("schoolHomePage",school.getHomePage()));
			
			add(new Label("schoolAddressLabel","Adresa"));
			add(new Label("schoolAdress",school.getAddress()));
			
			if (school.getCounty()!= null){
			add(new Label("schoolCountryLabel","Kraj"));
			add(new Label("schoolCountry", school.getCounty().getCountry().getName()));
			
			add(new Label("schoolCountyLabel","Okres"));
			add(new Label("schoolCounty", school.getCounty().getName()));
			} else {
				add(new Label("schoolCountryLabel","Kraj"));
				add(new Label("schoolCountry", "Neuvedené"));
				
				add(new Label("schoolCountyLabel","Okres"));
				add(new Label("schoolCounty", "Neuvedené"));
			}
			
			add(new Label("schoolTownLabel","Mesto"));
			add(new Label("schoolTown", school.getTown()));

			add(new Label("cmsContent",school.getNote()).setEscapeModelStrings(false));
			
		}

	}
	

}
