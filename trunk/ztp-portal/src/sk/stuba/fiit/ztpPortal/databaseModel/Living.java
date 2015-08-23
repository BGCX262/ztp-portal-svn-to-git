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
public class Living implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@DocumentId
	private long id;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String name;
	
	private RegisteredUser owner;
	private County county;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String address;
	
	@Field(index=Index.TOKENIZED, store=Store.YES)
	private String town;
	
	private Date livingDate;
	private Integer roomCount;
	
	@Field(index=Index.UN_TOKENIZED, store=Store.YES)
	private Integer price;
	
	@Field(index=Index.UN_TOKENIZED, store=Store.YES)
	private Integer size;
	
	private boolean state;
	private boolean active;
	private Date createDate;
	private Date changeDate;
	private String note;
	private LivingType livingType;
	private StuffType stuffType;
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
	public County getCounty() {
		return county;
	}
	public void setCounty(County county) {
		this.county = county;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public Date getLivingDate() {
		return livingDate;
	}
	public void setLivingDate(Date livingDate) {
		this.livingDate = livingDate;
	}
	public Integer getRoomCount() {
		return roomCount;
	}
	public void setRoomCount(Integer roomCount) {
		this.roomCount = roomCount;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
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
	public LivingType getLivingType() {
		return livingType;
	}
	public void setLivingType(LivingType livingType) {
		this.livingType = livingType;
	}
	public StuffType getStuffType() {
		return stuffType;
	}
	public void setStuffType(StuffType stuffType) {
		this.stuffType = stuffType;
	}
	
	
}
