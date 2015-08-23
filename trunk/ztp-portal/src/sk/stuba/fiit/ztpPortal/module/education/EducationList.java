package sk.stuba.fiit.ztpPortal.module.education;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.HeaderPanel;
import sk.stuba.fiit.ztpPortal.databaseController.CmsContentController;
import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseController.CourseController;
import sk.stuba.fiit.ztpPortal.databaseController.CourseTypeController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.SchoolController;
import sk.stuba.fiit.ztpPortal.databaseController.SchoolTypeController;
import sk.stuba.fiit.ztpPortal.databaseModel.CmsContent;
import sk.stuba.fiit.ztpPortal.databaseModel.Course;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.School;

public class EducationList extends CorePage{

	private static CoreFeedBackPanel feedbackPanel;

	private RegisteredUserController userController;
	private SchoolController schoolController;
	private CourseController courseController;
	private String login;
	private RegisteredUser user;
	private long userID;

	private CmsContentController cmsControler;
	private CmsContent pageContent;
	
	private final DefaultDataTable schoolTable;
	private final DefaultDataTable courseTable;
	
	private final SchoolProvider schoolProvider;
	private final CourseProvider courseProvider;
	
	public EducationList(){
		// nastavim CSS
		add(HeaderContributor.forCss(((CoreSession) getSession()).getUserStyle()));
		login = ((CoreSession) getSession()).getLoged();
		userID = ((CoreSession) getSession()).getUserId();
		userController = new RegisteredUserController();
		cmsControler = new CmsContentController();
		
		schoolProvider = new SchoolProvider();
		
		courseProvider = new CourseProvider(); 
		
		if (login != null){
			user = userController.getRegisteredUserByLogin(login);
			schoolProvider.setUserPreferTown(user.isPreferRegion());
			schoolProvider.setUserPreferredTownFilter(user.getCounty());
			schoolProvider.setSchoolOwner(user);
			
			courseProvider.setUserPreferTown(user.isPreferRegion());
			courseProvider.setUserPreferredTownFilter(user.getCounty());
			courseProvider.setCourseOwner(user);
			
		}
		else
			user = null;

		feedbackPanel = new CoreFeedBackPanel("feedback");
		add(feedbackPanel);
		
		setPageHeaderPanel();
		setPageLeftNavigation();
		
		pageContent = cmsControler.getContentByName("education");

		add(new Label("cmsContent", pageContent.getContent())
				.setEscapeModelStrings(false));
		
////robim tabulku + filtre skola
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
	
	private void setPageHeaderPanel() {
		HeaderPanel panel = new HeaderPanel("headerPanel",user);
		add(panel);
		
	}

	private void setPageLeftNavigation() {
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

			@Override
            public Component getFilter(final String varComponentId, final FilterForm varForm) {
                TextFilter filter = (TextFilter) super.getFilter(varComponentId, varForm);
                filter.getFilter().add(new AttributeModifier("size", true, new Model("10")));
                return filter;
            } 
			
		});
		
		columns.add(new TextFilteredPropertyColumn(new Model("Vytvoril"),
				"owner.login", "owner.login") {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass() {
				return "owner";
			}
			
			@Override
            public Component getFilter(final String varComponentId, final FilterForm varForm) {
                TextFilter filter = (TextFilter) super.getFilter(varComponentId, varForm);
                filter.getFilter().add(new AttributeModifier("size", true, new Model("7")));
                return filter;
            } 

		});
		
		columns.add(new AbstractColumn(new Model("")) {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			// add the UserActionsPanel to the cell item
			public void populateItem(Item cellItem, String componentId,
					IModel model) {
				cellItem.add(new SchoolDeletePanel(componentId, model));
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
				cellItem.add(new SchoolActionPanel(componentId, model));
			}
		});

		// columns.add(new AbstractColumn(new Model("N·hæad"))
		// {
		// public void populateItem(Item cellItem, String componentId, IModel
		// model)
		// {
		// cellItem.add(new ActionPanel(componentId, model));
		// }
		//				
		// @Override
		// public String getCssClass()
		// {
		// return "seeDetail";
		// }
		//				
		// });

		return columns;
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
	public SchoolActionPanel(String id, IModel model) {
		super(id, model);

		schoolController = new SchoolController();

		
		
		add(new Link("Detail") {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				long schoolId = (((School) getParent().getModelObject()).getId());
				if (login != null) {
					if (schoolController.isSchoolCreator(schoolId, userID))
						//TODO
						setResponsePage(new SchoolViewDetail(schoolId));
					else
						setResponsePage(new SchoolView(schoolId));
				} else
					setResponsePage(new SchoolView(schoolId));
			}
		});


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

	columns.add(new TextFilteredPropertyColumn(new Model("N·zov"),
			"name", "name") {

		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		@Override
		public String getCssClass() {
			return "name";
		}
		
		@Override
        public Component getFilter(final String varComponentId, final FilterForm varForm) {
            TextFilter filter = (TextFilter) super.getFilter(varComponentId, varForm);
            filter.getFilter().add(new AttributeModifier("size", true, new Model("9")));
            return filter;
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
		
		@Override
        public Component getFilter(final String varComponentId, final FilterForm varForm) {
            TextFilter filter = (TextFilter) super.getFilter(varComponentId, varForm);
            filter.getFilter().add(new AttributeModifier("size", true, new Model("3")));
            return filter;
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
		
		@Override
        public Component getFilter(final String varComponentId, final FilterForm varForm) {
            TextFilter filter = (TextFilter) super.getFilter(varComponentId, varForm);
            filter.getFilter().add(new AttributeModifier("size", true, new Model("7")));
            return filter;
        } 

	});
	
	columns.add(new AbstractColumn(new Model("")) {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		// add the UserActionsPanel to the cell item
		public void populateItem(Item cellItem, String componentId,
				IModel model) {
			cellItem.add(new CourseDeletePanel(componentId, model));
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
			cellItem.add(new CourseActionPanel(componentId, model));
		}
	});

	// columns.add(new AbstractColumn(new Model("N·hæad"))
	// {
	// public void populateItem(Item cellItem, String componentId, IModel
	// model)
	// {
	// cellItem.add(new ActionPanel(componentId, model));
	// }
	//				
	// @Override
	// public String getCssClass()
	// {
	// return "seeDetail";
	// }
	//				
	// });

	return columns;
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
public CourseActionPanel(String id, IModel model) {
	super(id, model);

	courseController = new CourseController();

	
	
	add(new Link("Detail") {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		public void onClick() {
			long courseId = (((Course) getParent().getModelObject()).getId());
			if (login != null) {
				if (courseController.isCourseCreator(courseId, userID))
					setResponsePage(new CourseViewDetail(courseId));
				else
					setResponsePage(new CourseView(courseId));
			} else
				setResponsePage(new CourseView(courseId));
		}
	});


}
}

class SchoolDeletePanel extends Panel {
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
	public SchoolDeletePanel(String id, final IModel model) {
		super(id, model);

		schoolController = new SchoolController();

		Link deleteLink =new Link("Delete") {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			 @Override
			  public void onClick() {
				 ((School)model.getObject()).setActive(false);
				 schoolController.updateSchool((School) model.getObject());
			  }
			};	
		
		deleteLink.add(new SimpleAttributeModifier("onclick", "return confirm('Chcete odstr·niù inzer·t?');"));
		
		add(deleteLink);
		
		if (!((School)model.getObject()).getOwner().getLogin().equals(login) || !((School)model.getObject()).isActive()){
			deleteLink.setVisible(false);
		}
	}
}

class CourseDeletePanel extends Panel {
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
	public CourseDeletePanel(String id, final IModel model) {
		super(id, model);

		courseController = new CourseController();

		Link deleteLink =new Link("Delete") {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			 @Override
			  public void onClick() {
				 ((Course)model.getObject()).setActive(false);
				 courseController.updateCourse((Course) model.getObject());
			  }
			};	
		
		deleteLink.add(new SimpleAttributeModifier("onclick", "return confirm('Chcete odstr·niù inzer·t?');"));
		
		add(deleteLink);
		
		if (!((Course)model.getObject()).getOwner().getLogin().equals(login) || !((Course)model.getObject()).isActive()){
			deleteLink.setVisible(false);
		}
	}
}

}
