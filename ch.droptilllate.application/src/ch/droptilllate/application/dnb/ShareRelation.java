package ch.droptilllate.application.dnb;

public class ShareRelation {


	private Integer id;
	private String key;
	
	/**
	 * ShareFolder
	 * @param id -> auto generate if it null by database insert
	 * @param path
	 * @param key
	 */
	public ShareRelation(Integer id, String key){
		this.id= id;
		this.key= key;
	}
	
	public Integer getID(){
		return this.id;
	}
	
	public void setID(Integer id){
		this.id = id;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public void setKey(String key){
		this.key = key;
	}
	
}
