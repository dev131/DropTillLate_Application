package ch.droptilllate.application.info;

import java.util.List;

import ch.droptilllate.application.dnb.ShareMember;

public class CRUDShareRelationInfo {

	private List<ShareMember> shareRelationListError;
	private List<ShareMember> shareRelationListSuccess;
	public List<ShareMember> getShareRelationListError() {
		return this.shareRelationListError;
	}
	public void setShareRelationListError(List<ShareMember> shareRelationListError) {
		this.shareRelationListError = shareRelationListError;
	}
	public List<ShareMember> getShareRelationListSuccess() {
		return this.shareRelationListSuccess;
	}
	public void setShareRelationListSuccess(
			List<ShareMember> shareRelationListSuccess) {
		this.shareRelationListSuccess = shareRelationListSuccess;
	}
	
	
	
}
