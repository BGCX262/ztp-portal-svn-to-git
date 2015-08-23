package sk.stuba.fiit.ztpPortal.core;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;

import sk.stuba.fiit.ztpPortal.server.Style;

public final class CoreSession extends WebSession {

	public CoreSession(Request request) {
		super(request);
	}
	
	private static final long serialVersionUID = 1L;
	private String loged;
	private String password;
	private long userId;
	
	private String userStyle;
	
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
	
	public static CoreSession get() {
		return (CoreSession) Session.get();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public synchronized String getUserStyle(){
		if (this.userStyle==null)
		return (new Style().getNormalSeasonStyle());	// FIXME: tymto sa zapnu styly podla mesiacov 
		else return (userStyle);//userStyle;
	}
	
	public synchronized void setUserStyle(String style){
		this.userStyle=style;
	}
	
	public boolean isLogged(){
		if (loged==null) return false;
		else return true;
	}
}
