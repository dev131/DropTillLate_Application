package ch.droptilllate.application.converter;


import java.util.ArrayList;

import java.util.List;

import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.filesystem.info.FileInfo;

public class FileInfoConverter {

	/**
	 * Convert FileInfoSummery into CRUD*Result
	 * 
	 * @param fileInfoSuccessList
	 * @param fileInfoErrorList
	 * @param droppedFiles
	 * @return
	 */
	public CRUDCryptedFileInfo convertFileInfoList(
			List<FileInfo> fileInfoSuccessList,
			List<FileInfo> fileInfoErrorList,
			List<EncryptedFileDob> droppedFiles){
		List<EncryptedFileDob> encryptedFileListError = new ArrayList<EncryptedFileDob>();
		List<EncryptedFileDob> encryptedFileListSuccess = new ArrayList<EncryptedFileDob>();
		CRUDCryptedFileInfo result = new CRUDCryptedFileInfo();
		// Fill SuccessList
		for (FileInfo fileInfo : fileInfoSuccessList) {
			for (EncryptedFileDob fileDob : droppedFiles) {
				if (fileDob.getId() == fileInfo.getFileID()) {
					fileDob.setContainerId(fileInfo.getContainerInfo()
							.getContainerID());
					encryptedFileListSuccess.add(fileDob);
				}
			}
		}
		// Fill ErrorList
		for (FileInfo fileInfo : fileInfoErrorList) {
			for (EncryptedFileDob fileDob : droppedFiles) {
				if (fileDob.getId() == fileInfo.getFileID()) {
					encryptedFileListError.add(fileDob);
				}
			}
		}
		result.setEncryptedFileListError(encryptedFileListError);
		result.setEncryptedFileListSuccess(encryptedFileListSuccess);
		return result;
	}

}
