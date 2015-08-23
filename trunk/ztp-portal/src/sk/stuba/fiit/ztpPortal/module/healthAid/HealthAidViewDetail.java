package sk.stuba.fiit.ztpPortal.module.healthAid;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
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
import sk.stuba.fiit.ztpPortal.databaseController.HandicapTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.HealthAidController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.HandicapType;
import sk.stuba.fiit.ztpPortal.databaseModel.HealthAid;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class HealthAidViewDetail extends CorePage {

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private HealthAidController infoController;
	private HealthAid healthAid;

	
	public HealthAidViewDetail(long id) {

		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		infoController = new HealthAidController();
		
		healthAid = infoController.getHealthAidByHealthAidId(id);
		
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
		Link healthAidDetailLink = new Link("healthAidListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new HealthAidList());
			}
		};
		add(healthAidDetailLink);
		
		Link newHealthAidLink = new Link("newHealthAidLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new HealthAidDetail());
			}
		};
		add(newHealthAidLink);
		
		add(new Link("commentLink"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new HealthAidCommentList(healthAid));
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
			private DropDownChoice handicapType;
		 		
	 		private final List<String> HANDICAPTYPE_LIST = Arrays.asList(new HandicapTypeController().getHandicapNameList());
	 		
			
		public EventViewDetailForm(final String id) {
			super(id);


			name = new TextField("name", new PropertyModel(
					properties, "name"));
			name.setRequired(true);
			name.setLabel(new Model("Popis"));
			value[0] = healthAid.getName();
			name.setModelValue(value);
			name.add(StringValidator.maximumLength(30));
			add(name);
			add(new Label("nameLabel", "Popis *"));

			HandicapType postihnutie = new HandicapType();
 			handicapType= new DropDownChoice("handicapType", new PropertyModel(postihnutie,
 					"name"),HANDICAPTYPE_LIST);
 			handicapType.setRequired(true);
 			handicapType.setLabel(new Model("Typ postihnutia"));
 			if (healthAid.getHandicapType()!=null)
 				postihnutie.setName(healthAid.getHandicapType().getName());
 			add(handicapType);
 			add(new Label("handicapTypeLabel","Handicap *"));
 			add(new FormComponentFeedbackBorder("handicapTypeAsterix").add(handicapType));
			
			value[0] = healthAid.getCmsContent();
			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.add(StringValidator.maximumLength(5000));
			textArea.setModelValue(value);
			textArea.setEscapeModelStrings(false);
			add(textArea);
			
			value[0]=String.valueOf(healthAid.isActive());
			active = new CheckBox("active", new PropertyModel(properties,"active"));
			active.setModelValue(value);
			add(active);
			add(new Label("activeLabel","Aktívny"));
			
			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}

		protected void onSubmit() {
			try {
		
				healthAid.setName(name.getModelObjectAsString());
				healthAid.setHandicapType(new HandicapTypeController().getHandicapByName(handicapType.getModelObjectAsString()));
				healthAid.setCmsContent(textArea.getValue());

				// not-user entered data
				healthAid.setActive(Boolean.valueOf(active.getModelObjectAsString()));

				if (infoController.updateHealthAid(healthAid))
					feedbackPanel.info("Inzerát úspešne aktualizovaný");
				else
					feedbackPanel.warn("Chyba pri vkladaní");
			} catch (NumberFormatException e) {
				
			}
		}

	}

}
