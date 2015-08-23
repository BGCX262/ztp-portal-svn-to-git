package sk.stuba.fiit.ztpPortal.admin.education;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import sk.stuba.fiit.ztpPortal.admin.AdminNavigation;
import sk.stuba.fiit.ztpPortal.admin.AdminPage;
import sk.stuba.fiit.ztpPortal.admin.AdminSession;
import sk.stuba.fiit.ztpPortal.admin.NavigationPanel;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.CourseController;
import sk.stuba.fiit.ztpPortal.databaseController.CourseTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.SchoolController;
import sk.stuba.fiit.ztpPortal.databaseController.SchoolTypeController;
import sk.stuba.fiit.ztpPortal.databaseModel.Course;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.School;
import sk.stuba.fiit.ztpPortal.module.education.CourseView;
import sk.stuba.fiit.ztpPortal.module.education.SchoolView;

public class EducationList extends AdminPage{

	private SchoolController schoolController;
	private CourseController courseController;
	private String login;
	private RegisteredUser user;
	private long userID;
	
	private final DefaultDataTable schoolTable;
	private final DefaultDataTable courseTable;
	
	private final SchoolProvider schoolProvider = new SchoolProvider();
	private final CourseProvider courseProvider = new CourseProvider();
	
	public EducationList(){
		login = ((AdminSession) getSession()).getLoged();

		Label loginNameInfoLabel = new Label("profileNameInfo",
				"Ste prihl·sen˝ ako " + login);
		add(loginNameInfoLabel);

		setNavigation();

		// //robim tabulku + filtre
		
		final FilterForm schoolForm = new FilterForm("school-filter-form", schoolProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (user!=null)
					schoolProvider.setUserPreferTown(false);
				schoolTable.setCurrentPage(0);
			}

		};
				
		schoolTable = new DefaultDataTable("schoolDatatable", createSchoolColumns(), schoolProvider,
				10);

		schoolTable.addTopToolbar(new FilterToolbar(schoolTable, schoolForm, schoolProvider));
		schoolForm.add(schoolTable);
		add(schoolForm);
		
		
	////robim tabulku + filtre kurzy
		
		final FilterForm courseForm = new FilterForm("course-filter-form", courseProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (user!=null)
					courseProvider.setUserPreferTown(false);
				courseTable.setCurrentPage(0);
			}

		};
				
		courseTable = new DefaultDataTable("courseDatatable", createCourseColumns(), courseProvider,
				10);

		courseTable.addTopToolbar(new FilterToolbar(courseTable, courseForm, courseProvider));
		courseForm.add(courseTable);
		add(courseForm);
		
	}
	
	private void setNavigation() {
		AdminNavigation adminNavigation = new AdminNavigation();

		add(adminNavigation.getUserLogOutLink());

		add(new NavigationPanel("panel"));

	}
	
	
	
private List<IColumn> createSchoolColumns() {
		
		final List<String> COUNTY_LIST = Arrays
		.asList(new CountyController().getCountyNameList());

		final List<String> TYPE_LIST = Arrays
		.asList(new SchoolTypeController().getSchoolTypeNameList());
		
		List<IColumn> columns = new ArrayList<IColumn>();

		columns.add(new PropertyColumn(new Model("D·tum"),
				"createDate", "createDate") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "createDate";
			}

		});
		
		columns.add(new ChoiceFilteredPropertyColumn(new Model("Typ ökoly"),
				"schoolType.name", new LoadableDetachableModel() {
					/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					protected List<String> load() {
						List<String> uniqueLastNames = TYPE_LIST;
						return uniqueLastNames;
					}
				}) {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "town";
			}

		});

		columns.add(new ChoiceFilteredPropertyColumn(new Model("Okres"),
				"county.name", new LoadableDetachableModel() {
					/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					protected List<String> load() {
						List<String> uniqueLastNames = COUNTY_LIST;
						return uniqueLastNames;
					}
				}) {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "county";
			}

		});
		
		columns.add(new TextFilteredPropertyColumn(new Model("Mesto"),
				"town", "town") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "town";
			}

		});
		
		columns.add(new PropertyColumn(new Model("AktÌvne"), "state", "state") {

			public void populateItem(Item cellItem, String componentId,
					IModel model) {

				School selectedSchool = ((School) model.getObject());

				cellItem.add(new SchoolActivePanel(componentId, selectedSchool));
			}

		});

		columns.add(new PropertyColumn(new Model(">>"), "detail") {

			public void populateItem(Item cellItem, String componentId,
					IModel model) {

				cellItem.add(new SchoolDetailPanel(componentId, model));
			}
		});
		
		columns.add(new FilteredAbstractColumn(new Model("")) {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			// return the go-and-clear filter for the filter toolbar
			public Component getFilter(String componentId, FilterForm form) {

				return new GoAndClearFilter(componentId, form,
						new ResourceModel("filter"), new ResourceModel("clear")){
					
					
				};

			}

			// add the UserActionsPanel to the cell item
			public void populateItem(Item cellItem, String componentId,
					IModel model) {
				
				School selectedSchool =(School) model.getObject();
				cellItem.add(new SchoolActionPanel(componentId, selectedSchool));
			}
		});

		return columns;
	}

class SchoolActivePanel extends Panel {
	private static final long serialVersionUID = 1L;

	public SchoolActivePanel(String id, School school) {
		super(id);

		Label aktivny = new Label("aktivny", "aktÌvny");

		Label neaktivny = new Label("neaktivny", "neaktÌvny");

		add(aktivny);
		add(neaktivny);

		if (school.isState())
			neaktivny.setVisible(false);
		else
			aktivny.setVisible(false);

	}

}

class SchoolDetailPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public SchoolDetailPanel(String id, IModel model) {
		super(id, model);

		Link schoolViewLink = new Link("SchoolViewLink") {

			private static final long serialVersionUID = 1L;

			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("target", "_blank");
			}

			public void onClick() {
				School selectedSchool = ((School) getParent().getModelObject());
				setResponsePage(new SchoolView(selectedSchool.getId()));
			}

		};

		add(schoolViewLink);
	}
}



class SchoolActionPanel extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 *            component id
	 * @param model
	 *            model for contact
	 */
	public SchoolActionPanel(String id, final School school) {
		super(id);

		schoolController = new SchoolController();

		Link deactivationLink = new Link("Deactivation") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				School selectedSchool = school;// ((Job)
											// getParent().getModelObject());
				selectedSchool.setState(false);
				schoolController.updateSchool(selectedSchool);
			}
		};

		deactivationLink.add(new SimpleAttributeModifier("onclick",
				"return confirm('éel·te si deaktivovaù inzer·t ?');"));

		// if (((Job) getParent().getModelObject()).isState())
		deactivationLink.setAutoEnable(false);
		add(deactivationLink);

		Link activationLink = new Link("Activation") {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				School selectedSchool = school;
				selectedSchool.setState(true);
				schoolController.updateSchool(selectedSchool);
			}
		};

		activationLink.add(new SimpleAttributeModifier("onclick",
				"return confirm('éel·te si aktivovaù inzer·t ?');"));

		add(activationLink);

		activationLink.setVisible(true);
		deactivationLink.setVisible(true);

		if (school.isState())
			activationLink.setVisible(false);
		else
			deactivationLink.setVisible(false);
	}
}




private List<IColumn> createCourseColumns() {
	
	final List<String> COUNTY_LIST = Arrays
	.asList(new CountyController().getCountyNameList());

	final List<String> TYPE_LIST = Arrays
	.asList(new CourseTypeController().getCourseTypeNameList());
	
	List<IColumn> columns = new ArrayList<IColumn>();

	columns.add(new PropertyColumn(new Model("D·tum"),
			"createDate", "createDate") {

		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		@Override
		public String getCssClass() {
			return "createDate";
		}

	});
	
	columns.add(new ChoiceFilteredPropertyColumn(new Model("Typ kurzu"),
			"courseType.name", new LoadableDetachableModel() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				protected List<String> load() {
					List<String> uniqueLastNames = TYPE_LIST;
					return uniqueLastNames;
				}
			}) {

		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		@Override
		public String getCssClass() {
			return "couseType";
		}

	});
	
	columns.add(new TextFilteredPropertyColumn(new Model("Cena"),
			"price", "price") {

		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		@Override
		public String getCssClass() {
			return "price";
		}

	});
	
	columns.add(new ChoiceFilteredPropertyColumn(new Model("Okres"),
			"county.name", new LoadableDetachableModel() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				protected List<String> load() {
					List<String> uniqueLastNames = COUNTY_LIST;
					return uniqueLastNames;
				}
			}) {

		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		@Override
		public String getCssClass() {
			return "county";
		}

	});
	
	columns.add(new TextFilteredPropertyColumn(new Model("Mesto"),
			"town", "town") {

		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		@Override
		public String getCssClass() {
			return "town";
		}

	});
	
	columns.add(new PropertyColumn(new Model("AktÌvne"), "state", "state") {

		public void populateItem(Item cellItem, String componentId,
				IModel model) {

			Course selectedCourse = ((Course) model.getObject());

			cellItem.add(new CourseActivePanel(componentId, selectedCourse));
		}

	});

	columns.add(new PropertyColumn(new Model(">>"), "detail", "detail") {

		public void populateItem(Item cellItem, String componentId,
				IModel model) {

			cellItem.add(new CourseDetailPanel(componentId, model));
		}
	});
	
	columns.add(new FilteredAbstractColumn(new Model("")) {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		// return the go-and-clear filter for the filter toolbar
		public Component getFilter(String componentId, FilterForm form) {
			
			return new GoAndClearFilter(componentId, form,
					new ResourceModel("filter"), new ResourceModel("clear")){
				
				
			};

		}

		// add the UserActionsPanel to the cell item
		public void populateItem(Item cellItem, String componentId,
				IModel model) {
			
			Course selectedCourse = ((Course) model.getObject());
			cellItem.add(new CourseActionPanel(componentId, selectedCourse));
		}
	});

	return columns;
}

class CourseActivePanel extends Panel {
	private static final long serialVersionUID = 1L;

	public CourseActivePanel(String id, Course course) {
		super(id);

		Label aktivny = new Label("aktivny", "aktÌvny");

		Label neaktivny = new Label("neaktivny", "neaktÌvny");

		add(aktivny);
		add(neaktivny);

		if (course.isState())
			neaktivny.setVisible(false);
		else
			aktivny.setVisible(false);

	}

}

class CourseDetailPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public CourseDetailPanel(String id, IModel model) {
		super(id, model);

		Link courseViewLink = new Link("CourseViewLink") {

			private static final long serialVersionUID = 1L;

			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("target", "_blank");
			}

			public void onClick() {
				Course selectedCourse = ((Course) getParent().getModelObject());
				setResponsePage(new CourseView(selectedCourse.getId()));
			}

		};

		add(courseViewLink);
	}
}

class CourseActionPanel extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 *            component id
	 * @param model
	 *            model for contact
	 */
	public CourseActionPanel(String id, final Course course) {
		super(id);

		courseController = new CourseController();

		Link deactivationLink = new Link("Deactivation") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				Course selectedCourse = course;// ((Job)
											// getParent().getModelObject());
				selectedCourse.setState(false);
				courseController.updateCourse(selectedCourse);
			}
		};

		deactivationLink.add(new SimpleAttributeModifier("onclick",
				"return confirm('éel·te si deaktivovaù inzer·t ?');"));

		// if (((Job) getParent().getModelObject()).isState())
		deactivationLink.setAutoEnable(false);
		add(deactivationLink);

		Link activationLink = new Link("Activation") {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				Course selectedCourse = course;
				selectedCourse.setState(true);
				courseController.updateCourse(selectedCourse);
			}
		};

		activationLink.add(new SimpleAttributeModifier("onclick",
				"return confirm('éel·te si aktivovaù inzer·t ?');"));

		add(activationLink);

		activationLink.setVisible(true);
		deactivationLink.setVisible(true);

		if (course.isState())
			activationLink.setVisible(false);
		else
			deactivationLink.setVisible(false);
	}
}

	
}
