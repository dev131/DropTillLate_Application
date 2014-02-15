package ch.droptilllate.application.com;

import java.util.List;

import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.model.EncryptedFileDob;

import ch.droptilllate.filesystem.info.*;
import ch.droptilllate.filesystem.api.FileHandlingSummary;


public interface IFileSystemCom {
	/**
	 * Encrypt Files
	 * @param droppedFiles
	 * @param containerPath
	 * @return List<FileInfo>
	 */
	public CRUDCryptedFileResult encryptFile(List<EncryptedFileDob> droppedFiles, String containerPath);
	/**
	 * Update Files List<FileInfo>
	 * @param filehandling_result  List<FileInfo>
	 */
	public CRUDCryptedFileResult decryptFile(List<EncryptedFileDob> droppedFiles, String containerPath);
	/**
	 * Delte File
	 * @param fileList
	 * @return List<EncryptedFileDob>
	 */
	public CRUDCryptedFileResult deleteFile(List<EncryptedFileDob> fileList);
	
	/**
	 * Move Files into new ShareRelation
	 * @param fileList
	 * @return FileInfoList
	 */
	public CRUDCryptedFileResult moveFiles(List<EncryptedFileDob> fileList, ShareFolder sharedFolder);

}
