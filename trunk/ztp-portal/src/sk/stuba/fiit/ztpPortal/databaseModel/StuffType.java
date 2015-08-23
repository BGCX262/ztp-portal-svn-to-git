package sk.stuba.fiit.ztpPortal.databaseModel;

import java.io.Serializable;

public class StuffType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//pristup do databazy
	private long id;
	
	//atributy
	private String name;

	//get a set metody
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
}
