package sk.stuba.fiit.ztpPortal.module.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sk.stuba.fiit.ztpPortal.admin.AdminPage;
import sk.stuba.fiit.ztpPortal.core.PageRenderer;
import sk.stuba.fiit.ztpPortal.core.CoreSession;
import sk.stuba.fiit.ztpPortal.core.UserProvider;
import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseController.JobSectorController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseController.TownController;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.yui.calendar.DateField;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.value.ValueMap;


import org.apache.wicket.model.IModel;


public class JobList extends AdminPage{
	
	private static final long serialVersionUID = 1L;
	
	String path;
	
	private List<Job> jobs;
	
	private PageRenderer pageRenderer;
	private static FeedbackPanel feedbackPanel;
	
	private RegisteredUserController userController;
	private String login;
	private RegisteredUser user;
	
	
	public JobList(){
		
		login = ((CoreSession) getSession()).getLoged();
		userController = new RegisteredUserController();

		if (login != null)
			user = userController.getRegisteredUserByLogin(login);
		else
			user = null;

		 feedbackPanel = new FeedbackPanel("feedback",new ContainerFeedbackMessageFilter(this));
	        add(feedbackPanel);
		
		pageRenderer = new PageRenderer();
		setPageHeaderPanel();
		setPageNavigationPanel();
		setPageLeftNavigation();
		
		DataTablePage();
		
	}
	
	private void setPageHeaderPanel() {
		pageRenderer.setUser(user);
		add(pageRenderer.getLoginForm());
		add(pageRenderer.getStatusForm());
	}

	private void setPageNavigationPanel() {
		add(pageRenderer.getJobListLink());
		add(pageRenderer.getHomePageLink());
	}
	
	//tu nastavim navigacne tlacitka vlavo v menu
	private void setPageLeftNavigation(){
		Link jobDetailLink = new Link("newJobLink") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new JobDetail());
			}
		};
		add(jobDetailLink);
	}
	
	private List<Job> getJobList(){
		JobController jobC = new JobController();
		return jobC.getAllJob();
	}

	public void DataTablePage() {
	        final UserProvider userProvider = new UserProvider();
	        
	        List<IColumn> columns = new ArrayList<IColumn>();
	        
	        columns.add(new PropertyColumn(new Model("D·tum vloûenia"), "creationDate", "creationDate"));
			columns.add(new PropertyColumn(new Model("Opis"), "specification", "specification"));
			columns.add(new PropertyColumn(new Model("Okres"), "town.name", "town.name"));
			columns.add(new PropertyColumn(new Model("Oblasù"), "sector.name", "sector.name"));
			
	        columns.add(new AbstractColumn(new Model("N·hæad"))
			{
				public void populateItem(Item cellItem, String componentId, IModel model)
				{
					cellItem.add(new ActionPanel(componentId, model));
				}
			});  
	        
	        DefaultDataTable table = new DefaultDataTable("datatable", columns, userProvider, 10);
	        
	        add(table);
	    }
	
	 class ActionPanel extends Panel
		{
			/**
			 * @param id
			 *            component id
			 * @param model
			 *            model for contact
			 */
			public ActionPanel(String id, IModel model)
			{
				super(id, model);
				add(new Link("Detail")
				{
					public void onClick()
					{
						setResponsePage(new JobViewDetail(((Job)getParent().getModelObject()).getId()));
					}
				});

//				SubmitLink removeLink = new SubmitLink("remove", form)
//				{
//					public void onSubmit()
//					{};
//				};
//				removeLink.setDefaultFormProcessing(false);
//				add(removeLink);
			}
		} 
	
}

