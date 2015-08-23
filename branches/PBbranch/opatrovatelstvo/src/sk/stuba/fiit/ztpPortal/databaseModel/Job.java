package sk.stuba.fiit.ztpPortal.databaseModel;

import java.io.Serializable;
import java.sql.Date;

public class Job implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	
//	region varchar(255),
//	town varchar(255),
//	position integer NOT NULL,
//	contract_type integer NOT NULL,
//	type_of_advert integer NOT NULL,
	
	private Date startDate;
	private String specification;
	private Integer hourPayment;
	private Integer workDuration;
	private RegisteredUser creator; 
	private boolean state;
	private Town town;
	private Date creationDate;
	private Date changeDate;
	private String urlLink;
	private JobSector sector;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public Integer getHourPayment() {
		return hourPayment;
	}
	public void setHourPayment(Integer hourPayment) {
		this.hourPayment = hourPayment;
	}
	public Integer getWorkDuration() {
		return workDuration;
	}
	public void setWorkDuration(Integer workDuration) {
		this.workDuration = workDuration;
	}
	public RegisteredUser getCreator() {
		return creator;
	}
	public void setCreator(RegisteredUser creator) {
		this.creator = creator;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
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
	public String getUrlLink() {
		return urlLink;
	}
	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}
	public Town getTown() {
		return town;
	}
	public void setTown(Town town) {
		this.town = town;
	}
	public JobSector getSector() {
		return sector;
	}
	public void setSector(JobSector sector) {
		this.sector = sector;
	}
	
		
}
