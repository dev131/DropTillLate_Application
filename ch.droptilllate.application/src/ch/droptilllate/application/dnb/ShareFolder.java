package ch.droptilllate.application.dnb;

public class ShareFolder {


	private Integer id;
	private String key;
	private String path;
	
	/**
	 * ShareFolder
	 * @param id -> auto generate if it is null
	 * @param path
	 * @param key
	 */
	public ShareFolder(Integer id, String path, String key){
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
	
	public String getPath(){
		return path;
	}
	
	public void setPath(String path){
		this.path = path;
	}
}
