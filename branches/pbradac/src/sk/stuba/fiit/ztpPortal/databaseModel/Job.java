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
//	position integer NOT NULL,
	
	private AdvertType advertType; 
	private Date startDate;
	private String specification;
	private Double hourPayment;
	private Integer workDuration;
	private RegisteredUser creator; 
	private boolean state;	//ci je aktivna voci userom
	private boolean active; //ci je aktivna voci vlastnikovi
	private County county;
	private String town;
	private Date creationDate;
	private Date changeDate;
	private String urlLink;
	private JobSector jobSector;
	private JobContractType contract;
	private String cmsContent;
	
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
	public Double getHourPayment() {
		return hourPayment;
	}
	public void setHourPayment(Double hourPayment) {
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
	public JobSector getJobSector() {
		return jobSector;
	}
	public void setJobSector(JobSector sector) {
		this.jobSector = sector;
	}
	public JobContractType getContract() {
		return contract;
	}
	public void setContract(JobContractType contract) {
		this.contract = contract;
	}
	public AdvertType getAdvertType() {
		return advertType;
	}
	public void setAdvertType(AdvertType typeOfAdvert) {
		this.advertType = typeOfAdvert;
	}
	public String getCmsContent() {
		return cmsContent;
	}
	public void setCmsContent(String cmsContent) {
		this.cmsContent = cmsContent;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public County getCounty() {
		return county;
	}
	public void setCounty(County county) {
		this.county = county;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getTown() {
		return town;
	}	
	
}
