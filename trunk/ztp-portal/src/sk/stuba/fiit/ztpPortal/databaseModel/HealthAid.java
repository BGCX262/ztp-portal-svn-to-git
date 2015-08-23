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
public class HealthAid implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@DocumentId
	private long id;
	
	@Field(index = Index.TOKENIZED, store = Store.YES)
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

	private boolean state;
	
	private boolean active;
	
	@Field(index = Index.TOKENIZED, store = Store.YES)
	private String cmsContent;

	private HandicapType handicapType;

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

	public String getCmsContent() {
		return cmsContent;
	}

	public void setCmsContent(String cmsContent) {
		this.cmsContent = cmsContent;
	}

	public HandicapType getHandicapType() {
		return handicapType;
	}

	public void setHandicapType(HandicapType handicapType) {
		this.handicapType = handicapType;
	}
	
}
