package sk.stuba.fiit.ztpPortal.module.accomodation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
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
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.NumberValidator;
import org.apache.wicket.validation.validator.StringValidator;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.core.RegionSelector;
import sk.stuba.fiit.ztpPortal.core.RegionSelectorView;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.LivingController;
import sk.stuba.fiit.ztpPortal.databaseController.LivingTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.StuffTypeController;
import sk.stuba.fiit.ztpPortal.databaseModel.Living;
import sk.stuba.fiit.ztpPortal.databaseModel.LivingType;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import wicket.contrib.tinymce.TinyMceBehavior;

public class LivingViewDetail extends CorePage {

	private static final Log log = LogFactory.getLog(LivingDetail.class);

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	private LivingController livingController;
	private Living living;
	private ModalWindowPage modal;
	
	public LivingViewDetail(long id) {

		add(HeaderContributor.forCss(((CoreSession) getSession())
				.getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();
		livingController = new LivingController();
		
		living = livingController.getLivingByLivingId(id);
		
		if (login != null) {
			user = userController.getRegisteredUserByLogin(login);
		} else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);

		modal = new ModalWindowPage(living);
		
		setPageHeaderPanel();
		setPageLeftNavigation();


		
		// formular vlozim

		add(new EventViewDetailForm("livingDetailForm"));
		
		add(modal.getLabel());
		add(modal.getModal());
		
	}

	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
	}

	private void setPageLeftNavigation() {
		Link eventDetailLink = new Link("livingListLink") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new LivingList());
			}
		};
		add(eventDetailLink);
		
		add(new Link("pictureView"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new PictureViewer(living.getId()));
			} 
		});
		add(modal.getLink());
	}
	

	public final class EventViewDetailForm extends Form {

			private static final long serialVersionUID = 1L;

			private DateTextField livingDateField;

			private TextField roomCount;
			private TextField address;
			private TextField town;
			private TextField size;
			private TextField price;
			
			private DropDownChoice livingType;
			private DropDownChoice stuffType;

			private TextArea textArea;
			private CheckBox active;
			
			private long actualDate = new Date().getTime();
			private long activeDateEnd=living.getCreateDate().getTime()+5184000;

			private TinyMceBehavior tinyMCE;

			private final List<String> LIVING_TYPE_LIST = Arrays
					.asList(new LivingTypeController().getLivingTypeNameList());

			private final List<String> STUFF_TYPE_LIST = Arrays
					.asList(new StuffTypeController().getStuffTypeNameList());
			
			private Button submit;
			private Button reactivate;
			private ValueMap properties = new ValueMap();
			private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
			private String[] value = new String[1];
			
			private Date livingDate;
			
			IValidator numberValidate = NumberValidator.minimum(0);
			
			private RegionSelectorView regionSelector;
			private RegionSelector region;
			
			public Date getLivingDate(){
				return living.getLivingDate();
			}
			
			public void setLivingDate(Date livingDate){
				this.livingDate = livingDate;
			}
			
			
		public EventViewDetailForm(final String id) {
			super(id);

			LivingType typLiving = new LivingType();
			livingType = new DropDownChoice("livingType", new PropertyModel(
					typLiving, "name"), LIVING_TYPE_LIST);
			typLiving.setName(living.getLivingType().getName());
			livingType.setRequired(true);
			livingType.setLabel(new Model("Typ inzerátu"));
			add(livingType);
			add(new Label("livingTypeLabel", "Typ inzerátu *"));
			add(new FormComponentFeedbackBorder("livingTypeAsterix").add(livingType));

			LivingType typStuff = new LivingType();
			stuffType = new DropDownChoice("stuffType", new PropertyModel(
					typStuff, "name"), STUFF_TYPE_LIST);
			typStuff.setName(living.getStuffType().getName());
			stuffType.setRequired(true);
			stuffType.setLabel(new Model("Typ zariadenia"));
			add(stuffType);
			add(new Label("stuffTypeLabel", "Typ zariadenia *"));
			add(new FormComponentFeedbackBorder("stuffTypeAsterix").add(stuffType));
			
			roomCount = new TextField("roomCount", new PropertyModel(
					properties, "roomCount"), Integer.class);
			roomCount.setLabel(new Model("Poèet izieb"));
			roomCount.add(numberValidate);
			value[0] = String.valueOf(living.getRoomCount());
			roomCount.setModelValue(value);
			add(roomCount);
			add(new Label("roomCountLabel", "Poèet izieb"));
			add(new FormComponentFeedbackBorder("roomCountAsterix").add(roomCount));

			price = new TextField("price", new PropertyModel(
					properties, "price"), Integer.class);
			price.setLabel(new Model("Cena"));
			price.add(numberValidate);
			value[0] = String.valueOf(living.getPrice());
			price.setModelValue(value);
			add(price);
			add(new Label("priceLabel", "Cena"));
			add(new FormComponentFeedbackBorder("priceAsterix").add(price));
			
			size = new TextField("size", new PropertyModel(
					properties, "size"), Integer.class);
			size.setLabel(new Model("Cena"));
			size.add(numberValidate);
			value[0] = String.valueOf(living.getSize());
			size.setModelValue(value);
			add(size);
			add(new Label("sizeLabel", "Rozloha"));
			add(new FormComponentFeedbackBorder("sizeAsterix").add(size));
			
			livingDateField = new DateTextField("livingDate", new PropertyModel(
					this, "livingDate"), "dd.MM.yyyy");
			livingDateField.add(new DatePicker());
			livingDateField.setLabel(new Model("Dátum ponuky"));
			livingDateField.setRequired(true);
			add(livingDateField);
			add(new Label("livingDateLabel", "Dátum ponuky *"));
			add(new FormComponentFeedbackBorder("livingDateAsterix").add(livingDateField));
						
			address = new TextField("address", new PropertyModel(
					properties, "address"));
			address.setLabel(new Model("Adresa"));
			value[0] = String.valueOf(living.getAddress());
			address.setModelValue(value);
			add(address);
			add(new Label("addressLabel", "Adresa"));
			add(new FormComponentFeedbackBorder("addressAsterix").add(address));
			
			town = new TextField("town", new PropertyModel(properties,
					"town"));
			town.setRequired(true);
			town.setLabel(new Model("Mesto"));
			value[0] = String.valueOf(living.getTown());
			town.setModelValue(value);
			add(town);
			add(new Label("townLabel", "Mesto *"));
			add(new FormComponentFeedbackBorder("townAsterix").add(town));
			
			textArea = new TextArea("richTextInput", new Model());
			tinyMCE = new TinyMceBehavior();
			value[0] = String.valueOf(living.getNote());
			textArea.setModelValue(value);
			textArea.add(tinyMCE);
			textArea.add(StringValidator.maximumLength(5000));
			textArea.setEscapeModelStrings(false);
			add(textArea);

			if (living.getCounty() != null) {
				regionSelector = new RegionSelectorView(living.getCounty());
				add(regionSelector.getCountryDropDownChoice());
				add(regionSelector.getCountyDropDownChoice());
			} else {
				region = RegionSelector.getSingletonObject();
				add(region.getCountryDropDownChoice());
				add(region.getCountyDropDownChoice());
			}
			add(new Label("countryLabel", "Kraj *"));
			add(new Label("countyLabel", "Okres"));
			
			value[0]=String.valueOf(living.isActive());
			active = new CheckBox("active", new PropertyModel(properties,"active"));
			active.setModelValue(value);
			add(active);
			add(new Label("activeLabel","Aktívny"));
			
			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
		}

		protected void onSubmit() {
			try {
				
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

				String startDate = livingDateField.getModelObjectAsString();

				Date dateStart = null;
				dateFormat.parse(startDate);
				dateStart = dateFormat.parse(startDate);
				java.sql.Date dateS = new java.sql.Date(dateStart.getTime());
				
				living.setRoomCount((Integer)(roomCount.getModelObject()));
				living.setSize(Integer.parseInt(size.getModelObjectAsString()));
				living.setPrice(Integer.parseInt(price.getModelObjectAsString()));
				living.setLivingDate(dateS);
				living.setLivingType(new LivingTypeController()
						.getLivingTypeByName(livingType
								.getModelObjectAsString()));
				living.setStuffType(new StuffTypeController()
				.getStuffTypeByName(stuffType
						.getModelObjectAsString()));
				if (living.getCounty()!=null)
					living.setCounty(new CountyController().getCountyByName(regionSelector.getMyCounty()));
				else 
					living.setCounty(new CountyController().getCountyByName(region.getMyCounty()));
				
				living.setTown(town.getModelObjectAsString());
				living.setNote(textArea.getValue());
				living.setAddress(address.getModelObjectAsString());


				// not-user entered data
				living.setActive(Boolean.valueOf(active.getModelObjectAsString()));


				// toto je pre tu hlupu aktivnost co ma byt 30 dni
				if (activeDateEnd>actualDate){
					java.sql.Date date = new java.sql.Date( new java.util.Date().getTime());
					living.setCreateDate(date);
				}
				
				LivingController livingController = new LivingController();

				if (livingController.updateLiving(living)){
					submit.setVisible(true);
					reactivate.setVisible(false);
					feedbackPanel.info("Inzerát úspešne aktualizovaný");
				}
				else{
					log.warn("Chyba pri vkladaní");
					feedbackPanel.warn("Chyba pri vkladaní");
				}
			} catch (ParseException e) {
				log.warn(e.getMessage());
			} catch (NumberFormatException e) {
				log.warn(e.getMessage());
			}
		}

	}

}
