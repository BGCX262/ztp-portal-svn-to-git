package sk.stuba.fiit.ztpPortal.databaseModel;

import java.io.Serializable;

public class GlobalSetting implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//pristup do databazy
	private long id;
	
	//atributy
	private String name;
	
	private String value;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
