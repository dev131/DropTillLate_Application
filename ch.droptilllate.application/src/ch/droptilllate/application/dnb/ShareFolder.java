package ch.droptilllate.application.dnb;

public class ShareFolder {


	private int id;
	private String key;
	private String path;
	
	public ShareFolder(int id, String path, String key){
		this.id= id;
		this.key= key;
	}
	
	public int getID(){
		return id;
	}
	
	public void setID(int id){
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
