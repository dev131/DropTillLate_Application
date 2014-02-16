package ch.droptilllate.application.info;

import java.util.ArrayList;
import java.util.List;

import ch.droptilllate.application.dnb.ShareRelation;

public class ShareRelationInfo extends ShareRelation{
	
	private List<ShareRelationInfo> shares;
	private Integer fileId;
	private Integer containerId;
		
	public ShareRelationInfo(Integer sharefolderId, String mail) {
		super(sharefolderId, mail);
		this.shares = new ArrayList<ShareRelationInfo>();
	}

	public List<ShareRelationInfo> getShares() {
		return shares;
	}

	public void setShares(List<ShareRelationInfo> shares) {
		this.shares = shares;
	}

	public void addShares(List<ShareRelationInfo> shares) {
		for (ShareRelationInfo info : shares) {
			this.shares.add(info);
		}
	}
	
	public void removeShares(ShareRelationInfo info) {
		this.shares.remove(info);
	}

	

}
