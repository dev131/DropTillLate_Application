package ch.droptilllate.application.dnb;

public class ShareMember {

	private Integer shareRelationId;
	private String mail;
	

	
	public ShareMember(Integer shareRelationId, String mail) {
		this.shareRelationId = shareRelationId;
		this.mail = mail;
	}


	public Integer getShareRelationId() {
		return shareRelationId;
	}


	public void setShareRelationId(Integer shareRelationId) {
		this.shareRelationId = shareRelationId;
	}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}

	
}
