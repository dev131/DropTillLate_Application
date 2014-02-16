package ch.droptilllate.application.info;

import java.util.List;

import ch.droptilllate.application.dnb.ShareRelation;

public class CRUDShareRelationResult {

	private List<ShareRelation> ShareRelationListError;
	private List<ShareRelation> ShareRelationListSuccess;
	public List<ShareRelation> getShareRelationListError() {
		return ShareRelationListError;
	}
	public void setShareRelationListError(List<ShareRelation> shareRelationListError) {
		ShareRelationListError = shareRelationListError;
	}
	public List<ShareRelation> getShareRelationListSuccess() {
		return ShareRelationListSuccess;
	}
	public void setShareRelationListSuccess(
			List<ShareRelation> shareRelationListSuccess) {
		ShareRelationListSuccess = shareRelationListSuccess;
	}
	
	
	
}
