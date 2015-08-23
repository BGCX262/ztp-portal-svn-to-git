package sk.stuba.fiit.ztpPortal.module.information;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
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
import sk.stuba.fiit.ztpPortal.databaseController.InformationController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Information;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class InfoViewDetail extends CorePage {

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private InformationController infoController;
	private Information information;

	
	public InfoViewDetail(long id) {

		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		infoController = new InformationController();
		
		information = infoController.getInformationByInformationId(id);
		
		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		setPageHeaderPanel();

		setPageLeftNavigation();

		// formular vlozim
		add(new EventViewDetailForm("eventDetailForm"));
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);

	}

	private void setPageLeftNavigation() {
		Link eventDetailLink = new Link("infoListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new InfoList());
			}
		};
		add(eventDetailLink);
		
		Link newEventLink = new Link("newInfoLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new InfoDetail());
			}
		};
		add(newEventLink);
		
		add(new Link("commentLink"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new InfoCommentList(information));
			} 
		});
		
	}
	

	public final class EventViewDetailForm extends Form {

			private static final long serialVersionUID = 1L;
			private TextField name;
			private TextArea textArea;
			private CheckBox active;
			private TinyMceBehavior tinyMCE;
			private Button submit;
			private ValueMap properties = new ValueMap();
			private String[] value = new String[1];
			
		public EventViewDetailForm(final String id) {
			super(id);


			name = new TextField("name", new PropertyModel(
					properties, "name"));
			name.setRequired(true);
			name.setLabel(new Model("Popis"));
			value[0] = information.getName();
			name.setModelValue(value);
			name.add(StringValidator.maximumLength(30));
			add(name);
			add(new Label("nameLabel", "Popis *"));

			value[0] = information.getCmsContent();
			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(StringValidator.maximumLength(5000));
			textArea.add(tinyMCE);
			textArea.setModelValue(value);
			textArea.setEscapeModelStrings(false);
			add(textArea);

			value[0]=String.valueOf(information.isActive());
			active = new CheckBox("active", new PropertyModel(properties,"active"));
			active.setModelValue(value);
			add(active);
			add(new Label("activeLabel","Aktívny"));
			
			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}

		protected void onSubmit() {
			try {
		
				information.setName(name.getModelObjectAsString());
				information.setCmsContent(textArea.getValue());

				// not-user entered data
				information.setActive(Boolean.valueOf(active.getModelObjectAsString()));

				if (infoController.updateInformation(information))
					feedbackPanel.info("Inzerát úspešne aktualizovaný");
				else
					feedbackPanel.warn("Chyba pri vkladaní");
			} catch (NumberFormatException e) {
				
			}
		}

	}

}
