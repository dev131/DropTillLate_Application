package ch.droptilllate.application.dnb;

import ch.droptilllate.application.properties.Messages;

public class ShareFolder {


	private Integer id;
	private String key;
	
	/**
	 * ShareFolder
	 * @param id -> auto generate if it is null
	 * @param path
	 * @param key
	 */
	public ShareFolder(Integer id, String key){
		this.id= id;
		this.key= key;
	}
	
	public Integer getID(){
		return id;
	}
	
	public void setID(Integer id){
		this.id = id;
	}
	
	public String getKey(){
		return key;
	}
	
	public void setKey(String key){
		this.key = key;
	}
	
}
