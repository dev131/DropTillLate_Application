package ch.droptilllate.application.info;

import java.util.List;

import ch.droptilllate.application.model.EncryptedFileDob;

public class CRUDCryptedFileInfo {
	private List<EncryptedFileDob> encryptedFileListError;
	private List<EncryptedFileDob> encryptedFileListSuccess;
	
	public List<EncryptedFileDob> getEncryptedFileListError() {
		return this.encryptedFileListError;
	}
	public void setEncryptedFileListError(
			List<EncryptedFileDob> encryptedFileListError) {
		this.encryptedFileListError = encryptedFileListError;
	}
	public List<EncryptedFileDob> getEncryptedFileListSuccess() {
		return this.encryptedFileListSuccess;
	}
	public void setEncryptedFileListSuccess(
			List<EncryptedFileDob> encryptedFileListSuccess) {
		this.encryptedFileListSuccess = encryptedFileListSuccess;
	}

}
