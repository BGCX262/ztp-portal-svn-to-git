package sk.stuba.fiit.ztpPortal.core;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ResourceModel;

import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.Style;

public class StyleChooser extends CorePage {
	private static final long serialVersionUID = 1L;
	
	private static CoreFeedBackPanel feedbackPanel;
	
	private Button normalStyleButton;
	private Button handicapStyle_contrast;
	private Button handicapStyle_simple;
	
	private String login;
	private RegisteredUser user;
	private RegisteredUserController userController;

	private PageRenderer pageRenderer;

	private Form loginForm;

	private Form statusForm;
	
	public StyleChooser(){
		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);
		setPageHeaderPanel();
		add(new FooterPanel("footPanel"));
		
		StyleForm form = new StyleForm("form");
		add(form);
	}
	
	private void setPageHeaderPanel() {
		
		pageRenderer = new PageRenderer();
		
		pageRenderer.setUser(user);
		loginForm = pageRenderer.getLoginForm();
		add(loginForm);
		statusForm = pageRenderer.getStatusForm();
		add(statusForm);
	
		if (user==null) pageRenderer.disableStatusForm();
		else pageRenderer.disableLoginForm();
	}
	
	public final class StyleForm extends Form {

		private static final long serialVersionUID = 1L;
		CoreSession session = ((CoreSession) getSession());
		
		public StyleForm(final String id) {
			super(id);
			
			normalStyleButton = new Button("normalStyle",new ResourceModel(
			"button.submit")){
				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					System.out.println("Normal Style");
					
					session.setUserStyle(new Style().getNormalSeasonStyle());
					
					setResponsePage(MainPage.class);
				}
			};
			
			handicapStyle_contrast = new Button("handicapStyle_contrast",new ResourceModel(
			"button.handicapStyle_contrast")){
				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					System.out.println("Hight Contrast");
					
					session.setUserStyle(Style.HIGH_CONTRAST);
					
					setResponsePage(MainPage.class);
				}
			};
			
			handicapStyle_simple = new Button("handicapStyle_simple",new ResourceModel(
			"button.handicapStyle_simple")){
				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					System.out.println("Simple Style");
					
					session.setUserStyle(Style.SIMPLE);
					
					setResponsePage(MainPage.class);
				}
			};
			
			add(normalStyleButton);
			add(handicapStyle_contrast);
			add(handicapStyle_simple);
		}
	}
}
