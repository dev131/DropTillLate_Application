package ch.droptilllate.application.info;

import java.util.List;

import ch.droptilllate.application.dnb.TillLateContainer;


public class CRUDContainerInfo {
	private List<TillLateContainer> encryptedContainerListError;
	private List<TillLateContainer> encryptedContainerListSuccess;
	
	public List<TillLateContainer> getEncryptedContainerListError() {
		return this.encryptedContainerListError;
	}
	public void setEncryptedContainerListError(
			List<TillLateContainer> encryptedContainerListError) {
		this.encryptedContainerListError = encryptedContainerListError;
	}
	public List<TillLateContainer> getEncryptedContainerListSuccess() {
		return this.encryptedContainerListSuccess;
	}
	public void setEncryptedContainerListSuccess(
			List<TillLateContainer> encryptedContainerListSuccess) {
		this.encryptedContainerListSuccess = encryptedContainerListSuccess;
	}

	
	
}
