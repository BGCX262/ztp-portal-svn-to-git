package sk.stuba.fiit.ztpPortal.databaseModel;

public class UserEvent {

	private long id;
	private RegisteredUser user;
	private Event event;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public RegisteredUser getUser() {
		return user;
	}
	public void setUser(RegisteredUser user) {
		this.user = user;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	
	
}
