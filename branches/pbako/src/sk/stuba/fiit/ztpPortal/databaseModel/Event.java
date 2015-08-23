package sk.stuba.fiit.ztpPortal.databaseModel;

import java.io.Serializable;
import java.sql.Date;

public class Event implements Serializable{

	private static final long serialVersionUID = 1L;

//TODO	region integer NULL, 
	
	// pristup do databazy
	private long id;
	
	private String name;
	private RegisteredUser owner;
	private County town;
	private String address;
	private Date startDate;
	private Date endDate;
	private long lenght;
	private int guestCount;
	private boolean state;
	private boolean active;
	private Date createDate;
	private Date changeDate;
	private String note;
	private EventType type;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public RegisteredUser getOwner() {
		return owner;
	}
	public void setOwner(RegisteredUser owner) {
		this.owner = owner;
	}
	public County getTown() {
		return town;
	}
	public void setTown(County town) {
		this.town = town;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public long getLenght() {
		return lenght;
	}
	public void setLenght(long lenght) {
		this.lenght = lenght;
	}
	public int getGuestCount() {
		return guestCount;
	}
	public void setGuestCount(int guestCount) {
		this.guestCount = guestCount;
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	
}
