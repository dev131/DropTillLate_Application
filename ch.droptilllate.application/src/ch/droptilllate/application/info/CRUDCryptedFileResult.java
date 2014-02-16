package ch.droptilllate.application.info;

import java.util.List;

import ch.droptilllate.application.model.EncryptedFileDob;

public class CRUDCryptedFileResult {
	private List<EncryptedFileDob> encryptedFileListError;
	private List<EncryptedFileDob> encryptedFileListSuccess;
	
	public List<EncryptedFileDob> getEncryptedFileListError() {
		return encryptedFileListError;
	}
	public void setEncryptedFileListError(
			List<EncryptedFileDob> encryptedFileListError) {
		this.encryptedFileListError = encryptedFileListError;
	}
	public List<EncryptedFileDob> getEncryptedFileListSuccess() {
		return encryptedFileListSuccess;
	}
	public void setEncryptedFileListSuccess(
			List<EncryptedFileDob> encryptedFileListSuccess) {
		this.encryptedFileListSuccess = encryptedFileListSuccess;
	}

}
