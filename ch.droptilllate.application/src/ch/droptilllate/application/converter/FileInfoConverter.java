package ch.droptilllate.application.converter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.droptilllate.application.info.CRUDCryptedFileResult;
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
	public CRUDCryptedFileResult convertFileInfoList(
			List<FileInfo> fileInfoSuccessList,
			List<FileInfo> fileInfoErrorList,
			List<EncryptedFileDob> droppedFiles){
		List<EncryptedFileDob> encryptedFileListError = new ArrayList<EncryptedFileDob>();
		List<EncryptedFileDob> encryptedFileListSuccess = new ArrayList<EncryptedFileDob>();
		CRUDCryptedFileResult result = new CRUDCryptedFileResult();
		// Fill SuccessList
		for (FileInfo fileInfo : fileInfoSuccessList) {
			for (EncryptedFileDob fileDob : droppedFiles) {
				if (fileDob.getId() == fileInfo.getFileID()) {
					fileDob.setContainerID(fileInfo.getContainerInfo()
							.getContainerID());
					fileDob.setPath(fileInfo.getContainerInfo()
							.getParentContainerPath());
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
