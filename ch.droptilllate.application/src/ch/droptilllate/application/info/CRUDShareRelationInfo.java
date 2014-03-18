package ch.droptilllate.application.info;

import java.util.List;

import ch.droptilllate.application.dnb.ShareRelation;

public class CRUDShareRelationInfo {

	private List<ShareRelation> shareRelationListError;
	private List<ShareRelation> shareRelationListSuccess;
	public List<ShareRelation> getShareRelationListError() {
		return this.shareRelationListError;
	}
	public void setShareRelationListError(List<ShareRelation> shareRelationListError) {
		this.shareRelationListError = shareRelationListError;
	}
	public List<ShareRelation> getShareRelationListSuccess() {
		return this.shareRelationListSuccess;
	}
	public void setShareRelationListSuccess(
			List<ShareRelation> shareRelationListSuccess) {
		this.shareRelationListSuccess = shareRelationListSuccess;
	}
	
	
	
}
