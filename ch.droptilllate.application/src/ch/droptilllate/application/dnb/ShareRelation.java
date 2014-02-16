package ch.droptilllate.application.dnb;

public class ShareRelation {

	private Integer sharefolderId;
	private String mail;
	

	
	public ShareRelation(Integer sharefolderId, String mail) {
		this.sharefolderId = sharefolderId;
		this.mail = mail;
	}
	public Integer getSharefolderId() {
		return sharefolderId;
	}
	public void setSharefolderId(Integer sharefolderId) {
		this.sharefolderId = sharefolderId;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	
}
