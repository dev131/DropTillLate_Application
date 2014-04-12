package ch.droptilllate.application.dnb;

public class EncryptedContainer {

	private Integer id;
	private Integer shareRelationId;
	
	/**
	 * EncryptedContainer
	 * @param id -> auto generate if it is null
	 * @param shareFolderId
	 */
	public EncryptedContainer(Integer id, Integer shareRelationId){
		this.id= id;
		this.shareRelationId= shareRelationId;
	}
	
	public Integer getId(){
		return this.id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getShareRelationId(){
		return this.shareRelationId;
	}
	
	public void setShareRelationId(Integer shareRelationId){
		this.shareRelationId = shareRelationId;
	}
}
