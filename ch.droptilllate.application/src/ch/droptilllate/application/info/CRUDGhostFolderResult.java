package ch.droptilllate.application.info;

import java.util.List;

import ch.droptilllate.application.model.GhostFolderDob;

public class CRUDGhostFolderResult {
	private List<GhostFolderDob> encryptedFolderListError;
	private List<GhostFolderDob> encryptedFolderListSuccess;
	
	public List<GhostFolderDob> getEncryptedFolderListError() {
		return encryptedFolderListError;
	}
	public void setEncryptedFolderListError(
			List<GhostFolderDob> encryptedFolderListError) {
		this.encryptedFolderListError = encryptedFolderListError;
	}
	public List<GhostFolderDob> getEncryptedFolderListSuccess() {
		return encryptedFolderListSuccess;
	}
	public void setEncryptedFolderListSuccess(
			List<GhostFolderDob> encryptedFolderListSuccess) {
		this.encryptedFolderListSuccess = encryptedFolderListSuccess;
	}
}
