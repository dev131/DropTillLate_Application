package ch.droptilllate.application.info;

import java.util.List;

import ch.droptilllate.application.dnb.EncryptedContainer;


public class CRUDContainerInfo {
	private List<EncryptedContainer> encryptedContainerListError;
	private List<EncryptedContainer> encryptedContainerListSuccess;
	
	public List<EncryptedContainer> getEncryptedContainerListError() {
		return this.encryptedContainerListError;
	}
	public void setEncryptedContainerListError(
			List<EncryptedContainer> encryptedContainerListError) {
		this.encryptedContainerListError = encryptedContainerListError;
	}
	public List<EncryptedContainer> getEncryptedContainerListSuccess() {
		return this.encryptedContainerListSuccess;
	}
	public void setEncryptedContainerListSuccess(
			List<EncryptedContainer> encryptedContainerListSuccess) {
		this.encryptedContainerListSuccess = encryptedContainerListSuccess;
	}

	
	
}
