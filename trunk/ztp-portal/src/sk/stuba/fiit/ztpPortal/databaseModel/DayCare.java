package sk.stuba.fiit.ztpPortal.databaseModel;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
public class DayCare implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@DocumentId
	private long id;
	
	private AdvertType advertType; 
	private HandicapType handicapType;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String town;
	
	private County county;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String description;
	
	private Date creationDate;
	private Date changeDate;
	private RegisteredUser creator;
	private String urlLink;
	private boolean state;	//ci je aktivna voci userom
	private boolean active; //ci je aktivna voci vlastnikovi
	private Date startDate;
	private Date endDate;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String shortDesc;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public AdvertType getAdvertType() {
		return advertType;
	}
	public void setAdvertType(AdvertType advertType) {
		this.advertType = advertType;
	}
	public HandicapType getHandicapType() {
		return handicapType;
	}
	public void setHandicapType(HandicapType handicapType) {
		this.handicapType = handicapType;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public County getCounty() {
		return county;
	}
	public void setCounty(County county) {
		this.county = county;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	public RegisteredUser getCreator() {
		return creator;
	}
	public void setCreator(RegisteredUser creator) {
		this.creator = creator;
	}
	public String getUrlLink() {
		return urlLink;
	}
	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
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
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}	

}
