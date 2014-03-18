package ch.droptilllate.application.info;

import java.util.List;

import ch.droptilllate.application.dnb.ShareFolder;


public class CRUDShareFolderInfo {

	private List<ShareFolder> shareFolderListError;
	private List<ShareFolder> shareFolderListSuccess;
	
	public List<ShareFolder> getShareFolderListError() {
		return this.shareFolderListError;
	}
	public void setShareFolderListError(List<ShareFolder> shareFolderListError) {
		this.shareFolderListError = shareFolderListError;
	}
	public List<ShareFolder> getShareFolderListSuccess() {
		return this.shareFolderListSuccess;
	}
	public void setShareFolderListSuccess(List<ShareFolder> shareFolderListSuccess) {
		this.shareFolderListSuccess = shareFolderListSuccess;
	}
	
}
