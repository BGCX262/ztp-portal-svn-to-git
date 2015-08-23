package sk.stuba.fiit.ztpPortal.module.information;

import java.sql.BatchUpdateException;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.StringValidator;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.core.PageNoAdmitance;
import sk.stuba.fiit.ztpPortal.databaseController.InformationController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Information;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class InfoDetail extends CorePage{
	
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	
	public InfoDetail(){
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		
		if (login != null){
			user = userController.getRegisteredUserByLogin(login);
		}
		else
			throw new RestartResponseAtInterceptPageException(
				      new PageNoAdmitance(this));

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();
		setPageLeftNavigation();
		
		// formular vlozim
		
		add(new EventDetailForm("eventDetailForm"));
	}
	
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}
	
	private void setPageLeftNavigation() {
		Link eventDetailLink = new Link("infoListLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new InfoList());
			}
		};
		add(eventDetailLink);
	}
	
	public final class EventDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		private TextArea textArea;
		private TinyMceBehavior tinyMCE;
		private TextField name;

		private Button submit;
		private ValueMap properties = new ValueMap();

		public EventDetailForm(final String id) {
			super(id);
			
			name = new TextField("name", new PropertyModel(
					properties, "name"));
			name.setRequired(true);
			name.setLabel(new Model("Popis"));
			name.add(StringValidator.maximumLength(30));
			add(name);
			add(new Label("nameLabel", "Popis *"));
			add(new FormComponentFeedbackBorder("nameAsterix").add(name));
			
			textArea = new TextArea("richTextInput", new Model());
			textArea.add(StringValidator.maximumLength(5000));
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.setEscapeModelStrings(false);
			add(textArea);

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}

		protected void onSubmit() {
			try {
				
				Information information = new Information();
				
				information.setCmsContent(textArea.getValue());
				information.setName(name.getModelObjectAsString());

				// not-user entered data
				information.setState(true);
				information.setActive(true);
				information.setOwner(user);

				InformationController infoController = new InformationController();

				if (infoController.saveNewInformation(information)){
					submit.setVisible(false);
					feedbackPanel.info("Inzerát úspešne vložený");
				}
				else
					feedbackPanel.warn("Chyba pri vkladaní");

			} catch (BatchUpdateException e){
				System.out.println(e.getMessage());
				System.out.println(e.getNextException());
			} catch (NumberFormatException e) {
				
			}
		}

	}
	
}