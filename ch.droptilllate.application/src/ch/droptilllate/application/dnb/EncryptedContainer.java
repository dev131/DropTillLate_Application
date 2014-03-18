package ch.droptilllate.application.dnb;

public class EncryptedContainer {

	private Integer id;
	private Integer shareFolderId;
	
	/**
	 * EncryptedContainer
	 * @param id -> auto generate if it is null
	 * @param shareFolderId
	 */
	public EncryptedContainer(Integer id, Integer shareFolderId){
		this.id= id;
		this.shareFolderId= shareFolderId;
	}
	
	public Integer getId(){
		return this.id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getShareFolderId(){
		return this.shareFolderId;
	}
	
	public void setContainerID(Integer shareFolderId){
		this.shareFolderId = shareFolderId;
	}
}
