package sk.stuba.fiit.ztpPortal.databaseModel;

import java.io.File;
import java.io.Serializable;
import java.sql.Date;

public class Picture implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//pristup do databazy
	private long id;
	
	private Date createDate;
	private Date changeDate;
	private boolean state;
	private boolean active;
	private String link;
	private byte[] image;
	private RegisteredUser owner;
	private Event event;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	public RegisteredUser getOwner() {
		return owner;
	}
	public void setOwner(RegisteredUser owner) {
		this.owner = owner;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	
	
	
}
