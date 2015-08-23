package sk.stuba.fiit.ztpPortal.module.accomodation;

import java.sql.BatchUpdateException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
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
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.NumberValidator;
import org.apache.wicket.validation.validator.StringValidator;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.core.PageNoAdmitance;
import sk.stuba.fiit.ztpPortal.core.RegionSelector;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.LivingController;
import sk.stuba.fiit.ztpPortal.databaseController.LivingTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.StuffTypeController;
import sk.stuba.fiit.ztpPortal.databaseModel.Living;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class LivingDetail extends CorePage{
	
	private static final Log log = LogFactory.getLog(LivingDetail.class);
	
	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private ModalWindowPage modal;
	private Living living;
	
	public LivingDetail(){
		
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
		
		living = new Living();
		modal = new ModalWindowPage(living);

		setPageHeaderPanel();
		setPageLeftNavigation();
		
		// formular vlozim
		
		add(new LivingDetailForm("livingDetailForm"));
		
		add(modal.getLabel());
		add(modal.getModal());
	}
	
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
		
	}
	
	private void setPageLeftNavigation() {
		Link eventDetailLink = new Link("livingListLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new LivingList());
			}
		};
		add(eventDetailLink);
		add(modal.getLink());
	}
	
	public final class LivingDetailForm extends Form {

		private static final long serialVersionUID = 1L;

		private DateTextField livingDate;

		private TextField roomCount;
		private TextField address;
		private TextField town;
		private TextField size;
		private TextField price;
		
		private DropDownChoice livingType;
		private DropDownChoice stuffType;

		private TextArea textArea;

		private TinyMceBehavior tinyMCE;
		
		RegionSelector region = RegionSelector.getSingletonObject();

		private final List<String> LIVING_TYPE_LIST = Arrays
				.asList(new LivingTypeController().getLivingTypeNameList());
		
		private final List<String> STUFF_TYPE_LIST = Arrays
		.asList(new StuffTypeController().getStuffTypeNameList());

		private Button submit;
		private ValueMap properties = new ValueMap();

		IValidator numberValidateMin = NumberValidator.minimum(0);
		IValidator numberValidateMax = NumberValidator.maximum(999);

		public LivingDetailForm(final String id) {
			super(id);

			livingType = new DropDownChoice("livingType", new PropertyModel(
					properties, "livingType"), LIVING_TYPE_LIST);
			livingType.setRequired(true);
			livingType.setLabel(new Model("Typ inzerátu"));
			add(livingType);
			add(new Label("livingTypeLabel", "Typ inzerátu *"));
			add(new FormComponentFeedbackBorder("livingTypeAsterix").add(livingType));

			stuffType = new DropDownChoice("stuffType", new PropertyModel(
					properties, "stuffType"), STUFF_TYPE_LIST);
			stuffType.setRequired(true);
			stuffType.setLabel(new Model("Typ zariadenia"));
			add(stuffType);
			add(new Label("stuffTypeLabel", "Typ zariadenia *"));
			add(new FormComponentFeedbackBorder("stuffTypeAsterix").add(stuffType));
			
			roomCount = new TextField("roomCount", new PropertyModel(
					properties, "roomCount"), Integer.class);
			roomCount.setLabel(new Model("Poèet izieb"));
			roomCount.add(numberValidateMin);
			roomCount.add(numberValidateMax);
			add(roomCount);
			add(new Label("roomCountLabel", "Poèet izieb"));
			add(new FormComponentFeedbackBorder("roomCountAsterix").add(roomCount));

			price = new TextField("price", new PropertyModel(
					properties, "price"),Integer.class);
			price.setLabel(new Model("Cena"));
			price.add(numberValidateMin);
			price.add(numberValidateMax);
			add(price);
			add(new Label("priceLabel", "Cena"));
			add(new FormComponentFeedbackBorder("priceAsterix").add(price));
			
			size = new TextField("size", new PropertyModel(
					properties, "size"),Integer.class);
			size.setLabel(new Model("Cena"));
			size.add(numberValidateMin);
			size.add(numberValidateMax);
			add(size);
			add(new Label("sizeLabel", "Rozloha"));
			add(new FormComponentFeedbackBorder("sizeAsterix").add(size));
			
			livingDate = new DateTextField("livingDate", new PropertyModel(
					properties, "livingDate"), "dd.MM.yyyy");
			livingDate.add(new DatePicker());
			livingDate.setLabel(new Model("Dátum ponuky"));
			livingDate.setRequired(true);
			add(livingDate);
			add(new Label("livingDateLabel", "Dátum ponuky *"));
			add(new FormComponentFeedbackBorder("livingDateAsterix").add(livingDate));
						
			address = new TextField("address", new PropertyModel(
					properties, "address"));
			address.setLabel(new Model("Adresa"));
			add(address);
			add(new Label("addressLabel", "Adresa"));
			add(new FormComponentFeedbackBorder("addressAsterix").add(address));

			///
			
			add(region.getCountryDropDownChoice());
			add(region.getCountyDropDownChoice());
			
			add(new Label("countryLabel", "Kraj *"));
			add(new Label("countyLabel", "Okres"));
			////
			
			town = new TextField("town", new PropertyModel(properties,
					"town"));
			town.setRequired(true);
			town.setLabel(new Model("Mesto"));
			add(town);
			add(new Label("townLabel", "Mesto *"));
			add(new FormComponentFeedbackBorder("townAsterix").add(town));
			
			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			textArea.add(tinyMCE);
			textArea.add(StringValidator.maximumLength(5000));
			textArea.setEscapeModelStrings(false);
			add(textArea);

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}

		protected void onSubmit() {
			try {
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				
				String startDate = livingDate.getModelObjectAsString();

				Date dateStart = null;
				dateFormat.parse(startDate);
				dateStart = dateFormat.parse(startDate);
				java.sql.Date dateS = new java.sql.Date(dateStart.getTime());
				
				living.setRoomCount((Integer)roomCount.getModelObject());
				living.setSize(Integer.parseInt(size.getModelObjectAsString()));
				living.setPrice(Integer.parseInt(price.getModelObjectAsString()));
				living.setLivingDate(dateS);
				living.setLivingType(new LivingTypeController()
						.getLivingTypeByName(livingType
								.getModelObjectAsString()));
				living.setStuffType(new StuffTypeController()
				.getStuffTypeByName(stuffType
						.getModelObjectAsString()));
				living.setCounty(new CountyController().getCountyByName(region.getMyCounty()));
				living.setTown(town.getModelObjectAsString());
				living.setNote(textArea.getValue());
				living.setAddress(address.getModelObjectAsString());


				// not-user entered data
				living.setState(true);
				living.setActive(true);
				living.setOwner(user);

				LivingController livingController = new LivingController();

				if (livingController.saveNewLiving(living)){
					submit.setVisible(false);
					feedbackPanel.info("Inzerát úspešne vložený");
				}
				else{
					log.warn("Chyba pri vkladaní");
					feedbackPanel.warn("Chyba pri vkladaní");
				}

			} catch (BatchUpdateException e){
				log.warn(e.getMessage());
			} catch (ParseException e) {
				log.warn(e.getMessage());
			} catch (NumberFormatException e) {
				log.warn(e.getMessage());
			}
		}

	}
	
}