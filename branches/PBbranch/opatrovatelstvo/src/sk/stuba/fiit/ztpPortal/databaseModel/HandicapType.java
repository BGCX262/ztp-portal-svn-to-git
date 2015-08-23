package sk.stuba.fiit.ztpPortal.databaseModel;

public class HandicapType {

	// id do databazy
	private long id;
	
	//atributy
	private String type;
	private String note;
	
	//get a set metody
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}
