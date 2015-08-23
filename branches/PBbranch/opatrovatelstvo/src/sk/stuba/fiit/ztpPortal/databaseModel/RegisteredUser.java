package sk.stuba.fiit.ztpPortal.databaseModel;

import java.io.Serializable;
import java.sql.Date;

public class RegisteredUser implements Serializable{

	// id do databázy
	private long id;
	
	// atribúty
	private String name;
	private String surname;
	private String login;
	private String password;
	private String email;
	private String region;
	private String town;
	private String handicapType;
	private boolean admin;
	private boolean state;
	private Date changeDate;
	private Date registrationDate;	//datum registracie
	private boolean preferRegion; //preferencia regionu
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
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getHandicapType() {
		return handicapType;
	}
	public void setHandicapType(String handicapType) {
		this.handicapType = handicapType;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	public boolean isPreferRegion() {
		return preferRegion;
	}
	public void setPreferRegion(boolean preferRegion) {
		this.preferRegion = preferRegion;
	}
	
	//get a set metody
	
	
}
