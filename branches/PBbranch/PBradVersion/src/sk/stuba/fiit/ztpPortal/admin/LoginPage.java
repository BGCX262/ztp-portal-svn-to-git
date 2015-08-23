package sk.stuba.fiit.ztpPortal.admin;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.server.SessionFactoryHolder;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;


public class LoginPage extends AdminPage {

	private static final long serialVersionUID = 1L;
	
	private RegisteredUserController userController;

	public LoginPage() {
//		 String path = ((WebApplication)getApplication()).getWicketServlet().getServletContext().getRealPath("/");
		add(new LoginForm("loginForm"));
	}

//	public Session newSession(Request request, Response response) {
//	   return new WicketSession(request);
//	}
	
	public final class LoginForm extends Form {

		private static final long serialVersionUID = 1L;
		TextField login;
		TextField password;
		Button submit;
		ComponentFeedbackPanel loginFeedback;
		ComponentFeedbackPanel passwordFeedback;
		ComponentFeedbackPanel submitFeedback;

		private ValueMap properties = new ValueMap();

		public LoginForm(final String id) {
			super(id);
			
			login = new TextField("login", new PropertyModel(properties,
					"login"));
			login.setRequired(true);
			add(login);
			add(loginFeedback = new ComponentFeedbackPanel("loginFeedback",
					login));
			add(new Label("loginLabel", "Prihlasovacie meno:"));

			password = new PasswordTextField("password", new PropertyModel(
					properties, "password"));
			password.setRequired(true);
			add(password);
			add(passwordFeedback = new ComponentFeedbackPanel(
					"passwordFeedback", password));
			add(new Label("passwordLabel", "Heslo:"));

			add(submit = new Button("submit",
					new ResourceModel("button.submit")));
			add(submitFeedback = new ComponentFeedbackPanel("submitFeedback",
					submit));

		}

		private boolean authenticatePassword() {
			
			String inputPassword=password.getModelObjectAsString();
			String inputLogin=login.getModelObjectAsString();
			RegisteredUser user=null;
			boolean returnValue=false;
			
			SessionFactory sf = SessionFactoryHolder.getSF();
			Session session = sf.openSession();
			try{
				user = userController.getRegisteredUserByLogin(inputLogin);
				if ((user.getLogin()).equals(inputLogin) && (user.getPassword()).equals(inputPassword)) returnValue=true;
			}finally{
				session.close();
				sf.close();
			}
			return returnValue;
		}
	

		protected void onSubmit() {
			userController = new RegisteredUserController();
			if (authenticatePassword()){
				RegisteredUser user = userController.getRegisteredUserByLogin(login.getModelObjectAsString());
			
				((AdminSession) getSession()).setLoged(user.getLogin());
				
				System.out.println(")))) "+((AdminSession) getSession()).isTemporary());
				
	//			((WicketSession) getSession()).bind();
				
				System.out.println("authorizing " + user.getSurname());
				MainPage page = new MainPage();
			setResponsePage(page);
			
			userController =null;
			} 
		}
	}
}
