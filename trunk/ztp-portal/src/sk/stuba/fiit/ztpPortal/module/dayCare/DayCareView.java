package sk.stuba.fiit.ztpPortal.module.dayCare;

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
import sk.stuba.fiit.ztpPortal.databaseController.DayCareController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.DayCare;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.Style;

public class DayCareView extends CorePage {

	private static final long serialVersionUID = 1L;

	private static CoreFeedBackPanel feedbackPanel;
	
	private RegisteredUserController userController;
	private DayCareController dayCareController;
	private String login;
	private RegisteredUser user;
	private DayCare dayCare;

	public DayCareView(long id) {

		if (getSession().getClass()==CoreSession.class){
			login = ((CoreSession) getSession()).getLoged();
			add(HeaderContributor.forCss(((CoreSession) getSession())
					.getUserStyle()));
		}	//toto robim kvoli prekliku admina
		else add(HeaderContributor.forCss(Style.NORMAL));
		
		
		userController = new RegisteredUserController();
		dayCareController = new DayCareController();
		
		dayCare = dayCareController.getDayCareByDayCareId(id);

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
	        add(feedbackPanel);
		
		setPageHeaderPanel();

		setPageLeftNavigation();

		
		add(new DayCareDetailForm("dayCareDetailForm"));
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}

	private void setPageLeftNavigation(){
		Link dayCareListLink = new Link("dayCareListLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new DayCareList());
			}
		};
		add(dayCareListLink);
		
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
	
	public final class DayCareDetailForm extends Form {

		private static final long serialVersionUID = 1L;
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		public DayCareDetailForm(final String id) {
			super(id);

			add(new Label("advertTypeLabel", "Typ"));
			add(new Label("advertType", dayCare.getAdvertType().getName()));
			
			add(new Label("shortDescLabel", "Názov"));
			add(new Label("shortDesc", dayCare.getShortDesc()));

			add(new Label("startDateLabel", "Zaèiatok"));
			add(new Label("startDate", dateFormat.format(dayCare.getStartDate())));
			
			add(new Label("endDateLabel", "Trvanie - do"));
			add(new Label("endDate", dateFormat.format(dayCare.getEndDate())));

			//add(new Label("countryLabel","Kraj:"));
			//add(new Label("country", dayCare.getCounty().getCountry().getName())); FIXME: LazyInitializationException
			
			add(new Label("countyLabel","Okres:"));
			if (dayCare.getCounty()!=null)
				add(new Label("county", dayCare.getCounty().getName()));
			else add(new Label("county", "Neudaný"));
			
			add(new Label("townLabel","Mesto:"));
			add(new Label("town", dayCare.getTown()));
			
			add(new Label("description",dayCare.getDescription()).setEscapeModelStrings(false)); //TODO: lepsie formatovanie
		}

	}
}
