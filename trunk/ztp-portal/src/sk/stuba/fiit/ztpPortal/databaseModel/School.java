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
public class School implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@DocumentId
	private long id;
	
	private SchoolType schoolType;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String name;
	
	private County county;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String town;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String address;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String homePage;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String contactPerson;
	
	@Field(index=Index.UN_TOKENIZED, store=Store.YES)
	private String phone;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String note;
	
	private String email;
	private boolean state;
	private boolean active;
	private Date createDate;
	private Date changeDate;
	private RegisteredUser owner;
	
	
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
	public County getCounty() {
		return county;
	}
	public void setCounty(County county) {
		this.county = county;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHomePage() {
		return homePage;
	}
	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public SchoolType getSchoolType() {
		return schoolType;
	}
	public void setSchoolType(SchoolType schoolType) {
		this.schoolType = schoolType;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}	
}
