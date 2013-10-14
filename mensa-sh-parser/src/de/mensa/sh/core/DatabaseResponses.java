package de.mensa.sh.core;

public enum DatabaseResponses {
	
	OK("ok"),
	NOT_FOUND("nf"),
	ERROR("err"),
	SEPERATOR("|");
	
	public String value;
	
	private DatabaseResponses(String value) {
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
}
