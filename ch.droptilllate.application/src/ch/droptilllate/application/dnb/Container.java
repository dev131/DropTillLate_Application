package ch.droptilllate.application.dnb;

public class Container {

	private int id;
	private int shareFolderID;
	
	public Container(int id, int shareFolderID){
		this.id= id;
		this.shareFolderID= shareFolderID;
	}
	
	public int getID(){
		return id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public int getShareFolderID(){
		return shareFolderID;
	}
	
	public void setShareFolderID(int shareFolderID){
		this.shareFolderID = shareFolderID;
	}
}
