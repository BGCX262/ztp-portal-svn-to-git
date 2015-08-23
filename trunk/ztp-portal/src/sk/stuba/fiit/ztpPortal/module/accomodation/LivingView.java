package sk.stuba.fiit.ztpPortal.module.accomodation;

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
import sk.stuba.fiit.ztpPortal.databaseController.LivingController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Living;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.Style;

public class LivingView extends CorePage{
	
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private LivingController livingController;
	private Living living;
	
	public LivingView(long id){
	
		if (getSession().getClass()==CoreSession.class){
			login = ((CoreSession) getSession()).getLoged();
			add(HeaderContributor.forCss(((CoreSession) getSession())
					.getUserStyle()));
		}	//toto robim kvoli prekliku admina
		else add(HeaderContributor.forCss(Style.NORMAL));
		
		userController = new RegisteredUserController();
		livingController = new LivingController();
		
		living = livingController.getLivingByLivingId(id);
		
		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);
		
		setPageHeaderPanel();
		setPageLeftNavigation();
		
		//form
		
		add(new LivingViewForm("livingViewForm"));
		
	}
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);	}

	private void setPageLeftNavigation() {
		Link livingListLink = new Link("livingListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new LivingList());
			}
		};
		add(livingListLink);
		
		Link newLivingLink = new Link("newLivingLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new LivingDetail());
			}
		};
		add(newLivingLink);
		
		
		add(new Link("pictureView"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new PictureViewer(living.getId()));
			} 
		});
	}
	
	public final class LivingViewForm extends Form{
		
		private static final long serialVersionUID = 1L;
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		public LivingViewForm(String id) {
			super(id);
			
			add(new Label("livingTypeLabel","Typ"));
			add(new Label("livingType",living.getLivingType().getName()));
			
			add(new Label("livingStuffTypeLabel","Typ"));
			add(new Label("livingStuffType",living.getStuffType().getName()));
			
			add(new Label("livingSizeLabel","Rozloha"));
			add(new Label("livingSize",String.valueOf(living.getSize())));
			
			add(new Label("livingPriceLabel","Cena"));
			add(new Label("livingPrice",String.valueOf(living.getPrice())));
			
			add(new Label("livingRoomCountLabel","Poèet izieb"));
			add(new Label("livingRoomCount",String.valueOf(living.getRoomCount())));
			
			add(new Label("livingDateLabel", "Zaèiatok"));
			add(new Label("livingDate", dateFormat.format(living.getLivingDate())));
			
			add(new Label("livingAddressLabel","Adresa"));
			add(new Label("livingAddress",living.getAddress()));
			
			if (living.getCounty()!=null){
				add(new Label("livingCountryLabel","Kraj"));
				add(new Label("livingCountry", living.getCounty().getCountry().getName()));
				
				add(new Label("livingCountyLabel","Okres"));
				add(new Label("livingCounty", living.getCounty().getName()));
			} else {
				add(new Label("livingCountryLabel","Kraj"));
				add(new Label("livingCountry", "Neuvedené"));
				
				add(new Label("livingCountyLabel","Okres"));
				add(new Label("livingCounty", "Neuvedené"));
			}
			
			add(new Label("livingTownLabel","Mesto"));
			add(new Label("livingTown", living.getTown()));
			
			add(new Label("cmsContent",living.getNote()).setEscapeModelStrings(false));
			
		}
		
		
	}
	

}
