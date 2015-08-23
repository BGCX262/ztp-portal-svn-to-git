package sk.stuba.fiit.ztpPortal.databaseModel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
public class Comment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@DocumentId
	private long id;

	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String name;

	@IndexedEmbedded
	@ManyToMany 
	private RegisteredUser owner;

	@Field(index = Index.UN_TOKENIZED, store = Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	private Date createDate;
	
	@Field(index = Index.UN_TOKENIZED, store = Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	private Date changeDate;

	@Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private boolean state;
	
	@Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private boolean active;
	
	@IndexedEmbedded
	@ManyToMany
	private Thread thread;
	
	@IndexedEmbedded
	@ManyToMany
	private Event event;
	
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	private long notice;

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

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public long getNotice() {
		return notice;
	}

	public void setNotice(long notice) {
		this.notice = notice;
	}

}
