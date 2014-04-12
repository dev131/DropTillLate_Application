package ch.droptilllate.application.info;

import java.util.List;

import ch.droptilllate.application.dnb.ShareRelation;


public class CRUDShareFolderInfo {

	private List<ShareRelation> shareFolderListError;
	private List<ShareRelation> shareFolderListSuccess;
	
	public List<ShareRelation> getShareFolderListError() {
		return this.shareFolderListError;
	}
	public void setShareFolderListError(List<ShareRelation> shareFolderListError) {
		this.shareFolderListError = shareFolderListError;
	}
	public List<ShareRelation> getShareFolderListSuccess() {
		return this.shareFolderListSuccess;
	}
	public void setShareFolderListSuccess(List<ShareRelation> shareFolderListSuccess) {
		this.shareFolderListSuccess = shareFolderListSuccess;
	}
	
}
