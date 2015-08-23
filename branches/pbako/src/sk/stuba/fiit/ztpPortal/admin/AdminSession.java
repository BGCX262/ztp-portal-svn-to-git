package sk.stuba.fiit.ztpPortal.admin;


import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;

public final class AdminSession extends WebSession {

	public AdminSession(Request request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	private String loged;
	private String password;

	public synchronized String getLoged() {
		return loged;
	}

	public synchronized void setLoged(String loged) {
		this.loged = loged;
	}

	public synchronized String getPassword() {
		return password;
	}

	public synchronized void setPassword(String password) {
		this.password = password;
	}

	public static AdminSession get() {
		return (AdminSession) Session.get();
	}
	
}
